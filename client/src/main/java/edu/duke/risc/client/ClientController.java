package edu.duke.risc.client;

import edu.duke.risc.shared.Communicable;
import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.SocketCommunicator;
import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.actions.PlacementAction;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.GameStage;
import edu.duke.risc.shared.board.Territory;
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
import java.util.Set;

import static edu.duke.risc.shared.Configurations.GAME_BOARD_STRING;
import static edu.duke.risc.shared.Configurations.PLAYER_STRING;

/**
 * @author eason
 * @date 2021/3/10 13:58
 */
public class ClientController {

    private GameBoard gameBoard;

    private Communicable communicator;

    private BufferedReader consoleReader;

    private Integer playerId = Configurations.DEFAULT_PLAYER_ID;

    public ClientController() throws IOException {
        this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
        this.startGame();
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startGame() throws IOException {
        tryConnectAndWait();
        assignUnits();
        this.gameBoard.displayBoard();
    }

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

    private void assignUnits() throws IOException {
        while (true) {
            assert this.gameBoard.getGameStage() == GameStage.PLACEMENT;

            List<Action> actions = new ArrayList<>();
            Player player = this.gameBoard.getPlayers().get(playerId);

            while (!player.getInitUnitsMap().isEmpty()) {
                this.gameBoard.displayBoard();
                //print basic information
                System.out.println("You are the " + player.getColor() + " player: ");

                //asking target territory
                System.out.println("You are assigned " + gameBoard.getPlayerOwnedTerritoryInfo(playerId));
                System.out.println("You still have " + player.getUnitsInfo(player.getInitUnitsMap()) + " available");
                System.out.println("Please enter your placement will in the format of " +
                        "<target territory>,<unit type>,<unit number> for example 1,S,5");

                String input = this.consoleReader.readLine();
                Action action = null;
                try {
                    action = this.validateInputAndGenerateAction(input, this.gameBoard, playerId);
                    action.apply(this.gameBoard);
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
                this.waitAndReadServerResponse();
            } catch (InvalidPayloadContent | ServerRejectException | UnmatchedReceiverException exception) {
                //if server returns failed, re-do the actions again
                exception.printStackTrace();
                continue;
            }
            System.out.println("Successfully finished placement phase");
            break;
        }
    }

    private void sendMessage(PayloadObject payloadObject) throws IOException {
        this.communicator.writeMessage(payloadObject);
    }


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
            case UPDATE:
                if (contents.containsKey(GAME_BOARD_STRING)
                        && contents.containsKey(PLAYER_STRING)) {
                    this.gameBoard = (GameBoard) contents.get(GAME_BOARD_STRING);
                    this.playerId = (Integer) contents.get(PLAYER_STRING);
                } else {
                    throw new InvalidPayloadContent("do not contain gameBoard object");
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid message type");
        }
    }

    private PayloadObject waitAndRead() throws IOException, ClassNotFoundException {
        while (true) {
            PayloadObject readObject = null;
            if ((readObject = communicator.receiveMessage()) != null) {
                return readObject;
            }
        }
    }

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

            //check valid territory id
            Player player = board.findPlayer(playerId);
            Set<Integer> ownedTerritories = player.getOwnedTerritories();
            if (!ownedTerritories.contains(territoryId)){
                throw new InvalidInputException("You do not own territory with id = " + territoryId);
            }

            //check valid unit type mapping
            Map<String, UnitType> unitTypeMapper = gameBoard.getUnitTypeMapper();
            if (!unitTypeMapper.containsKey(unitTypeString)){
                throw new InvalidInputException("Invalid unit type string " + unitTypeString);
            }
            UnitType unitType = unitTypeMapper.get(unitTypeString);

            action = new PlacementAction(territoryId, unitType, unitNum, playerId);

        } catch (NumberFormatException e) {
            throw new InvalidInputException("Cannot parse string to valid int");
        }
        return action;
    }

}
