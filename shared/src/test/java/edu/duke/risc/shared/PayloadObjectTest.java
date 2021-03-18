package edu.duke.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.commons.PayloadType;





public class PayloadObjectTest {
  @Test
  public void test_payload() {
    PayloadObject PL = new PayloadObject(1, 0, PayloadType.UPDATE);
    PL.toString();
    //gets
    PL.getSender();
    PL.getReceiver();
    PL.getMessageType();
    PL.getContents();
  }

}
