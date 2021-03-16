package edu.duke.risc.shared;

import java.io.IOException;

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

    /**
     * Constructor
     * @param communicator communicator
     * @param barrier barrier
     */
    public PlayerHandler(Communicable communicator, ThreadBarrier barrier) {
        this.communicator = communicator;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            while (true) {
                PayloadObject readObject = null;
                if ((readObject = communicator.receiveMessage()) != null){
                    System.out.println("Message received: " + readObject);
                    barrier.objectReceived(readObject);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            //when socket disconnects
            System.out.println(e.getMessage());
        }
    }

}
