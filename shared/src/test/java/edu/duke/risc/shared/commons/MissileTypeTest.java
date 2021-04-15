package edu.duke.risc.shared.commons;

import edu.duke.risc.shared.exceptions.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @author eason
 * @date 2021/4/15 11:28
 */
public class MissileTypeTest {

    @Test
    public void testGetMissileTypeWithTechLevel() throws InvalidInputException {
        Assertions.assertEquals(MissileType.MISSILE_LV1, MissileType.getMissileTypeWithTechLevel(1));
        Assertions.assertEquals(MissileType.MISSILE_LV2, MissileType.getMissileTypeWithTechLevel(2));
        Assertions.assertEquals(MissileType.MISSILE_LV3, MissileType.getMissileTypeWithTechLevel(3));
        Assertions.assertEquals(MissileType.MISSILE_LV4, MissileType.getMissileTypeWithTechLevel(4));
        Assertions.assertEquals(MissileType.MISSILE_LV5, MissileType.getMissileTypeWithTechLevel(5));
        Assertions.assertEquals(MissileType.MISSILE_LV6, MissileType.getMissileTypeWithTechLevel(6));
        Assertions.assertThrows(InvalidInputException.class, () -> MissileType.getMissileTypeWithTechLevel(7));
    }

    @Test
    public void testGetMissileKilledUnits() throws InvalidInputException {
        Set<UnitType> Lv1KilledType = MissileType.getMissileKilledUnits(MissileType.MISSILE_LV1);
        Set<UnitType> Lv2KilledType = MissileType.getMissileKilledUnits(MissileType.MISSILE_LV2);
        Set<UnitType> Lv3KilledType = MissileType.getMissileKilledUnits(MissileType.MISSILE_LV3);
        Set<UnitType> Lv4KilledType = MissileType.getMissileKilledUnits(MissileType.MISSILE_LV4);
        Set<UnitType> Lv5KilledType = MissileType.getMissileKilledUnits(MissileType.MISSILE_LV5);
        Set<UnitType> Lv6KilledType = MissileType.getMissileKilledUnits(MissileType.MISSILE_LV6);
        Assertions.assertThrows(InvalidInputException.class,
                () -> MissileType.getMissileKilledUnits(MissileType.MISSILE_TEST));

        Assertions.assertTrue(Lv1KilledType.contains(UnitType.SOLDIER));

        Assertions.assertTrue(Lv2KilledType.contains(UnitType.SOLDIER));
        Assertions.assertTrue(Lv2KilledType.contains(UnitType.INFANTRY));

        Assertions.assertTrue(Lv3KilledType.contains(UnitType.SOLDIER));
        Assertions.assertTrue(Lv3KilledType.contains(UnitType.INFANTRY));
        Assertions.assertTrue(Lv3KilledType.contains(UnitType.CAVALRY));

        Assertions.assertTrue(Lv4KilledType.contains(UnitType.SOLDIER));
        Assertions.assertTrue(Lv4KilledType.contains(UnitType.INFANTRY));
        Assertions.assertTrue(Lv4KilledType.contains(UnitType.CAVALRY));
        Assertions.assertTrue(Lv4KilledType.contains(UnitType.KNIGHT));

        Assertions.assertTrue(Lv5KilledType.contains(UnitType.SOLDIER));
        Assertions.assertTrue(Lv5KilledType.contains(UnitType.INFANTRY));
        Assertions.assertTrue(Lv5KilledType.contains(UnitType.CAVALRY));
        Assertions.assertTrue(Lv5KilledType.contains(UnitType.KNIGHT));
        Assertions.assertTrue(Lv5KilledType.contains(UnitType.ROOK));

        Assertions.assertTrue(Lv6KilledType.contains(UnitType.SOLDIER));
        Assertions.assertTrue(Lv6KilledType.contains(UnitType.INFANTRY));
        Assertions.assertTrue(Lv6KilledType.contains(UnitType.CAVALRY));
        Assertions.assertTrue(Lv6KilledType.contains(UnitType.KNIGHT));
        Assertions.assertTrue(Lv6KilledType.contains(UnitType.ROOK));
        Assertions.assertTrue(Lv6KilledType.contains(UnitType.QUEEN));
    }

}
