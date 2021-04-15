package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author eason
 * @date 2021/4/15 16:43
 */
public class UpgradeUnitActionTest {

    @Test
    public void upgradeUnitActionTest() throws InvalidActionException {
        GameBoard gameBoard = new GameBoard(3);
        Player player1 = new Player(1, UserColor.BLUE);
        Player player2 = new Player(2, UserColor.GREEN);
        Player player3 = new Player(3, UserColor.RED);
        assertEquals(UserColor.BLUE.toString(), "Blue");
        Set<Integer> terris1 = gameBoard.addPlayer(player1);
        Set<Integer> terris2 = gameBoard.addPlayer(player2);
        Set<Integer> terris3 = gameBoard.addPlayer(player3);
        player1.setOwnedTerritories(terris1);
        player2.setOwnedTerritories(terris2);
        player3.setOwnedTerritories(terris3);

        //invalid unit map: unit value < 0
        Map<UnitType, Integer> invalidUnitMap = new HashMap<>();
        invalidUnitMap.put(UnitType.SOLDIER, -1);
        AbstractAction invalidAction = new UpgradeUnitAction(0, 1, invalidUnitMap);
        Assertions.assertEquals("Invalid number -1 for unit type (S)oldiers(I)", invalidAction.isValid(gameBoard));

        //invalid input: invalid player id
        Map<UnitType, Integer> unitMap2 = new HashMap<>();
        unitMap2.put(UnitType.SOLDIER, 1);
        AbstractAction invalidPlayerIdAction = new UpgradeUnitAction(10, 1, unitMap2);
        Assertions.assertEquals("Does not contain user: 10", invalidPlayerIdAction.isValid(gameBoard));
        Assertions.assertThrows(InvalidActionException.class, () -> invalidPlayerIdAction.apply(gameBoard));

        //invalid action: does not own source territory
        Map<UnitType, Integer> notOwnTerritoryUnitMap = new HashMap<>();
        notOwnTerritoryUnitMap.put(UnitType.SOLDIER, 1);
        AbstractAction notOwnTerritoryUnitAction = new UpgradeUnitAction(1, 4, notOwnTerritoryUnitMap);
        Assertions.assertEquals("The player does not own the target territory Colorado(4)",
                notOwnTerritoryUnitAction.isValid(gameBoard));

        //invalid action: not enough unit type
        Map<UnitType, Integer> noUnitUnitMap = new HashMap<>();
        noUnitUnitMap.put(UnitType.SOLDIER, 10);
        Action noUnitUnitMapAction = new UpgradeUnitAction(2, 3, noUnitUnitMap);
        Assertions.assertEquals("The target territory does not contain the unit type (S)oldiers(I)",
                noUnitUnitMapAction.isValid(gameBoard));

        //invalid action: not enough unit number
        Territory t3 = gameBoard.findTerritory(3);
        t3.updateUnitsMap(UnitType.SOLDIER, 3);
        Action notEnoughUnitAction = new UpgradeUnitAction(2, 3, noUnitUnitMap);
        Assertions.assertEquals("The target territory does not have enough unit number: 3 < 10",
                notEnoughUnitAction.isValid(gameBoard));

        //invalid action: not enough tech resource
        noUnitUnitMap.put(UnitType.SOLDIER, 10);
        t3.updateUnitsMap(UnitType.SOLDIER, 10);
        player2.updateResourceMap(ResourceType.TECH, -40);
        Action notEnoughResourceAction = new UpgradeUnitAction(2, 3, noUnitUnitMap);
        Assertions.assertEquals("The player does not have enough tech resources: 10 < 30",
                notEnoughResourceAction.isValid(gameBoard));

        //invalid action: cannot upgrade more
        t3.updateUnitsMap(UnitType.MASTER, 1);
        player2.updateResourceMap(ResourceType.TECH, 400);

        Map<UnitType, Integer> topUnitMap = new HashMap<>();
        topUnitMap.put(UnitType.MASTER, 1);
        Action atTopLevelAction = new UpgradeUnitAction(2, 3, topUnitMap);
        Assertions.assertEquals("Already at (M)aster(VII) cannot upgrade more.",
                atTopLevelAction.isValid(gameBoard));

        //not enough tech level
        Map<UnitType, Integer> highUnitMap = new HashMap<>();
        highUnitMap.put(UnitType.CAVALRY, 1);
        gameBoard.findTerritory(5).updateUnitsMap(UnitType.CAVALRY, 1);
        Action highLevelAction = new UpgradeUnitAction(3, 5, highUnitMap);
        Assertions.assertEquals("Not enough tech level: 1 < 3",
                highLevelAction.isValid(gameBoard));

        //completely valid action
        Territory territory1 = gameBoard.findTerritory(1);
        territory1.updateUnitsMap(UnitType.SOLDIER, 1);

        Map<UnitType, Integer> validUnitMap = new HashMap<>();
        validUnitMap.put(UnitType.SOLDIER, 1);
        Action validAction = new UpgradeUnitAction(1, 1, validUnitMap);
        Assertions.assertNotNull(validAction.simulateApply(gameBoard));

    }
}
