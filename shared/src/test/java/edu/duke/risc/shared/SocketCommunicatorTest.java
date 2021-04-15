package edu.duke.risc.shared;

import edu.duke.risc.shared.commons.PayloadType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;


public class SocketCommunicatorTest {
    @Test
    public void test_SocketCommu() throws IOException, ClassNotFoundException {
        Socket mySocket = new Socket();
//        SocketCommunicator socketCommunicator = new SocketCommunicator(mySocket);
//        Assertions.assertNotNull(socketCommunicator.getSocket());
//        PayloadObject payloadObject = new PayloadObject(1, 0, PayloadType.UPDATE);
//        new Thread(() -> {
//            try {
//                socketCommunicator.writeMessage(payloadObject);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//        Assertions.assertNotNull(socketCommunicator.receiveMessage());
//        socketCommunicator.terminate();
    }

}
