package edu.duke.risc.server;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.PlayerHandler;
import edu.duke.risc.shared.SocketCommunicator;
import edu.duke.risc.shared.ThreadBarrier;
import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.PayloadType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.GameUser;
import edu.duke.risc.shared.users.Master;
import edu.duke.risc.shared.users.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.duke.risc.shared.Configurations.GAME_BOARD_STRING;
import static edu.duke.risc.shared.Configurations.PLAYER_STRING;

/**
 * @author eason
 * @date 2021/3/10 0:01
 */
public class GameController {

    private GameBoard board;

    private GameUser root;

    private final List<UserColor> colors = new ArrayList<>();

    private ServerSocket serverSocket;

    private Map<Player, SocketCommunicator> playerConnections;

    private ThreadBarrier barrier;

    public GameController() {
        barrier = new ThreadBarrier(Configurations.MAX_PLAYERS);
        root = new Master();
        board = new GameBoard();
        playerConnections = new HashMap<>();

        this.addColors();
        try {
            serverSocket = new ServerSocket(Configurations.DEFAULT_SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() throws IOException {
        this.waitPlayers();
        this.placementPhase();
        this.move_attack_Phase();
    }

    /**
     * Do the placement phase
     *
     * @throws IOException
     */
    private void placementPhase() throws IOException {
        int numberOfRequestRequired = this.board.getPlayers().size();
        List<Action> cacheActions = new ArrayList<>();
        while (numberOfRequestRequired > 0) {
            PayloadObject request = this.barrier.consumeRequest();
            //validate placement request
            if (request.getMessageType() != PayloadType.REQUEST
                    || request.getReceiver() != this.root.getId()
                    || !request.getContents().containsKey(Configurations.REQUEST_PLACEMENT_ACTIONS)) {
                sendBackErrorMessage(request, "Invalid request type");
                continue;
            }
            List<Action> actions = (List<Action>) request.getContents().get(Configurations.REQUEST_PLACEMENT_ACTIONS);
            String validateResult = validateActions(actions, this.board);
            if (validateResult != null) {
                //error occurs, send response back to the client.
                sendBackErrorMessage(request, validateResult);
                continue;
            } else {
                //validate success, response the client with success message and continues the next request
                cacheActions.addAll(actions);
                numberOfRequestRequired -= 1;
            }
        }
        //with all requests received, process them simultaneously
        for (Action action : cacheActions) {
            try {
                action.apply(this.board);
            } catch (InvalidActionException e) {
                //simply ignore this
                System.out.println(e.getMessage());
            }
        }
        broadcastUpdatedMaps();
    }

    /**
     * Do the move and attack phase
     *
     * @throws IOException
     */
    private void move_attack_Phase() throws IOException {
        int numberOfRequestRequired = this.board.getPlayers().size();
        List<Action> move_cacheActions = new ArrayList<>();
        List<Action> attack_cacheActions = new ArrayList<>();
        while (numberOfRequestRequired > 0) {
            PayloadObject request = this.barrier.consumeRequest();
            //validate move_attack_request
            if (request.getMessageType() != PayloadType.REQUEST
                    || request.getReceiver() != this.root.getId()
                    || !request.getContents().containsKey(Configurations.REQUEST_MOVE_ACTIONS)
                    || !request.getContents().containsKey(Configurations.REQUEST_ATTACK_ACTIONS)) {
                sendBackErrorMessage(request, "Invalid request type");
                continue;
            }
            List<Action> moveActions = (List<Action>) request.getContents().get(Configurations.REQUEST_MOVE_ACTIONS);
            List<Action> attackActions = (List<Action>) request.getContents().get(Configurations.REQUEST_ATTACK_ACTIONS);
            String moveValidateResult = validateActions(moveActions, this.board);
            if (moveValidateResult != null) {
                //error occurs, send response back to the client.
                sendBackErrorMessage(request, moveValidateResult);
                continue;
            }
            String attackValidateResult = validateActions(attackActions, this.board);
            if (attackValidateResult != null) {
                //error occurs, send response back to the client.
                sendBackErrorMessage(request, attackValidateResult);
                continue;
            } else {
                //validate success, response the client with success message and continues the next request
                move_cacheActions.addAll(moveActions);
                attack_cacheActions.addAll(attackActions);
                numberOfRequestRequired -= 1;
            }
        }
        //with all requests received, process them simultaneously
        //first move then attack
        for (Action action : move_cacheActions) {
            try {
                action.apply(this.board);
            } catch (InvalidActionException e) {
                //simply ignore this
                System.out.println(e.getMessage());
            }
        }
        for (Action action : attack_cacheActions) {
            try {
                action.apply(this.board);
            } catch (InvalidActionException e) {
                //simply ignore this
                System.out.println(e.getMessage());
            }
        }
        broadcastUpdatedMaps();
    }

    private void sendBackErrorMessage(PayloadObject request, String validateResult) {
        Map<String, Object> response = new HashMap<>(5);
        response.put(Configurations.ERR_MSG, validateResult);
        PayloadObject err = new PayloadObject(this.root.getId(), request.getSender(),
                PayloadType.ERROR, response);
        this.sendPackageToPlayer(request.getSender(), err);
    }

    /***
     * validateActions
     * @param actions
     * @param board
     * @return
     */
    private String validateActions(List<Action> actions, GameBoard board) {
        for (Action action : actions) {
            String errMsg;
            if ((errMsg = action.isValid(this.board)) != null) {
                return errMsg;
            }
        }
        return null;
    }

    /**
     * Waiting for players phase
     *
     * @throws IOException
     */
    private void waitPlayers() throws IOException {
        while (this.board.getPlayers().size() < Configurations.MAX_PLAYERS) {
            int playerIndex = playerConnections.size();
            System.out.println("Waiting for " + (Configurations.MAX_PLAYERS - playerIndex)
                    + " players to join the game.");
            Socket clientSocket = serverSocket.accept();
            SocketCommunicator communicator = new SocketCommunicator(clientSocket);
            Player player = new Player(playerIndex + 1, this.colors.get(playerIndex));
            //assign territories
            this.assignTerritories(player, this.board);
            playerConnections.put(player, communicator);
            System.out.println("Successfully connect with player " + player);
            PlayerHandler handler = new PlayerHandler(communicator, barrier);
            handler.start();
        }
        System.out.println("All player are ready");
        this.board.forwardPlacementPhase();
        //share game map with every player
        broadcastUpdatedMaps();
    }

    private void broadcastUpdatedMaps() throws IOException {
        for (Map.Entry<Player, SocketCommunicator> entry : playerConnections.entrySet()) {
            Player player = entry.getKey();
            Map<String, Object> content = new HashMap<>(10);
            content.put(GAME_BOARD_STRING, this.board);
            content.put(PLAYER_STRING, player.getId());
            PayloadObject payloadObject = new PayloadObject(root.getId(), player.getId(), PayloadType.UPDATE, content);
            entry.getValue().writeMessage(payloadObject);
        }
    }

    private void sendPackageToPlayer(int playerId, PayloadObject payloadObject) {

    }

    private void addColors() {
        colors.add(UserColor.BLUE);
        colors.add(UserColor.GREEN);
        colors.add(UserColor.RED);
    }

    /**
     * Assign territories to player.
     *
     * @param player
     * @param gameBoard
     */
    private void assignTerritories(Player player, GameBoard gameBoard) {
        Set<Integer> assignedTerritories = gameBoard.addPlayer(player);
        player.setOwnedTerritories(assignedTerritories);
    }

}
