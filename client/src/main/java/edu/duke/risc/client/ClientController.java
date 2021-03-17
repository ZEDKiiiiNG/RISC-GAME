package edu.duke.risc.client;

import edu.duke.risc.shared.Communicable;
import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.SocketCommunicator;
import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.actions.AttackAction;
import edu.duke.risc.shared.actions.MoveAction;
import edu.duke.risc.shared.actions.PlacementAction;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.PayloadType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.exceptions.InvalidInputException;
import edu.duke.risc.shared.exceptions.InvalidPayloadContent;
import edu.duke.risc.shared.exceptions.ServerRejectException;
import edu.duke.risc.shared.exceptions.UnmatchedReceiverException;
import edu.duke.risc.shared.users.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.duke.risc.shared.Configurations.*;

/**
 * Represents the client-side codes
 *
 * @author eason
 * @date 2021/3/10 13:58
 */
public class ClientController {

    /**
     * The game board
     */
    private GameBoard gameBoard;

    /**
     * Socket communicator
     */
    private Communicable communicator;

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

    /**
     * Thread which reads exit signal
     */
    private ReadExitThread readExitThread;

    /**
     * Constructor
     *
     * @throws IOException IOException
     */
    public ClientController() throws IOException {
        this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Start the game, doing the game logics
     *
     * @throws IOException IOException
     */
    public void startGame() throws IOException {
        tryConnectAndWait();
        assignUnits();
        moveAndAttack();
        observerMode();
    }

    /**
     * Try to connect with the server and wait for other users
     */
    private void tryConnectAndWait() {
        try {
            //try connect to the server
            Socket socket = new Socket(ClientConfigurations.LOCALHOST, Configurations.DEFAULT_SERVER_PORT);
            communicator = new SocketCommunicator(socket);
            System.out.println(ClientConfigurations.CONNECT_SUCCESS_MSG);
            //waiting for other users
            this.waitAndReadServerResponse();
            System.out.println("You are current the player: " + this.gameBoard.getPlayers().get(playerId));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnmatchedReceiverException | InvalidPayloadContent | ServerRejectException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Assign units and doing the placement
     *
     * @throws IOException IOException
     */
    private void assignUnits() throws IOException {
        while (true) {
            List<Action> actions = new ArrayList<>();
            Player player = this.gameBoard.getPlayers().get(playerId);

            while (!player.getInitUnitsMap().isEmpty()) {
                this.gameBoard.displayBoard();
                //print basic information
                System.out.println("You are the " + player.getColor() + " player: ");

                //asking target territory
                System.out.println("You are assigned " + gameBoard.getPlayerAssignedTerritoryInfo(playerId));
                System.out.println("You still have " + player.getUnitsInfo(player.getInitUnitsMap()) + " available");
                System.out.println("Please enter your placement will in the format of " +
                        "<target territory>,<unit type>,<unit number> for example 1,S,5");

                String input = this.consoleReader.readLine();
                Action action = null;
                try {
                    action = this.validateInputAndGenerateAction(input, this.gameBoard, playerId);
                    action.simulateApply(this.gameBoard);
                    actions.add(action);
                } catch (InvalidInputException | InvalidActionException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }

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
                System.out.println(this.loggerInfo);
                this.gameBoard.displayBoard();
            } catch (InvalidPayloadContent | ServerRejectException | UnmatchedReceiverException exception) {
                //if server returns failed, re-do the actions again
                exception.printStackTrace();
                continue;
            }
            System.out.println("Successfully finished placement phase");
            break;
        }
    }

    /**
     * Send message to the server
     *
     * @param payloadObject payloadObject
     * @throws IOException IOException
     */
    private void sendMessage(PayloadObject payloadObject) throws IOException {
        this.communicator.writeMessage(payloadObject);
    }

    /**
     * Listening to the move and attack actions and sends request to the server
     *
     * @throws IOException IOException
     */
    private void moveAndAttack() throws IOException {
        while (true) {
            if (this.checkUserStatus()) {
                return;
            }
            Player player = this.gameBoard.getPlayers().get(playerId);
            boolean isFinished = false;
            List<Action> moveActions = new ArrayList<>();
            List<Action> attackActions = new ArrayList<>();
            while (!isFinished) {
                this.gameBoard.displayBoard();
                System.out.println(this.gameBoard.getPlayerInfo(this.playerId));
                System.out.println("You are the " + player.getColor() + " player, what would you like to do?");
                System.out.println("(M)ove");
                System.out.println("(A)ttack");
                System.out.println("(D)one");
                String input = this.consoleReader.readLine();
                switch (input) {
                    case "M":
                        conductMoveOrAttack(moveActions, 0);
                        break;
                    case "A":
                        conductMoveOrAttack(attackActions, 1);
                        break;
                    case "D":
                        System.out.println("You have finished your actions, submitting...");
                        isFinished = true;
                        break;
                    default:
                        System.out.println("Invalid input, please input again");
                        break;
                }
            }

            //todo combine all attack actions
            //sending to the server
            //constructing payload objects
            Map<String, Object> content = new HashMap<>(3);
            content.put(Configurations.REQUEST_MOVE_ACTIONS, moveActions);

            content.put(Configurations.REQUEST_ATTACK_ACTIONS, attackActions);
            PayloadObject request = new PayloadObject(this.playerId,
                    Configurations.MASTER_ID, PayloadType.REQUEST, content);
            try {
                this.sendMessage(request);
                System.out.println("Actions sent, please wait other players finish placing");
                this.waitAndReadServerResponse();
                System.out.println(this.loggerInfo);
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
    private void observerMode() {
        try {
            System.out.println("You lost the game, entering Observer Mode, you can type exit to quit...");
            this.readExitThread = new ReadExitThread(this.consoleReader, this.communicator, this.playerId);
            this.readExitThread.start();
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
    private void conductMoveOrAttack(List<Action> actions, int actionType) throws IOException {
        System.out.println("Please enter instruction in the following format: " +
                "<sourceTerritoryId>,<destinationId>,<UnitType>,<amount>");
        String moveInput = this.consoleReader.readLine();
        Action action;
        try {
            action = this.readActionAndProceed(moveInput, this.gameBoard, playerId, actionType);
            action.simulateApply(this.gameBoard);
            actions.add(action);
        } catch (InvalidInputException | InvalidActionException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * waitAndReadServerResponse
     *
     * @throws UnmatchedReceiverException UnmatchedReceiverException
     * @throws InvalidPayloadContent      InvalidPayloadContent
     * @throws ServerRejectException      ServerRejectException
     */
    private void waitAndReadServerResponse() throws UnmatchedReceiverException, InvalidPayloadContent, ServerRejectException {
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
    private Action validateInputAndGenerateAction(String input, GameBoard board, Integer playerId)
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

            action = new PlacementAction(territoryId, unitType, unitNum, playerId);

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
    private Action readActionAndProceed(String input, GameBoard board, Integer playerId, int actionType)
            throws InvalidInputException {
        List<String> inputs = new ArrayList<>(Arrays.asList(input.split(",")));
        if (inputs.size() != 4) {
            throw new InvalidInputException("Invalid input size");
        }
        Action action;
        try {
            int sourceTerritoryId = Integer.parseInt(inputs.get(0));
            int destTerritoryId = Integer.parseInt(inputs.get(1));
            String unitTypeString = inputs.get(2);
            int unitNum = Integer.parseInt(inputs.get(3));

            //check valid unit type mapping
            Map<String, UnitType> unitTypeMapper = board.getUnitTypeMapper();
            if (!unitTypeMapper.containsKey(unitTypeString)) {
                throw new InvalidInputException("Invalid unit type string " + unitTypeString);
            }
            UnitType unitType = unitTypeMapper.get(unitTypeString);
            if (actionType == 0) {
                action = new MoveAction(sourceTerritoryId, destTerritoryId, unitType, unitNum, playerId);
            } else {
                action = new AttackAction(sourceTerritoryId, destTerritoryId, unitType, unitNum, playerId);
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Cannot parse string to valid int");
        }
        return action;
    }

    /**
     * Check whether the user is win or lost
     *
     * @return whether the user is win or lost
     */
    private boolean checkUserStatus() {
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
            this.consoleReader.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
