package edu.duke.risc.shared.users;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MasterTest {
    @Test
    public void masterTest(){
        Master master = new Master();
        assertTrue(master.isMaster());
        assertEquals(master.getId(), Configurations.MASTER_ID);
    }




}