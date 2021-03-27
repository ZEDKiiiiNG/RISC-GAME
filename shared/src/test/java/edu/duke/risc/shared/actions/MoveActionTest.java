package edu.duke.risc.shared.actions;


import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoveActionTest {
    @Test
    public void test_MoveAction() {
        Map<UnitType, Integer> unitTypeIntegerMap = new HashMap<>();
        unitTypeIntegerMap.put(UnitType.SOLDIER, 1);
        MoveAction newMove = new MoveAction(0, 0, unitTypeIntegerMap, 0);
        //apply
        GameBoard testBoard = new GameBoard(3);
        try {
            newMove.apply(testBoard);
        } catch (InvalidActionException e) {
        }//should return " "
        //Testing isValid
        newMove.isValid(testBoard);
        //number > 0, but no player
        MoveAction newMove1 = new MoveAction(0, 0, unitTypeIntegerMap, 0);
        newMove1.isValid(testBoard);
        //now contains user
        //but
        //do not own territory
        Player player = new Player(0, UserColor.BLUE);
        Player player2 = new Player(1, UserColor.GREEN);
        Set<Integer> terris = testBoard.addPlayer(player);
        Set<Integer> terris2 = testBoard.addPlayer(player2);
        assertTrue(testBoard.getPlayers().containsKey(0));
        assertTrue(testBoard.getPlayers().get(0).equals(player));
        newMove1.isValid(testBoard);
        //now own territory, but attack own
        player.setOwnedTerritories(terris);
        player2.setOwnedTerritories(terris2);
        newMove1.isValid(testBoard);
        //now attack another territory, but no unit type
        MoveAction newMove2 = new MoveAction(0, 1, unitTypeIntegerMap, 0);
        newMove2.isValid(testBoard);
        //now has unit type, but not enough Unit type
        testBoard.getTerritories().get(0).updateUnitsMap(UnitType.SOLDIER, 1);//player 1 has 1 soldier at territory 0
        testBoard.getTerritories().get(1).updateUnitsMap(UnitType.SOLDIER, 1);//player 2 has 1 soldier at territory 1
        newMove1.isValid(testBoard);

        //enough unit type, but not reachable
        testBoard.getTerritories().get(0).updateUnitsMap(UnitType.SOLDIER, 4);//player 1 has 4 soldier at territory 0
        MoveAction newMove3 = new MoveAction(0, 0, unitTypeIntegerMap, 0);
        newMove3.isValid(testBoard);
        //reacheable
        //assertEquals(null, newMove2.isValid(testBoard));
        try {
            newMove2.simulateApply(testBoard);
        } catch (InvalidActionException e) {
        }
        //get Player territory
        Set<Territory> playerT = newMove2.getPlayerTerritory(testBoard);
        try {
            newMove1.apply(testBoard);
        } catch (InvalidActionException e) {
        }
        //abstract Action class's applybefore&applyafter
        try {
            assertEquals(null, newMove.applyBefore(testBoard));
            assertEquals(null, newMove.applyAfter(testBoard));
        } catch (InvalidActionException e) {
        }
    }

}
