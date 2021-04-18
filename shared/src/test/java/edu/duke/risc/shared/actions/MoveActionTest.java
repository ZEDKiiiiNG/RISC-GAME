package edu.duke.risc.shared.actions;


import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
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

public class MoveActionTest {
    @Test
    public void test_MoveAction() throws InvalidActionException {
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

        player1.addOwnedTerritory(5);
        player2.addOwnedTerritory(5);

        Territory territory0 = gameBoard.findTerritory(0);
        Territory territory2 = gameBoard.findTerritory(2);
        territory0.updateUnitsMap(UnitType.SOLDIER, 50);
        territory2.updateUnitsMap(UnitType.SOLDIER, 50);

        //invalid unit map: unit value < 0
        Map<UnitType, Integer> invalidUnitMap = new HashMap<>();
        invalidUnitMap.put(UnitType.SOLDIER, -1);
        AbstractAction invalidAction = new MoveAction(0, 1, invalidUnitMap, 1);
        Assertions.assertNotNull(invalidAction.isValid(gameBoard));

        //invalid input: invalid player id
        Map<UnitType, Integer> unitMap2 = new HashMap<>();
        unitMap2.put(UnitType.SOLDIER, 1);
        AbstractAction invalidPlayerIdAction = new MoveAction(0, 1, unitMap2, 10);
        Assertions.assertNotNull(invalidPlayerIdAction.isValid(gameBoard));

        //invalid action: does not own source territory
        Map<UnitType, Integer> notOwnTerritoryUnitMap = new HashMap<>();
        notOwnTerritoryUnitMap.put(UnitType.SOLDIER, 1);
        AbstractAction notOwnTerritoryUnitAction = new MoveAction(4, 1, notOwnTerritoryUnitMap, 1);
        Assertions.assertNotNull(notOwnTerritoryUnitAction.isValid(gameBoard));

        //invalid action:  does not own destination territory
        Map<UnitType, Integer> notOwnDestTerritoryUnitMap = new HashMap<>();
        notOwnDestTerritoryUnitMap.put(UnitType.SOLDIER, 1);
        AbstractAction notOwnDestTerritoryUnitAction = new MoveAction(1, 4,
                notOwnDestTerritoryUnitMap, 1);
        Assertions.assertNotNull(notOwnDestTerritoryUnitAction.isValid(gameBoard));
        Assertions.assertThrows(InvalidActionException.class, () -> notOwnDestTerritoryUnitAction.apply(gameBoard));

        //invalid action: does not contain unit type
        Map<UnitType, Integer> notContainUnitTypeUnitMap = new HashMap<>();
        notContainUnitTypeUnitMap.put(UnitType.INFANTRY, 1);
        AbstractAction notContainUnitTypeAction = new MoveAction(1, 0,
                notContainUnitTypeUnitMap, 1);
        Assertions.assertNotNull(notContainUnitTypeAction.isValid(gameBoard));

        //invalid action: does not contain unit type
        Map<UnitType, Integer> notContainEnoughUnitTypeUnitMap = new HashMap<>();
        notContainEnoughUnitTypeUnitMap.put(UnitType.SOLDIER, 51);
        AbstractAction notContainEnoughUnitTypeAction = new MoveAction(0, 1,
                notContainEnoughUnitTypeUnitMap, 1);
        Assertions.assertNotNull(notContainEnoughUnitTypeAction.isValid(gameBoard));

        //invalid action: not enough food resources
        Map<UnitType, Integer> notEnoughFoodUnitMap = new HashMap<>();
        notEnoughFoodUnitMap.put(UnitType.SOLDIER, 49);
        AbstractAction notEnoughFoodAction = new MoveAction(0, 1,
                notEnoughFoodUnitMap, 1);
        Assertions.assertNotNull(notEnoughFoodAction.isValid(gameBoard));

        //invalid action: not reachable
        Map<UnitType, Integer> notReachableUnitMap = new HashMap<>();
        notReachableUnitMap.put(UnitType.SOLDIER, 20);
        AbstractAction notReachableAction = new MoveAction(2, 5,
                notReachableUnitMap, 2);
        Assertions.assertNotNull(notReachableAction.isValid(gameBoard));


        //completely valid action
        Map<UnitType, Integer> validUnitMap = new HashMap<>();
        validUnitMap.put(UnitType.SOLDIER, 1);
        MoveAction validAction = new MoveAction(0, 1, validUnitMap, 1);
        validAction.getPlayerTerritory(gameBoard);
        Assertions.assertNull(validAction.isValid(gameBoard));
        Assertions.assertNotNull(validAction.simulateApply(gameBoard));
        Assertions.assertNotNull(validAction.apply(gameBoard));

    }

}
