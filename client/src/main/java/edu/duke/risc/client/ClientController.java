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
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.exceptions.InvalidPayloadContent;
import edu.duke.risc.shared.exceptions.UnmatchedReceiverException;
import edu.duke.risc.shared.users.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    }

    private void tryConnectAndWait() {
        try {
            //try connect to the server
            Socket socket = new Socket(ClientConfigurations.LOCALHOST, Configurations.DEFAULT_SERVER_PORT);
            communicator = new SocketCommunicator(socket);
            System.out.println(ClientConfigurations.CONNECT_SUCCESS_MSG);
            //waiting for other users
            PayloadObject response = waitAndRead();
            this.unpackAndUpdate(response);
            System.out.println("You are current the player: " + this.gameBoard.getPlayers().get(playerId));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnmatchedReceiverException | InvalidPayloadContent e) {
            System.out.println(e.getMessage());
        }
    }

    private void assignUnits() throws IOException {
        List<Action> actions = new ArrayList<>();
        this.gameBoard.displayBoard();
        assert this.gameBoard.getGameStage() == GameStage.PLACEMENT;
        Player player = this.gameBoard.getPlayers().get(playerId);
        System.out.println("You are the " + player.getColor() + " player: ");
        System.out.println("Placement phase: where would you like to place your units ?");
        for (Integer territoryId : player.getOwnedTerritories()) {
            Territory territory = gameBoard.getTerritories().get(territoryId);
            for (Map.Entry<UnitType, Integer> unitMap : player.getInitUnitsMap().entrySet()) {
                while (true) {
                    System.out.println("You have " + player.getUnitsInfo());
                    System.out.println("How many " + unitMap.getKey() + " would you like to put on "
                            + territory.getTerritoryName() + " ? ");
                    String input = this.consoleReader.readLine();
                    int number;
                    try {
                        number = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input, should only be numbers");
                        continue;
                    }
                    Action action = new PlacementAction(territoryId, unitMap.getKey(), number, player);
                    try {
                        action.apply(this.gameBoard);
                    } catch (InvalidActionException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    actions.add(action);
                    break;
                }
            }
        }
        //sending to the server

    }

    private PayloadObject waitAndRead() throws IOException, ClassNotFoundException {
        while (true) {
            PayloadObject readObject = null;
            if ((readObject = communicator.receiveMessage()) != null) {
                return readObject;
            }
        }
    }

    private void unpackAndUpdate(PayloadObject response) throws UnmatchedReceiverException, InvalidPayloadContent {
        //check desired receiver
        if (playerId == Configurations.DEFAULT_PLAYER_ID) {
            playerId = response.getReceiver();
        } else if (response.getReceiver().equals(playerId)) {
            throw new UnmatchedReceiverException("the " + playerId + "is not matched with " + response.getReceiver());
        }
        //unpack message
        Map<String, Object> contents = response.getContents();
        switch (response.getMessageType()) {
            case INFO:
                break;
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

}
