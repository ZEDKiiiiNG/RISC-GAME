package edu.duke.risc.server;

import edu.duke.risc.shared.*;
import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.PayloadType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.GameUser;
import edu.duke.risc.shared.users.Master;
import edu.duke.risc.shared.users.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.duke.risc.shared.Configurations.*;
import static java.lang.Thread.sleep;

/**
 * Represent the main game logic and process controller.
 *
 * @author eason
 * @date 2021/3/10 0:01
 */
public class GameController {

    /**
     * The game board
     */
    private GameBoard board;

    /**
     * The current server is regarded as the root
     */
    private final GameUser root;

    /**
     * Initial colors
     */
    private final List<UserColor> colors = new ArrayList<>();

//    /**
//     * Socket connection with the server
//     */
//    private ServerSocket serverSocket;

    /**
     * Player connections
     */
    private final Map<Player, SocketCommunicator> playerConnections;

    /**
     * Cyclic barrier
     */
    private final ThreadBarrier barrier;

    /**
     * The buffered reader who reads input from the console
     */
    private final BufferedReader reader;


    /**
     * Maximum number of players
     */
    private int maxPlayer;

    /**
     * id with corresponding communicator
     */
    private final Map<String, SocketCommunicator> userConnections;;

    /**
     * id with corresponding player
     */
    private Map<String, Player> idMap;


    /**
     * id  of gamecontroller
     */
    private int gameId;



    /**
     * stage of the controller
     */
    private String stage;

    /**
     * Constructor
     */
    public GameController(int gameId, int maxPlayer) {
        barrier = new ThreadBarrier(maxPlayer);
        root = new Master();
        playerConnections = new HashMap<>();
        idMap = new HashMap<>();
        userConnections = new HashMap<>();
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.gameId = gameId;
        this.maxPlayer = maxPlayer;
        this.addColors();
        this.stage = STAGE_CREATE;
//        try {
//            serverSocket = new ServerSocket(Configurations.DEFAULT_SERVER_PORT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    public void run() {
        try {
            this.startGame();
        }
        catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * Start the game
     *
     * @throws IOException IOException
     */
    public void startGame() throws IOException, InterruptedException {
        this.initWorld();
        this.waitPlayers();
        this.stage = STAGE_ASSIGN;
        this.placementPhase();
        this.stage = STAGE_MOVE;
        this.moveAttackPhase();
        this.stage = GAMEFINISHED;
    }

    /**
     * Read the user input and initialize the world according to number of players
     */
    private void initWorld() {
//        while (true) {
//            System.out.println("Please enter number of players (2-5)");
//            try {
//                String input = this.reader.readLine();
//                int numPlayer = Integer.parseInt(input);
//                if (numPlayer >= 2 && numPlayer <= 5) {
//                    this.maxPlayer = numPlayer;
//                    break;
//                } else {
//                    System.out.println("Should only be number 2,3,4,5");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input");
//            }
//        }
        board = new GameBoard(maxPlayer);
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
            List<Action> upgradeUnitsCacheActions = new ArrayList<>();
            List<Action> upgradeTechCacheActions = new ArrayList<>();
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
                List<Action> moveActions =
                        (List<Action>) request.getContents().get(Configurations.REQUEST_MOVE_ACTIONS);
                List<Action> attackActions =
                        (List<Action>) request.getContents().get(Configurations.REQUEST_ATTACK_ACTIONS);
                List<Action> upgradeUnitsAction =
                        (List<Action>) request.getContents().get(Configurations.REQUEST_UPGRADE_UNITS_ACTIONS);
                List<Action> upgradeTechAction =
                        (List<Action>) request.getContents().get(Configurations.REQUEST_UPGRADE_TECH_ACTIONS);
                //validate success, response the client with success message and continues the next request
                moveCacheActions.addAll(moveActions);
                attackCacheActions.addAll(attackActions);
                upgradeUnitsCacheActions.addAll(upgradeUnitsAction);
                upgradeTechCacheActions.addAll(upgradeTechAction);
                numberOfRequestRequired -= 1;
            }
            //with all requests received, process them simultaneously

            //conduct upgrade tech actions
            for (Action action : upgradeTechCacheActions) {
                try {
                    String result = action.apply(this.board);
                    logger.append(result);
                } catch (InvalidActionException e) {
                    //simply ignore this
                    logger.append("FAILED: ").append(action).append(e.getMessage()).append(System.lineSeparator());
                }
            }

            //first conduct upgrade units actions
            for (Action action : upgradeUnitsCacheActions) {
                try {
                    String result = action.apply(this.board);
                    logger.append(result);
                } catch (InvalidActionException e) {
                    //simply ignore this
                    logger.append("FAILED: ").append(action).append(e.getMessage()).append(System.lineSeparator());
                }
            }

            //then conduct move actions
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
            List<Action> validAttackList = attackCacheActions.stream().filter((action -> {
                String error;
                if ((error = action.isValid(board)) != null) {
                    logger.append("FAILED: ").append(action).append(" : ").append(error).append(System.lineSeparator());
                    return false;
                } else {
                    return true;
                }
            })).collect(Collectors.toList());

            Collections.shuffle(validAttackList);
            for (Action action : validAttackList) {
                try {
                    action.applyBefore(this.board);
                } catch (InvalidActionException e) {
                    //simply ignore this
                    logger.append("FAILED: ").append(action).append(e.getMessage()).append(System.lineSeparator());
                }
            }
            for (Action action : validAttackList) {
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
                this.terminateProcess(logger.toString());
                return;
            } else {
                broadcastUpdatedMaps(logger.toString(), PayloadType.UPDATE);
            }
        }
    }

    /**
     * Send back error message to the client, indicating invalid input or status.
     *
     * @param request        PayloadObject
     * @param validateResult validateResult
     * @throws IOException IOException
     */
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
    private void waitPlayers() throws IOException, InterruptedException {
//        while (this.board.getPlayers().size() < maxPlayer) {
//            int playerIndex = playerConnections.size();
//            System.out.println("Waiting for " + (maxPlayer - playerIndex)
//                    + " players to join the game.");
//            Socket clientSocket = serverSocket.accept();
//            SocketCommunicator communicator = new SocketCommunicator(clientSocket);
        int playerIndex = 0;

        for(Map.Entry<String, SocketCommunicator> entry : userConnections.entrySet()){
            Player player = new Player(playerIndex + 1, this.colors.get(playerIndex));
            //assign territories
            this.assignTerritories(player, this.board);
            // validate
            SocketCommunicator communicator  = entry.getValue();
            playerConnections.put(player, communicator);
            idMap.put(entry.getKey(),player);
            System.out.println("Successfully connect with player " + player);
            PlayerHandler handler = new PlayerHandler(communicator, barrier/*, serverSocket,playerConnections, player*/);
            handler.start();
            playerIndex++;
        }
        System.out.println("All player are ready");
        this.board.forwardPlacementPhase();
        //share game map with every player
        broadcastUpdatedMaps("", PayloadType.UPDATE);
    }


    /**
     * add user connections
     *
     * @param id     the id of user
     * @param socketCommunicator the communicator of the id
     */

    public void addUserConnections(String id, SocketCommunicator socketCommunicator){
        this.userConnections.put(id,socketCommunicator);
    }

    /**
     * update user connections
     */

    public void updateConnections(){
        for(Map.Entry<String, SocketCommunicator> entry : userConnections.entrySet()){
            String id = entry.getKey();
            SocketCommunicator socketCommunicator = entry.getValue();
            Player player = idMap.get(id);

            playerConnections.put(player, socketCommunicator);
        }
    }



    /**
     * Broadcast messages to all (alive) clients
     *
     * @param lastLog     last log of the actions
     * @param payloadType broadcast response
     * @throws IOException IOException
     */
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

    /**
     * Send directly package back to the client
     *
     * @param playerId      playerId
     * @param payloadObject payloadObject
     * @throws IOException IOException
     */
    private void sendPackageToPlayer(int playerId, PayloadObject payloadObject) throws IOException {
        Player player = this.board.findPlayer(playerId);
        SocketCommunicator communicator = this.playerConnections.get(player);
        communicator.writeMessage(payloadObject);
    }

    /**
     * Initialize all colors
     */
    private void addColors() {
        colors.add(UserColor.BLUE);
        colors.add(UserColor.GREEN);
        colors.add(UserColor.RED);
        colors.add(UserColor.WHITE);
        colors.add(UserColor.DARK);
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

    /**
     * Get the logger
     *
     * @return string builder
     */
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

    /**
     * Check whether the user wants to quit the game
     *
     * @param request request
     * @return true if the user wants to quit, false if not
     * @throws IOException IOException
     */
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

    /**
     * Terminate the current server JVM process
     *
     * @param lastLog lastLog
     * @throws IOException IOException
     */
    private void terminateProcess(String lastLog) throws IOException {
        this.broadcastUpdatedMaps(lastLog, PayloadType.GAME_OVER);
        for (Map.Entry<Player, SocketCommunicator> socketCommunicatorEntry : this.playerConnections.entrySet()) {
            socketCommunicatorEntry.getValue().terminate();
        }
        //this.serverSocket.close();

//        System.out.println("Connection resources closed");
//        System.exit(0);
    }

    public String getStage() {
        return stage;
    }
    public GameBoard getBoard() {
        return board;
    }
    public Map<String, Player> getIdMap() {
        return idMap;
    }
    public int getMaxPlayer() {
        return maxPlayer;
    }

    public Map<String, SocketCommunicator> getUserConnections() {
        return userConnections;
    }
}
