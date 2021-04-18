package edu.duke.risc.shared.board;

import edu.duke.risc.shared.commons.MissileType;
import edu.duke.risc.shared.commons.UnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TerritoryTest {

    @Test
    public void test_Territory() {
        //equals
        Territory T1 = new Territory(1, "T1");
        Territory T2 = new Territory(2, "T2");
        T1.addNeighbor(2);
        Assertions.assertEquals(T1, T1);
        Assertions.assertNotEquals(T1, null);
        Assertions.assertNotEquals(T2, T1);
        Assertions.assertEquals("T1(1)", T1.getBasicInfo());
        Assertions.assertTrue(T1.isAdjacentTo(2));
        T1.updateUnitsMap(UnitType.SOLDIER, 2);
        T1.updateVirtualUnitsMap(UnitType.SOLDIER, 2);

        Assertions.assertFalse(T1.containsSpies(2));
        Assertions.assertEquals(0, T1.getNumberOfSpies(2));
        Assertions.assertEquals(2, T1.getTotalNumberOfUnits());
        Assertions.assertEquals(2, T1.getUnitsNumber(UnitType.SOLDIER));

        T1.updateVirtualMissileMap(MissileType.MISSILE_LV1, 2);

        //cloaking test
        T1.reduceCloaks();
        Assertions.assertFalse(T1.hasCloaks());
        T1.enableCloaking();
        T1.reduceCloaks();
        T1.updateSpiesMap(1, 2);
        Assertions.assertTrue(T1.hasCloaks());
        T1.resetCloak();
        Assertions.assertFalse(T1.hasCloaks());

        //sets
        Map<UnitType, Integer> myMap = new HashMap<UnitType, Integer>();
        myMap.put(UnitType.SOLDIER, 1);
        T1.getVirtualUnitsMap();

    }

}
