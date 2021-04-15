package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlacementActionTest {

    @Test
    public void placementActionTest() throws InvalidActionException {
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
        AbstractAction invalidAction = new PlacementAction(0, invalidUnitMap, 1);
        Assertions.assertNotNull(invalidAction.isValid(gameBoard));

        //invalid input: invalid player id
        Map<UnitType, Integer> unitMap2 = new HashMap<>();
        unitMap2.put(UnitType.SOLDIER, 1);
        AbstractAction invalidPlayerIdAction = new PlacementAction(0, unitMap2, 10);
        Assertions.assertNotNull(invalidPlayerIdAction.isValid(gameBoard));

        //invalid action: does not own territory
        Map<UnitType, Integer> notOwnTerritoryUnitMap = new HashMap<>();
        notOwnTerritoryUnitMap.put(UnitType.SOLDIER, 1);
        AbstractAction notOwnTerritoryUnitAction = new PlacementAction(4, notOwnTerritoryUnitMap, 1);
        Assertions.assertNotNull(notOwnTerritoryUnitAction.isValid(gameBoard));

        //player does not contain unit type error
        Map<UnitType, Integer> notContainUnitUnitMap = new HashMap<>();
        notContainUnitUnitMap.put(UnitType.MASTER, 1);
        AbstractAction notContainUnitAction = new PlacementAction(0, notContainUnitUnitMap, 1);
        Assertions.assertNotNull(notContainUnitAction.isValid(gameBoard));
        Assertions.assertThrows(InvalidActionException.class, ()->notContainUnitAction.apply(gameBoard));

        //completely valid action
        Map<UnitType, Integer> validUnitMap = new HashMap<>();
        validUnitMap.put(UnitType.SOLDIER, 1);
        AbstractAction validAction = new PlacementAction(0, validUnitMap, 1);
        Assertions.assertNull(validAction.isValid(gameBoard));
        Assertions.assertNotNull(validAction.simulateApply(gameBoard));
        Assertions.assertNotNull(validAction.apply(gameBoard));

    }

}
