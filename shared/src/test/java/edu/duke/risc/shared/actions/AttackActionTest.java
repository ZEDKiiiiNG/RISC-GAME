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

public class AttackActionTest {

    @Test
    public void attackActionTest() throws InvalidActionException {
        //initialize
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

        Territory territory0 = gameBoard.findTerritory(0);
        Territory territory2 = gameBoard.findTerritory(2);
        territory0.updateUnitsMap(UnitType.SOLDIER, 50);
        territory2.updateUnitsMap(UnitType.SOLDIER, 50);

        //invalid unit map: unit value < 0
        Map<UnitType, Integer> invalidUnitMap = new HashMap<>();
        invalidUnitMap.put(UnitType.SOLDIER, -1);
        AbstractAction invalidAction = new AttackAction(0, 1, invalidUnitMap, 1);
        Assertions.assertEquals("Invalid number -1 for unit type (S)oldiers(I)", invalidAction.isValid(gameBoard));
        invalidAction.toString();

        //invalid input: invalid player id
        Map<UnitType, Integer> unitMap2 = new HashMap<>();
        unitMap2.put(UnitType.SOLDIER, 1);
        AbstractAction invalidPlayerIdAction = new AttackAction(0, 1, unitMap2, 10);
        Assertions.assertEquals("Does not contain user: 10", invalidPlayerIdAction.isValid(gameBoard));

        //invalid action: does not own source territory
        Map<UnitType, Integer> notOwnTerritoryUnitMap = new HashMap<>();
        notOwnTerritoryUnitMap.put(UnitType.SOLDIER, 1);
        AbstractAction notOwnTerritoryUnitAction = new AttackAction(4, 1, notOwnTerritoryUnitMap, 1);
        Assertions.assertEquals("You do not own territory 4",
                notOwnTerritoryUnitAction.isValid(gameBoard));

        //invalid action: does own destination territory
        Map<UnitType, Integer> notOwnDestTerritoryUnitMap = new HashMap<>();
        notOwnDestTerritoryUnitMap.put(UnitType.SOLDIER, 1);
        AbstractAction notOwnDestTerritoryUnitAction = new AttackAction(1, 0,
                notOwnDestTerritoryUnitMap, 1);
        Assertions.assertEquals("You cannot attack owned 0, please try move",
                notOwnDestTerritoryUnitAction.isValid(gameBoard));

        //invalid action: does not contain unit type
        Map<UnitType, Integer> notContainUnitTypeUnitMap = new HashMap<>();
        notContainUnitTypeUnitMap.put(UnitType.INFANTRY, 1);
        AbstractAction notContainUnitTypeAction = new AttackAction(1, 2,
                notContainUnitTypeUnitMap, 1);
        Assertions.assertEquals("The source territory does not contain the unit type.",
                notContainUnitTypeAction.isValid(gameBoard));

        //invalid action: does not contain unit type
        Map<UnitType, Integer> notContainEnoughUnitTypeUnitMap = new HashMap<>();
        notContainEnoughUnitTypeUnitMap.put(UnitType.SOLDIER, 51);
        AbstractAction notContainEnoughUnitTypeAction = new AttackAction(0, 2,
                notContainEnoughUnitTypeUnitMap, 1);
        Assertions.assertEquals("The source territory does not contain enough unit type.",
                notContainEnoughUnitTypeAction.isValid(gameBoard));

        //invalid action: not enough food resources
        Map<UnitType, Integer> notEnoughFoodUnitMap = new HashMap<>();
        notEnoughFoodUnitMap.put(UnitType.SOLDIER, 49);
        AbstractAction notEnoughFoodAction = new AttackAction(0, 2,
                notEnoughFoodUnitMap, 1);
        Assertions.assertEquals("The player does not have enough food resources: 50 < 294",
                notEnoughFoodAction.isValid(gameBoard));

        //invalid action: not reachable
        Map<UnitType, Integer> notReachableUnitMap = new HashMap<>();
        notReachableUnitMap.put(UnitType.SOLDIER, 20);
        AbstractAction notReachableAction = new AttackAction(2, 5,
                notReachableUnitMap, 2);
        Assertions.assertEquals("Not reachable from source Idaho(2) to destinationNew Mexico(5)",
                notReachableAction.isValid(gameBoard));


        //completely valid action: attacker win
        gameBoard.findTerritory(0).updateUnitsMap(UnitType.SOLDIER, 100);
        gameBoard.findTerritory(0).updateUnitsMap(UnitType.INFANTRY, 100);
        gameBoard.findTerritory(2).updateUnitsMap(UnitType.SOLDIER, 100);
        gameBoard.findTerritory(2).updateUnitsMap(UnitType.INFANTRY, 100);

        player1.updateResourceMap(ResourceType.FOOD, 500);

        Map<UnitType, Integer> validUnitMap = new HashMap<>();
        validUnitMap.put(UnitType.SOLDIER, 10);
        validUnitMap.put(UnitType.INFANTRY, 10);
        Action validAction = new AttackAction(0, 2, validUnitMap, 1);
        Assertions.assertNotNull(validAction.simulateApply(gameBoard));
        Assertions.assertNotNull(validAction.apply(gameBoard));
        Assertions.assertNotNull(validAction.applyBefore(gameBoard));
        Assertions.assertNotNull(validAction.applyAfter(gameBoard));

        //completely valid action: defender win
        Map<UnitType, Integer> validUnitMap2 = new HashMap<>();
        validUnitMap2.put(UnitType.SOLDIER, 50);
        Action validAction2 = new AttackAction(0, 3, validUnitMap2, 1);
        Assertions.assertNotNull(validAction2.applyBefore(gameBoard));
        Assertions.assertNotNull(validAction2.applyAfter(gameBoard));
    }

    @Test
    public void test_randomWIN() {
        //to be continue
        Map<UnitType, Integer> unitTypeIntegerMap = new HashMap<>();
        unitTypeIntegerMap.put(UnitType.SOLDIER, 1);
        AttackAction newAttack = new AttackAction(0, 0, unitTypeIntegerMap, 0);
        int result = newAttack.randomWin(UnitType.SOLDIER, UnitType.SOLDIER);
        for (int i = 0; i < 99; i++) {
            result = newAttack.randomWin(UnitType.SOLDIER, UnitType.SOLDIER);
        }
    }


}
