package edu.duke.risc.client;

import edu.duke.risc.shared.Communicable;
import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.SocketCommunicator;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.exceptions.InvalidPayloadContent;
import edu.duke.risc.shared.exceptions.UnmatchedReceiverException;
import edu.duke.risc.shared.users.GameUser;
import edu.duke.risc.shared.users.Player;

import java.io.IOException;
import java.net.Socket;
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

    private GameUser player;

    public ClientController() {
        this.startGame();
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startGame() {
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
            System.out.println("You are current the player: " + this.player);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnmatchedReceiverException | InvalidPayloadContent e) {
            System.out.println(e.getMessage());
        }
    }

    private void assignUnits() {
        this.gameBoard.displayBoard();
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
        if (player == null) {
            player = response.getReceiver();
        } else if (response.getReceiver().equals(player)) {
            throw new UnmatchedReceiverException("the " + player + "is not matched with " + response.getReceiver());
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
                    this.player = (Player) contents.get(PLAYER_STRING);
                } else {
                    throw new InvalidPayloadContent("do not contain gameBoard object");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid message type");
        }
    }

}
