package edu.duke.risc.shared;

import java.io.IOException;

/**
 * @author eason
 * @date 2021/3/10 11:00
 */
public interface Communicable {

    /**
     * writeMessage
     * @param message
     * @return
     */
    public boolean writeMessage(PayloadObject message) throws IOException;

    /**
     * receiveMessage
     * @return
     * @throws IOException
     */
    public PayloadObject receiveMessage() throws IOException, ClassNotFoundException;

}
