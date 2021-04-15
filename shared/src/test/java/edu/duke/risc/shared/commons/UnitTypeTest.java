package edu.duke.risc.shared.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eason
 * @date 2021/4/15 11:36
 */
public class UnitTypeTest {

    @Test
    public void testGetUpgradeResources() {
        UnitType unitType = UnitType.SOLDIER;
        Assertions.assertEquals(15, unitType.getUpgradeResources(5));
    }

    @Test
    public void testGetTopLevel() {
        Assertions.assertEquals(UnitType.MASTER, UnitType.getTopLevel());
    }

    @Test
    public void testGetTechRequiredToUpgrade() {
        Assertions.assertEquals(1, UnitType.getTechRequiredToUpgrade(UnitType.SOLDIER));
        Assertions.assertEquals(2, UnitType.getTechRequiredToUpgrade(UnitType.INFANTRY));
        Assertions.assertEquals(3, UnitType.getTechRequiredToUpgrade(UnitType.CAVALRY));
        Assertions.assertEquals(4, UnitType.getTechRequiredToUpgrade(UnitType.KNIGHT));
        Assertions.assertEquals(5, UnitType.getTechRequiredToUpgrade(UnitType.ROOK));
        Assertions.assertEquals(6, UnitType.getTechRequiredToUpgrade(UnitType.QUEEN));
        Assertions.assertEquals(Integer.MAX_VALUE, UnitType.getTechRequiredToUpgrade(UnitType.MASTER));
        Assertions.assertEquals(Integer.MAX_VALUE, UnitType.getTechRequiredToUpgrade(UnitType.TEST));
    }

    @Test
    public void testGetNextLevelOfUnit() {
        Assertions.assertEquals(UnitType.INFANTRY, UnitType.getNextLevelOfUnit(UnitType.SOLDIER));
        Assertions.assertEquals(UnitType.CAVALRY, UnitType.getNextLevelOfUnit(UnitType.INFANTRY));
        Assertions.assertEquals(UnitType.KNIGHT, UnitType.getNextLevelOfUnit(UnitType.CAVALRY));
        Assertions.assertEquals(UnitType.ROOK, UnitType.getNextLevelOfUnit(UnitType.KNIGHT));
        Assertions.assertEquals(UnitType.QUEEN, UnitType.getNextLevelOfUnit(UnitType.ROOK));
        Assertions.assertEquals(UnitType.MASTER, UnitType.getNextLevelOfUnit(UnitType.QUEEN));
        Assertions.assertEquals(UnitType.MASTER, UnitType.getNextLevelOfUnit(UnitType.MASTER));
        Assertions.assertNull(UnitType.getNextLevelOfUnit(UnitType.TEST));
    }

    @Test
    public void testGetLowestLevelUnitType() {
        Map<UnitType, Integer> map = new HashMap<>();

        Assertions.assertNull(UnitType.getLowestLevelUnitType(map));

        map.put(UnitType.TEST, 1);
        Assertions.assertNull(UnitType.getLowestLevelUnitType(map));

        map.put(UnitType.MASTER, 1);
        Assertions.assertEquals(UnitType.MASTER, UnitType.getLowestLevelUnitType(map));

        map.put(UnitType.QUEEN, 1);
        Assertions.assertEquals(UnitType.QUEEN, UnitType.getLowestLevelUnitType(map));

        map.put(UnitType.ROOK, 1);
        Assertions.assertEquals(UnitType.ROOK, UnitType.getLowestLevelUnitType(map));

        map.put(UnitType.KNIGHT, 1);
        Assertions.assertEquals(UnitType.KNIGHT, UnitType.getLowestLevelUnitType(map));

        map.put(UnitType.CAVALRY, 1);
        Assertions.assertEquals(UnitType.CAVALRY, UnitType.getLowestLevelUnitType(map));

        map.put(UnitType.INFANTRY, 1);
        Assertions.assertEquals(UnitType.INFANTRY, UnitType.getLowestLevelUnitType(map));

        map.put(UnitType.SOLDIER, 1);
        Assertions.assertEquals(UnitType.SOLDIER, UnitType.getLowestLevelUnitType(map));

    }

    @Test
    public void testGetHighestLevelUnitType() {
        Map<UnitType, Integer> map = new HashMap<>();

        Assertions.assertNull(UnitType.getHighestLevelUnitType(map));

        map.put(UnitType.TEST, 1);
        Assertions.assertNull(UnitType.getHighestLevelUnitType(map));

        map.put(UnitType.SOLDIER, 1);
        Assertions.assertEquals(UnitType.SOLDIER, UnitType.getHighestLevelUnitType(map));

        map.put(UnitType.INFANTRY, 1);
        Assertions.assertEquals(UnitType.INFANTRY, UnitType.getHighestLevelUnitType(map));

        map.put(UnitType.CAVALRY, 1);
        Assertions.assertEquals(UnitType.CAVALRY, UnitType.getHighestLevelUnitType(map));

        map.put(UnitType.KNIGHT, 1);
        Assertions.assertEquals(UnitType.KNIGHT, UnitType.getHighestLevelUnitType(map));

        map.put(UnitType.ROOK, 1);
        Assertions.assertEquals(UnitType.ROOK, UnitType.getHighestLevelUnitType(map));

        map.put(UnitType.QUEEN, 1);
        Assertions.assertEquals(UnitType.QUEEN, UnitType.getHighestLevelUnitType(map));

        map.put(UnitType.MASTER, 1);
        Assertions.assertEquals(UnitType.MASTER, UnitType.getHighestLevelUnitType(map));

    }

}
