package edu.duke.risc.client;

import edu.duke.risc.shared.Communicable;
import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.SocketCommunicator;
import edu.duke.risc.shared.actions.*;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.PayloadType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.*;
import edu.duke.risc.shared.users.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

import static edu.duke.risc.shared.Configurations.*;

/**
 * Represents the client-side codes
 *
 * @author eason
 * @date 2021/3/10 13:58
 */
public class ClientController extends WaitPlayerUI {

    /**
     * The game board
     */
    private GameBoard gameBoard;

    public Communicable getCommunicator() {
        return communicator;
    }

    /**
     * Socket communicator
     */
    private Communicable communicator;

    public void setReadExitThread(ReadExitThread readExitThread) {
        this.readExitThread = readExitThread;
    }

    /**
     * Buffered reader which reads from console
     */
    private BufferedReader consoleReader;

    /**
     * Logger info of the last actions
     */
    private String loggerInfo;

    /**
     * Current player id
     */
    private Integer playerId = Configurations.DEFAULT_PLAYER_ID;

    public ReadExitThread getReadExitThread() {
        return readExitThread;
    }

    /**
     * Thread which reads exit signal
     */
    private ReadExitThread readExitThread;
    /*

     */
    private WaitPlayerUI waitPlayerUI;
    /**
     * stage of the client
     */
    private String stage;

    /**
     * Constructor
     *
     * @throws IOException IOException
     */
    public Player getMyself(){
        return this.gameBoard.getPlayers().get(playerId);
    }

    public String getLoggerInfo() {
        return loggerInfo;
    }

    public String getStage() {
        return stage;
    }

    public GameBoard getGameBoard(){
        return this.gameBoard;
    }

    public Integer getPlayerId(){
        return this.playerId;
    }

    public ClientController() throws IOException {
        //no need reader in GUI
        //this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
        //here communicator is first initialized when constructed
        Socket socket = new Socket(ClientConfigurations.LOCALHOST, Configurations.DEFAULT_SERVER_PORT);
        communicator = new SocketCommunicator(socket);
    }

    /**
     * Start the game, doing the game logics
     *
     * @throws IOException IOException
     */

    /**
     * Try to connect with the server and wait for other users
     */
    public void tryConnectAndWait() {
        try {
            //try connect to the server
            //the user is already in a game, skip
            if(!stage.equals(STAGE_CREATE)){
                System.out.println("You are current the player: " + this.gameBoard.getPlayers().get(playerId));
                return;
            }
            System.out.println(ClientConfigurations.CONNECT_SUCCESS_MSG);


            //waiting for other users
            this.waitAndReadServerResponse();

            //System.out.println("You are current the player: " + this.gameBoard.getPlayers().get(playerId));
            //waitPlayerUI.start(new Stage());
            //System.out.println("You are current the player: " + this.gameBoard.getPlayers().get(playerId));
        } catch (UnmatchedReceiverException | InvalidPayloadContent | ServerRejectException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Log in server and wait response
     *
     * @return String indicate login status
     * @throws IOException            IOException
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public String logInValidate(String choose, String id, String pwd) throws IOException, ClassNotFoundException {
       //construct a payload , send and read server response
        System.out.println("id passed in loginValidate is \n\n\n\n" + id +"\n\n\n\n\n\n");
        PayloadObject readObject = null;
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("ID",id);
        userInfo.put("PWD",pwd);
        PayloadObject writeObject = choose.equals("L") ? new PayloadObject(DEFAULT_PLAYER_ID , MASTER_ID , PayloadType.LOGIN, userInfo) :new PayloadObject(DEFAULT_PLAYER_ID , MASTER_ID , PayloadType.SIGNUP, userInfo) ;
        communicator.writeMessage(writeObject);
        readObject = communicator.receiveMessage();
        if ((readObject.getContents().containsKey(SUCCESS_LOG))){
            return null;
        }
        else if((readObject.getContents().containsKey(OCCUPIED_LOG))){
            System.out.println("The user Id is occupied ");
            return "The user Id is occupied ";
        }
        else{
            System.out.println("Cannot find corresponding id or password is wrong");
            return "Cannot find corresponding id or password is wrong";
        }
    }

    /**
     * Log in server and wait response
     *
     * @return String error mess(null on success)
     * @throws IOException            IOException
     * @throws ClassNotFoundException ClassNotFoundException
     */

    /*
    * Note that in joining a new gamer: gameId represents the game id
    * in creating a new game, gameId represents the max player num*/
    public String gameChoose(String choose, int gameId) throws IOException, ClassNotFoundException, UnmatchedReceiverException, InvalidPayloadContent {
            PayloadObject readObject = null;

            //if player num is valid?
            if(choose.equals("N")) {
                if (gameId < 2 || gameId > 5){
                    System.out.println("the game numbers should be in range 2 to 5 ");
                    return "the game numbers should be in range 2 to 5 ";
                }
            }
            Map<String, Object> gameInfo = new HashMap<>();
            gameInfo.put("CHOOSE",choose);
            gameInfo.put("ID",gameId);
            PayloadObject writeObject = new PayloadObject(DEFAULT_PLAYER_ID , MASTER_ID , PayloadType.LOGIN, gameInfo);
            communicator.writeMessage(writeObject);
            //read the response
            readObject = communicator.receiveMessage();
            if ((readObject.getContents().containsKey(SUCCESSFOUND))){
                //create stage just join
                if(readObject.getContents().get("STAGE").equals(STAGE_CREATE)){//if stage == STAGE_CREATE(create user and last exit before assign)
                    stage = STAGE_CREATE;//set stage
                    System.out.println("Successfully create/newly join a game with ID "+readObject.getContents().get("GAMEID"));
                    return null;
                }
                else{//if last exit after assign
                    // other stage update the info and move foward
                    stage = (String) readObject.getContents().get("STAGE");//update local stage

                    if (playerId == Configurations.DEFAULT_PLAYER_ID) {//why????????????????????????????
                        playerId = readObject.getReceiver(); //update playerId with server based on user Id
                    } else if (!readObject.getReceiver().equals(playerId)) {//reconnect?
                        throw new UnmatchedReceiverException("the " + playerId + "is not matched with " + readObject.getReceiver());//unlikely?
                    }
                    //unpack message(update local board)
                    Map<String, Object> contents = readObject.getContents();
                    if (contents.containsKey(GAME_BOARD_STRING)
                            && contents.containsKey(PLAYER_STRING)) {
                        this.gameBoard = (GameBoard) contents.get(GAME_BOARD_STRING);
                        this.playerId = (Integer) contents.get(PLAYER_STRING);
                        this.loggerInfo = (String) contents.get(LOGGER_STRING);
                    } else {
                        throw new InvalidPayloadContent("do not contain gameBoard object");
                    }
                    System.out.println("Successfully reconnect");
                    return null;
                }
            }
            else if((readObject.getContents().containsKey(USERNOTFOUND))){
                System.out.println("The user Id is not in the game ");
                return "The user Id is not in the game ";
            }
            else{
                System.out.println("Cannot find corresponding game");
                return "Cannot find corresponding game";
            }
    }

    /**
     * Assign units and doing the placement
     *
     * @throws IOException IOException
     */

    public void clientAssignUnits(List<Action> actions, String input){
        Action action = null;
        try {
            action = this.validateInputAndGeneratePlacementAction(input, this.gameBoard, playerId);
            action.simulateApply(this.gameBoard);
            actions.add(action);
        } catch (InvalidInputException | InvalidActionException e) {
            System.out.println(e.getMessage());
        }
    }





    public void assignUnits(List<Action> actions) throws IOException {
        while (true) {

            //sending to the server
            //constructing payload objects
            Map<String, Object> content = new HashMap<>(3);
            content.put(Configurations.REQUEST_PLACEMENT_ACTIONS, actions);
            PayloadObject request = new PayloadObject(this.playerId,
                    Configurations.MASTER_ID, PayloadType.REQUEST, content);
            try {
                this.sendMessage(request);
                System.out.println("Actions sent, please wait other players finish placing");
                this.waitAndReadServerResponse();
                //System.out.println(this.loggerInfo);
                //this.gameBoard.displayBoard();
            } catch (InvalidPayloadContent | ServerRejectException | UnmatchedReceiverException exception) {
                //if server returns failed, re-do the actions again
                exception.printStackTrace();
                continue;
            }
            System.out.println("Successfully finished placement phase");
            break;
        }
        stage = STAGE_MOVE;
    }

    /**
     * Send message to the server
     *
     * @param payloadObject payloadObject
     * @throws IOException IOException
     */
    public void sendMessage(PayloadObject payloadObject) throws IOException {
        this.communicator.writeMessage(payloadObject);
    }

    /**
     * Listening to the move and attack actions and sends request to the server
     *
     * @throws IOException IOException
     */
    public String moveAndAttack(List<Action> moveActions, List<Action> attackActions,
                              List<Action> upgradeTechActions, List<Action> upgradeUnitsActions) throws IOException {
    while(true) {
        //sending to the server
        //constructing payload objects
        Map<String, Object> content = new HashMap<>(3);
        content.put(Configurations.REQUEST_MOVE_ACTIONS, moveActions);
        content.put(Configurations.REQUEST_ATTACK_ACTIONS, attackActions);
        content.put(Configurations.REQUEST_UPGRADE_UNITS_ACTIONS, upgradeUnitsActions);
        content.put(Configurations.REQUEST_UPGRADE_TECH_ACTIONS, upgradeTechActions);
        PayloadObject request = new PayloadObject(this.playerId,
                Configurations.MASTER_ID, PayloadType.REQUEST, content);
        try {
            this.sendMessage(request);
            System.out.println("Actions sent, please wait other players finish placing");
            this.waitAndReadServerResponse();
            System.out.println(this.loggerInfo);
            return this.loggerInfo;
        } catch (InvalidPayloadContent | ServerRejectException | UnmatchedReceiverException exception) {
            //if server returns failed, re-do the actions again
            exception.printStackTrace();
            continue;
        }
    }

    }

    /**
     * Entering the observer's mode
     */
    public void observerMode() {
        try {
//            System.out.println("You lost the game, entering Observer Mode, you can type exit to quit...");
            this.readExitThread = new ReadExitThread(this.consoleReader, this.communicator, this.playerId);
            this.readExitThread.run();
            while (true) {
                this.waitAndReadServerResponse();
                System.out.println(this.loggerInfo);
                this.gameBoard.displayBoard();
            }
        } catch (UnmatchedReceiverException | InvalidPayloadContent | ServerRejectException e) {
            e.printStackTrace();
        }
    }

    /**
     * conductMoveOrAttack
     *
     * @param actions    action
     * @param actionType 0 for move and 1 for attack
     * @throws IOException
     */
    public void conductMoveOrAttack(List<Action> actions, int actionType, String moveInput)
            throws InvalidInputException, InvalidActionException {
//        System.out.println("Please enter instruction in the following format: " +
//                "<sourceTerritoryId>,<destinationId>;<UnitType1>,<amount1>;<UnitType2>,<amount2>");
//        String moveInput = this.consoleReader.readLine();
        Action action;

        action = this.readAttackOrMoveAction(moveInput, this.gameBoard, playerId, actionType);
        action.simulateApply(this.gameBoard);
        actions.add(action);

    }

    /**
     * conductUpgradeUnits
     *
     * @param actions action
     */
    public void conductUpgradeTechLevel(List<Action> actions) throws InvalidActionException {
        Action action;

        action = new UpgradeTechAction(playerId, ActionType.UPGRADE_TECH);
        action.simulateApply(this.gameBoard);
        actions.add(action);

    }

    /**
     * conductUpgradeUnits
     *
     * @param actions action
     * @throws IOException IOException
     */
    public void conductUpgradeUnits(List<Action> actions, String upgradeUnitInput) throws IOException, InvalidInputException, InvalidActionException {
//        System.out.println("Please enter instruction in the following format: " +
//                "<targetTerritoryId>,<UnitType>,<amount>");
//        String upgradeUnitInput = this.consoleReader.readLine();
        Action action;

        action = this.readUpgradeUnitAction(upgradeUnitInput, this.gameBoard, playerId);
        action.simulateApply(this.gameBoard);
        actions.add(action);

    }

    /**
     * waitAndReadServerResponse
     *
     * @throws UnmatchedReceiverException UnmatchedReceiverException
     * @throws InvalidPayloadContent      InvalidPayloadContent
     * @throws ServerRejectException      ServerRejectException
     */
    public void waitAndReadServerResponse() throws UnmatchedReceiverException, InvalidPayloadContent, ServerRejectException {
        PayloadObject response = null;
        try {
            response = waitAndRead();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        //check desired receiver
        if (playerId == Configurations.DEFAULT_PLAYER_ID) {
            playerId = response.getReceiver();
        } else if (!response.getReceiver().equals(playerId)) {
            throw new UnmatchedReceiverException("the " + playerId + "is not matched with " + response.getReceiver());
        }
        //unpack message
        Map<String, Object> contents = response.getContents();
        switch (response.getMessageType()) {
            case INFO:
                break;
            case SUCCESS:
                System.out.println(response.getContents().get(Configurations.SUCCESS_MSG));
                return;
            case ERROR:
                throw new ServerRejectException("Action requests are rejected by server"
                        + response.getContents().get(Configurations.ERR_MSG));
            case QUIT:
                System.out.println("Request to disconnect");
                this.terminateProcess();
            case UPDATE:
                if (contents.containsKey(GAME_BOARD_STRING)
                        && contents.containsKey(PLAYER_STRING)) {
                    this.gameBoard = (GameBoard) contents.get(GAME_BOARD_STRING);
                    this.playerId = (Integer) contents.get(PLAYER_STRING);
                    this.loggerInfo = (String) contents.get(LOGGER_STRING);
                } else {
                    throw new InvalidPayloadContent("do not contain gameBoard object");
                }
                break;
            case GAME_OVER:
                this.gameBoard = (GameBoard) contents.get(GAME_BOARD_STRING);
                this.playerId = (Integer) contents.get(PLAYER_STRING);
                this.loggerInfo = (String) contents.get(LOGGER_STRING);
                System.out.println(loggerInfo);
                this.printWinnerInfo();
                this.terminateProcess();
                break;
            default:
                throw new IllegalArgumentException("Invalid message type");
        }
    }

    /**
     * Wait server response and wait there
     *
     * @return PayloadObject as response
     * @throws IOException            IOException
     * @throws ClassNotFoundException ClassNotFoundException
     */
    private PayloadObject waitAndRead() throws IOException, ClassNotFoundException {
        while (true) {
            PayloadObject readObject = null;
            if ((readObject = communicator.receiveMessage()) != null) {
                return readObject;
            }
        }
    }

    /**
     * Validate placement action and generate action object.
     *
     * @param input    input
     * @param board    board
     * @param playerId playerId
     * @return action
     * @throws InvalidInputException InvalidInputException
     */
    private Action validateInputAndGeneratePlacementAction(String input, GameBoard board, Integer playerId)
            throws InvalidInputException {
        List<String> inputs = new ArrayList<>(Arrays.asList(input.split(",")));
        if (inputs.size() != 3) {
            throw new InvalidInputException("Invalid input size");
        }
        Action action;
        try {
            int territoryId = Integer.parseInt(inputs.get(0));
            String unitTypeString = inputs.get(1);
            int unitNum = Integer.parseInt(inputs.get(2));

            //check valid unit type mapping
            Map<String, UnitType> unitTypeMapper = board.getUnitTypeMapper();
            if (!unitTypeMapper.containsKey(unitTypeString)) {
                throw new InvalidInputException("Invalid unit type string " + unitTypeString);
            }
            UnitType unitType = unitTypeMapper.get(unitTypeString);

            Map<UnitType, Integer> unitMap = new HashMap<>();
            unitMap.put(unitType, unitNum);
            action = new PlacementAction(territoryId, unitMap, playerId);

        } catch (NumberFormatException e) {
            throw new InvalidInputException("Cannot parse string to valid int");
        }
        return action;
    }

    /**
     * input in the format "sourceTerritoryId,destinationId,UnitType,amount"
     *
     * @param input    input
     * @param board    board
     * @param playerId id of the player
     * @return result action
     * @throws InvalidInputException when input is invalid
     */
    private Action readUpgradeUnitAction(String input, GameBoard board, Integer playerId)
            throws InvalidInputException {
        List<String> inputs = new ArrayList<>(Arrays.asList(input.split(",")));
        if (inputs.size() != 3) {
            throw new InvalidInputException("Invalid input size");
        }
        Action action;
        try {
            int targetTerritoryId = Integer.parseInt(inputs.get(0));
            String unitTypeString = inputs.get(1);
            int unitNum = Integer.parseInt(inputs.get(2));

            //check valid unit type mapping
            Map<String, UnitType> unitTypeMapper = board.getUnitTypeMapper();
            if (!unitTypeMapper.containsKey(unitTypeString)) {
                throw new InvalidInputException("Invalid unit type string " + unitTypeString);
            }
            UnitType unitType = unitTypeMapper.get(unitTypeString);
            Map<UnitType, Integer> unitMap = new HashMap<>();
            unitMap.put(unitType, unitNum);
            action = new UpgradeUnitAction(playerId, targetTerritoryId, unitMap);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Cannot parse string to valid int");
        }
        return action;
    }

    /**
     * input in the format "sourceTerritoryId,destinationId,UnitType,amount"
     *
     * @param input    input
     * @param board    board
     * @param playerId id of the player
     * @return result action
     * @throws InvalidInputException when input is invalid
     */
    private Action readAttackOrMoveAction(String input, GameBoard board, Integer playerId, int actionType)
            throws InvalidInputException, NumberFormatException {
        List<String> actionInputs = new ArrayList<>(Arrays.asList(input.split(";")));
        if (actionInputs.size() < 2) {
            throw new InvalidInputException("Invalid input size");
        }

        //get source and destination information
        String territoryInput = actionInputs.get(0);
        List<String> territoryInputs = new ArrayList<>(Arrays.asList(territoryInput.split(",")));
        int sourceTerritoryId = Integer.parseInt(territoryInputs.get(0));
        int destTerritoryId = Integer.parseInt(territoryInputs.get(1));

        //get units and number information
        Map<UnitType, Integer> unitsMap = new HashMap<>();
        for (int i = 1; i < actionInputs.size(); i++) {
            String unitsInput = actionInputs.get(i);
            List<String> inputs = new ArrayList<>(Arrays.asList(unitsInput.split(",")));
            if (inputs.size() != 2) {
                throw new InvalidInputException("Invalid input size");
            }
            try {
                String unitTypeString = inputs.get(0);
                int unitNum = Integer.parseInt(inputs.get(1));
                //check valid unit type mapping
                Map<String, UnitType> unitTypeMapper = board.getUnitTypeMapper();
                if (!unitTypeMapper.containsKey(unitTypeString)) {
                    throw new InvalidInputException("Invalid unit type string " + unitTypeString);
                }
                UnitType unitType = unitTypeMapper.get(unitTypeString);
                unitsMap.put(unitType, unitNum);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Cannot parse string to valid int");
            }
        }

        Action action;
        if (actionType == 0) {
            action = new MoveAction(sourceTerritoryId, destTerritoryId, unitsMap, playerId);
        } else {
            action = new AttackAction(sourceTerritoryId, destTerritoryId, unitsMap, playerId);
        }
        return action;
    }

    /**
     * Check whether the user is win or lost
     *
     * @return whether the user is win or lost
     */
    boolean checkUserStatus() {
        return isLost() || isWin();
    }

    /**
     * The current user has won.
     *
     * @return whether the current user has won.
     */
    private boolean isWin() {
        Player player = this.gameBoard.findPlayer(playerId);
        return player.isWin();
    }

    /**
     * Print the winner information
     */
    private void printWinnerInfo() {
        Player winner = this.gameBoard.getWinner();
        if (winner != null) {
            if (this.playerId.equals(winner.getId())) {
                System.out.println("Cong! You are the winner");
            } else {
                System.out.println("Winner is " + winner.getColor() + " player");
            }
        }
    }

    /**
     * Whether you are lost
     *
     * @return Whether you are lost
     */
    private boolean isLost() {
        Player player = this.gameBoard.findPlayer(playerId);
        return player.isLost();
    }

    /**
     * The terminate process of the client JVM process
     */
    private void terminateProcess() {
        try {
            System.out.println("GAME OVER");
            this.communicator.terminate();
            //this.consoleReader.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
