package edu.duke.risc.server;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.PlayerHandler;
import edu.duke.risc.shared.SocketCommunicator;
import edu.duke.risc.shared.ThreadBarrier;
import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.PayloadType;
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
import java.util.stream.Collectors;

import static edu.duke.risc.shared.Configurations.*;

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
        this.moveAttackPhase();
    }

    /**
     * Do the placement phase
     *
     * @throws IOException IOException
     */
    private void placementPhase() throws IOException {
        int numberOfRequestRequired = this.board.getShouldWaitPlayers();
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
        //todo create logger here
        broadcastUpdatedMaps("", PayloadType.UPDATE);
    }

    /**
     * Do the move and attack phase
     *
     * @throws IOException IOException
     */
    private void moveAttackPhase() throws IOException {
        while (true) {
            this.board.setGameStart();
            int numberOfRequestRequired = this.board.getShouldWaitPlayers();
            List<Action> moveCacheActions = new ArrayList<>();
            List<Action> attackCacheActions = new ArrayList<>();
            StringBuilder logger = getLogger();
            while (numberOfRequestRequired > 0) {
                PayloadObject request = this.barrier.consumeRequest();
                if (this.checkQuit(request)) {
                    continue;
                }
                //validate move_attack_request
                if (request.getMessageType() != PayloadType.REQUEST
                        || request.getReceiver() != this.root.getId()
                        || !request.getContents().containsKey(Configurations.REQUEST_MOVE_ACTIONS)
                        || !request.getContents().containsKey(Configurations.REQUEST_ATTACK_ACTIONS)) {
                    sendBackErrorMessage(request, "Invalid request type");
                    continue;
                }
                List<Action> moveActions = (List<Action>) request.getContents().get(Configurations.REQUEST_MOVE_ACTIONS);
                List<Action> attackActions =
                        (List<Action>) request.getContents().get(Configurations.REQUEST_ATTACK_ACTIONS);
                //validate success, response the client with success message and continues the next request
                moveCacheActions.addAll(moveActions);
                attackCacheActions.addAll(attackActions);
                numberOfRequestRequired -= 1;
            }
            //with all requests received, process them simultaneously

            //first conduct move actions
            for (Action action : moveCacheActions) {
                try {
                    String result = action.apply(this.board);
                    logger.append(result);
                } catch (InvalidActionException e) {
                    //simply ignore this
                    logger.append("FAILED: ").append(action).append(e.getMessage()).append(System.lineSeparator());
                }
            }

            //then conduct attack actions
            List<Action> validList = attackCacheActions.stream().filter((action -> action.isValid(board) == null))
                    .collect(Collectors.toList());

            for (Action action : validList) {
                try {
                    action.applyBefore(this.board);
                } catch (InvalidActionException e) {
                    //simply ignore this
                    logger.append("FAILED: ").append(action).append(e.getMessage()).append(System.lineSeparator());
                }
            }
            for (Action action : validList) {
                try {
                    String result = action.applyAfter(this.board);
                    logger.append(result);
                } catch (InvalidActionException e) {
                    //simply ignore this
                    logger.append("FAILED: ").append(action).append(e.getMessage()).append(System.lineSeparator());
                }
            }
            //grow the territories owned by players
            String growResult = this.board.territoryGrow();

            logger.append(growResult);
            System.out.println(logger.toString());

            if (this.checkAndUpdatePlayerStatus()) {
                this.terminateProcess();
            } else {
                broadcastUpdatedMaps(logger.toString(), PayloadType.UPDATE);
            }
        }
    }

    private void sendBackErrorMessage(PayloadObject request, String validateResult) throws IOException {
        Map<String, Object> response = new HashMap<>(5);
        response.put(Configurations.ERR_MSG, validateResult);
        PayloadObject err = new PayloadObject(this.root.getId(), request.getSender(),
                PayloadType.ERROR, response);
        this.sendPackageToPlayer(request.getSender(), err);
    }

    /***
     * validateActions
     *
     * @param actions actions
     * @param board board
     * @return errMsg if not valid, null for valid
     */
    private String validateActions(List<Action> actions, GameBoard board) {
        for (Action action : actions) {
            String errMsg;
            if ((errMsg = action.isValid(board)) != null) {
                return errMsg;
            }
        }
        return null;
    }

    /**
     * Waiting for players phase
     *
     * @throws IOException IOException
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
        broadcastUpdatedMaps("", PayloadType.UPDATE);
    }

    private void broadcastUpdatedMaps(String lastLog, PayloadType payloadType) throws IOException {
        for (Map.Entry<Player, SocketCommunicator> entry : playerConnections.entrySet()) {
            Player player = entry.getKey();
            Map<String, Object> content = new HashMap<>(10);
            content.put(GAME_BOARD_STRING, this.board);
            content.put(LOGGER_STRING, lastLog);
            content.put(PLAYER_STRING, player.getId());
            PayloadObject payloadObject = new PayloadObject(root.getId(), player.getId(), payloadType, content);
            entry.getValue().writeMessage(payloadObject);
        }
    }

    private void sendPackageToPlayer(int playerId, PayloadObject payloadObject) throws IOException {
        Player player = this.board.findPlayer(playerId);
        SocketCommunicator communicator = this.playerConnections.get(player);
        communicator.writeMessage(payloadObject);
    }

    private void addColors() {
        colors.add(UserColor.BLUE);
        colors.add(UserColor.GREEN);
        colors.add(UserColor.RED);
    }

    /**
     * Assign territories to player.
     *
     * @param player    player
     * @param gameBoard gameBoard
     */
    private void assignTerritories(Player player, GameBoard gameBoard) {
        Set<Integer> assignedTerritories = gameBoard.addPlayer(player);
        player.setOwnedTerritories(assignedTerritories);
    }

    private StringBuilder getLogger() {
        StringBuilder builder = new StringBuilder();
        builder.append("ACTION LOGS").append(System.lineSeparator());
        builder.append("------------").append(System.lineSeparator());
        return builder;
    }

    /**
     * If player owns no territory, mark him as LOSE. If one player owns all territories, mark him as WIN.
     *
     * @return true if the game should be over, false if not
     */
    private boolean checkAndUpdatePlayerStatus() {
        for (Map.Entry<Integer, Player> playerEntry : this.board.getPlayers().entrySet()) {
            Player player = playerEntry.getValue();
            if (player.getOwnedTerritories().size() == 0) {
                player.markLost();
            } else if (player.getOwnedTerritories().size() == this.board.getTerritoriesSize()) {
                player.markWin();
                this.board.setGameOver();
                System.out.println(player.getColor() + " Player with ID " + player.getId() + " wins.");
            }
        }
        return this.board.isGameOver();
    }

    private boolean checkQuit(PayloadObject request) throws IOException {
        if (request.getMessageType() == PayloadType.QUIT) {
            PayloadObject response = new PayloadObject(this.root.getId(), request.getSender(), PayloadType.QUIT);
            Player player = this.board.findPlayer(request.getSender());
            SocketCommunicator communicator = this.playerConnections.get(player);
            if (communicator.writeMessage(response)) {
                communicator.terminate();
                this.playerConnections.remove(player);
            }
            return true;
        }
        return false;
    }

    private void terminateProcess() throws IOException {
        this.broadcastUpdatedMaps("GAME OVER", PayloadType.GAME_OVER);
        System.out.println("GAME OVER");
        for (Map.Entry<Player, SocketCommunicator> socketCommunicatorEntry : this.playerConnections.entrySet()) {
            socketCommunicatorEntry.getValue().terminate();
        }
        this.serverSocket.close();
        System.out.println("Connection resources closed");
        System.exit(0);
    }

}
