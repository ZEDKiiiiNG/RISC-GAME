package edu.duke.risc.shared;

import edu.duke.risc.shared.board.WorldMapTerritoryFactory;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.board.TerritoryFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author eason
 * @date 2021/3/11 11:49
 */
public class TestTerritoryFactory {

    @Test
    public void testFactory() {
        TerritoryFactory factory = new WorldMapTerritoryFactory();
        Map<Integer, Territory> territories = factory.makeTerritories(3);
        System.out.println();
    }

}
