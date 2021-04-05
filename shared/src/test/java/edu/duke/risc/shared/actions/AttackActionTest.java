package edu.duke.risc.shared.actions;


import edu.duke.risc.shared.board.GameBoard;
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

public class AttackActionTest {
    @Test
    public void test_AttackAction() {
        Map<UnitType, Integer> unitTypeIntegerMap = new HashMap<>();
        unitTypeIntegerMap.put(UnitType.SOLDIER, 1);
        AttackAction newAttack = new AttackAction(0, 0, unitTypeIntegerMap, 0);
        //apply
        GameBoard testBoard = new GameBoard(3);
        try {
            newAttack.apply(testBoard);
        } catch (InvalidActionException e) {
        }//should return " "
        //Testing isValid
        newAttack.isValid(testBoard);
        //number > 0, but no player
        AttackAction newAttack1 = new AttackAction(0, 0, unitTypeIntegerMap, 0);
        newAttack1.isValid(testBoard);
        //now contains user
        //but
        //do not own territory
        Player player = new Player(0, UserColor.BLUE);
        Player player2 = new Player(1, UserColor.GREEN);
        Set<Integer> terris = testBoard.addPlayer(player);
        Set<Integer> terris2 = testBoard.addPlayer(player2);
        assertTrue(testBoard.getPlayers().containsKey(0));
        assertTrue(testBoard.getPlayers().get(0).equals(player));
        newAttack1.isValid(testBoard);
        //now own territory, but attack own
        player.setOwnedTerritories(terris);
        player2.setOwnedTerritories(terris2);
        newAttack1.isValid(testBoard);
        //now attack another territory, but no unit type
        AttackAction newAttack2 = new AttackAction(0, 4,unitTypeIntegerMap, 0);
        newAttack2.isValid(testBoard);
        //now has unit type, but not enough Unit type
        testBoard.getTerritories().get(0).updateUnitsMap(UnitType.SOLDIER, 1);//player 1 has 1 soldier at territory 0
        testBoard.getTerritories().get(1).updateUnitsMap(UnitType.SOLDIER, 1);//player 2 has 1 soldier at territory 1
        testBoard.getTerritories().get(4).updateUnitsMap(UnitType.SOLDIER, 1);//player 2 has 1 soldier at territory 1
        newAttack2.isValid(testBoard);
        //enough unit type, but not reachable
        testBoard.getTerritories().get(0).updateUnitsMap(UnitType.SOLDIER, 4);//player 1 has 4 soldier at territory 0
        AttackAction newAttack3 = new AttackAction(0, 8, unitTypeIntegerMap, 0);
//        newAttack3.isValid(testBoard);
        //reacheable
//        try {
//            newAttack2.simulateApply(testBoard);
//        } catch (InvalidActionException e) {
//        }
//        //applyAfter
//        try {
//            newAttack2.applyBefore(testBoard);
//        } catch (InvalidActionException e) {
//        }
//        int i = 0;
//        try {
//            newAttack.applyBefore(testBoard);
//        } catch (InvalidActionException e) {
//            i = 1;
//        }
//        assertEquals(1, i);
//        try {
//            newAttack.applyAfter(testBoard);
//        } catch (InvalidActionException e) {
//            i = 1;
//        }
//        try {
//            newAttack2.applyAfter(testBoard);
//        } catch (InvalidActionException e) {
//            i = 1;
//        }
//        try {
//            newAttack1.simulateApply(testBoard);
//        } catch (InvalidActionException e) {
//            i = 1;
//        }
//        newAttack.toString();


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
