package edu.duke.risc.shared;



import java.io.IOException;

/**
 * @author eason
 * @date 2021/3/10 11:00
 */
public interface Communicable {

    /**
     * Write messages into the socket channel
     * @param message message
     * @return whether this action is successful or not
     * @throws IOException IOException
     */
    public boolean writeMessage(PayloadObject message) throws IOException;

    /**
     * Receive message from socket channel
     *
     * @return PayloadObject
     * @throws IOException IOException
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public PayloadObject receiveMessage() throws IOException, ClassNotFoundException;


    /**
     * Terminate socket connection
     * @throws IOException IOException
     */
    public void terminate() throws IOException;

    public void sendUrgentData() throws  IOException;
}
