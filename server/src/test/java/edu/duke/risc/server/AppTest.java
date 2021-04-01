/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.risc.server;

import edu.duke.risc.client.App;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.board.TerritoryFactory;
import edu.duke.risc.shared.board.WorldMapTerritoryFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppTest {
    @Test
    void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }

    @Test
    public void testFactory() {
        TerritoryFactory factory = new WorldMapTerritoryFactory();
        Map<Integer, Territory> territories = factory.makeTerritories(3);
        System.out.println();
    }

}
