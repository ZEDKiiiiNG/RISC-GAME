package edu.duke.risc.shared.users;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.commons.MissileType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    public void playerTest() {
        Player player = new Player(0, UserColor.BLUE);
        Player playerb = new Player(0, UserColor.BLUE);

        playerb.upgradeTechLevel(false);
        playerb.getPlayerInfo();

        player.updateSpiesMap(1, 1 );

        Assertions.assertEquals(Configurations.DEFAULT_FOOD_RESOURCE, player.getResources(ResourceType.FOOD));

        Assertions.assertNotNull(player.getSpiesMap());
        Assertions.assertNotNull(player.getMissiles());
        Assertions.assertEquals(1, player.getTechnology());
        Assertions.assertNull(player.hasEnoughResourcesForTechUpgrade());

        player.doResearchCloaking();
        player.upgradeTechLevel(true);
        Assertions.assertFalse(player.isAlreadyUpgradeTechInTurn());
        Assertions.assertNotNull(player.hasEnoughResourcesForTechUpgrade());

        //missiles
        player.obtainMissile(MissileType.MISSILE_LV1, 3);
        Assertions.assertTrue(player.hasEnoughMissiles(MissileType.MISSILE_LV1, 3));
        Assertions.assertFalse(player.hasEnoughMissiles(MissileType.MISSILE_LV2, 3));
        player.useMissiles(MissileType.MISSILE_LV1, 1);

        Assertions.assertTrue(player.isCloakingResearched());
        Assertions.assertFalse(player.isAtTopLevel());
        assertFalse(player.isMaster());
        assertEquals(player.getUnitsInfo(player.getInitUnitsMap()), "(S)oldiers(I) : 10");
        assertEquals(player.getId(), 0);
        assertEquals(player.getColor(), UserColor.BLUE);
        assertEquals(playerb, player);
        assertEquals(player, player);
        assertNotEquals(player, null);
        assertNotNull(player.toString());
        assertEquals(player.getUserId(), 0);
        player.updateTotalUnitMap(UnitType.SOLDIER, 10);
        player.updateTotalUnitMap(UnitType.SOLDIER, 10);
        player.updateTotalUnitMap(UnitType.SOLDIER, -5);
        Map<UnitType, Integer> unitmap = new HashMap<UnitType, Integer>();
        unitmap.put(UnitType.SOLDIER, 15);


        assertEquals(player.getTotalUnitsMap(), unitmap);
        player.updateTotalUnitMap(UnitType.SOLDIER, -10);
        player.updateInitUnitMap(UnitType.SOLDIER, -10);
        player.updateInitUnitMap(UnitType.SOLDIER, 0);

        assertEquals(player.hashCode(), Objects.hash(0));
        assertEquals(player.getId(), Configurations.MASTER_ID);
        assertFalse(player.ownsTerritory(1));

        player.addOwnedTerritory(1);
        player.addOwnedTerritory(2);
        player.removeOwnedTerritory(2);
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        playerb.setOwnedTerritories(set);

        assertEquals(player.getOwnedTerritories(), playerb.getOwnedTerritories());
        assertNotNull(player.getOwnedTerritories());
        playerb.markLost();
        assertTrue(playerb.isLost());
        player.markWin();
        assertTrue(player.isWin());

        player.useResources(ResourceType.TECH, 0);
        player.hasEnoughResources(ResourceType.TECH, 10000);
        player.hasEnoughTechLevel(1);
        player.hasEnoughTechLevel(6);
    }
}
