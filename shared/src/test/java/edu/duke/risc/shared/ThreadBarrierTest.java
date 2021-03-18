package edu.duke.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.commons.PayloadType;

public class ThreadBarrierTest {
  @Test
  public void test_ThreadBarrier() {
    ThreadBarrier TB = new ThreadBarrier(3);//3 players
    PayloadObject PLsend = new PayloadObject(1,0,PayloadType.UPDATE);
    TB.objectReceived(PLsend);
    PayloadObject PLrecv = TB.consumeRequest();
  }

}
