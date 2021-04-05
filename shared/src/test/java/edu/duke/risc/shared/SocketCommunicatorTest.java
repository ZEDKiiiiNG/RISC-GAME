package edu.duke.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.commons.PayloadType;


public class SocketCommunicatorTest {
  @Test
  public void test_SocketCommu() {
    Socket mySocket = new Socket();
//    SocketCommunicator sc = new SocketCommunicator(mySocket);
    //PayloadObject PL = new PayloadObject(1, 0, PayloadType.UPDATE);
    //try{sc.writeMessage(PL);}
    //catch(IOException e){}
  }

}
