package edu.duke.risc.client;

import edu.duke.risc.shared.Communicable;
import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.SocketCommunicator;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.GameStage;
import edu.duke.risc.shared.users.Player;

import java.io.IOException;
import java.net.Socket;

/**
 * @author eason
 * @date 2021/3/10 13:58
 */
public class ClientController {

    private GameBoard gameBoard;

    private GameStage gameStage;

    private Communicable communicator;

    private Player player;

    public ClientController() throws IOException, ClassNotFoundException {
        this.startGame();
    }

    private void startGame() throws IOException, ClassNotFoundException {
        tryConnect();
        System.out.println(communicator.receiveMessage());
        communicator.writeMessage(new PayloadObject());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tryConnect() {
        try {
            Socket socket = new Socket("localhost", Configurations.DEFAULT_SERVER_PORT);
            communicator = new SocketCommunicator(socket);
            System.out.println("Successfully connect to the server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) throws IOException {
        communicator.writeMessage(new PayloadObject());
    }

}
