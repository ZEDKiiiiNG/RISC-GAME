package edu.duke.risc.shared.users;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    public void playerTest() {
        Player player = new Player(0, UserColor.BLUE);
        Player playerb = new Player(0, UserColor.BLUE);
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

    }
}
