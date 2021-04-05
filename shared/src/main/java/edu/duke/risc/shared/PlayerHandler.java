package edu.duke.risc.shared;

import edu.duke.risc.shared.users.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Used as a separate thread to listen the client input.
 *
 * @author eason
 * @date 2021/3/10 13:32
 */
public class PlayerHandler extends Thread {

    /**
     * Socket communicator
     */
    private Communicable communicator;

    /**
     * ThreadBarrier
     */
    private ThreadBarrier barrier;


//    private final Map<Player, SocketCommunicator> playerConnection;
    /**
     * Constructor
     * @param communicator communicator
     * @param barrier barrier
     */
    public PlayerHandler(Communicable communicator, ThreadBarrier barrier/*,ServerSocket serverSocket, Map<Player, SocketCommunicator> playerConnections, Player player*/) {
        this.communicator = communicator;
        this.barrier = barrier;
//        this.serverSocket = serverSocket;
//        this.playerConnections = playerConnections;
//        this.player = player;
    }

    @Override
    public void run() {
        try {
            while (true) {
//                try {
//                    communicator.sendUrgentData();
//                }catch(IOException ex) {
//                    communicator.terminate();
//                    Socket clientSocket = serverSocket.accept();
//                    communicator = new SocketCommunicator(clientSocket);
//                    playerConnections.put(player, (SocketCommunicator) communicator);
//                }
                PayloadObject readObject = null;
                if ((readObject = communicator.receiveMessage()) != null){
                    System.out.println("Message received: " + readObject);
                    barrier.objectReceived(readObject);
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            //when socket disconnects
            System.out.println(e.getMessage());
        }
    }

}
