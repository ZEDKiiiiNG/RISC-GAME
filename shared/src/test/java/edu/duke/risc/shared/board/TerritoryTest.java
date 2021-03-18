package edu.duke.risc.shared.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import edu.duke.risc.shared.commons.UnitType;
import java.util.Map;
import java.util.HashMap;

public class TerritoryTest {
  @Test
  public void test_Territory() {
    //equals
    Territory T1 = new Territory(1, "T1");
    Territory T2 = new Territory(1, "T2");
    assertEquals(true, T1.equals(T1));
    assertEquals(false, T1.equals(null));
    assertEquals(true, T1.equals(T2));
    //sets
    T1.setTerritoryId(1);
    T1.setTerritoryName("changedT1");
    Map<UnitType, Integer> myMap = new HashMap<UnitType, Integer>();
    myMap.put(UnitType.SOLDIER, 1);
    T1.setUnitsMap(myMap);
    //virtual
    T1.setVirtualUnitsMap(myMap);
    T1.getVirtualUnitsMap();
  }

}
