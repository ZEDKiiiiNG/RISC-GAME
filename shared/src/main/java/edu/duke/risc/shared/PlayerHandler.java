package edu.duke.risc.shared;

import java.io.IOException;

/**
 * @author eason
 * @date 2021/3/10 13:32
 */
public class PlayerHandler extends Thread {

    private Communicable communicator;

    private ThreadBarrier barrier;

    public PlayerHandler(Communicable communicator, ThreadBarrier barrier) {
        this.communicator = communicator;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            PayloadObject readObject = null;
            communicator.writeMessage(new PayloadObject());
            while ((readObject = communicator.receiveMessage()) != null) {
                System.out.println("Message received: " + readObject);
                barrier.objectReceived(readObject);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
