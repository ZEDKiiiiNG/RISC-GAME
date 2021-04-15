package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.MissileType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MissileAttackActionTest
 *
 * @author eason
 * @date 2021/4/15 19:09
 */
public class MissileAttackActionTest {

    @Test
    public void missileAttackActionTest() throws InvalidActionException {
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

        player1.obtainMissile(MissileType.MISSILE_LV1, 10);
        //invalid input: invalid player id
        Action invalidPlayerIdAction = new MissileAttackAction(10, ActionType.MISSILE_ATTACK,
                2, MissileType.MISSILE_LV1);
        Assertions.assertEquals("Does not contain user: 10",
                invalidPlayerIdAction.isValid(gameBoard));
        Assertions.assertThrows(InvalidActionException.class, () -> invalidPlayerIdAction.apply(gameBoard));

        //invalid action: owns the destination territory -> cannot attack own place
        Action notOwnTerritoryUnitAction = new MissileAttackAction(1, ActionType.MISSILE_ATTACK,
                0, MissileType.MISSILE_LV1);
        Assertions.assertEquals("You cannot attack with missile on your own territory Utah(0)",
                notOwnTerritoryUnitAction.isValid(gameBoard));

        //invalid action: owns the destination territory -> cannot attack own place
        Action notHaveMissileAction = new MissileAttackAction(1, ActionType.MISSILE_ATTACK,
                4, MissileType.MISSILE_LV6);
        Assertions.assertEquals("You do not have enough MISSILE LV6",
                notHaveMissileAction.isValid(gameBoard));

        //valid action
        gameBoard.findTerritory(4).updateUnitsMap(UnitType.SOLDIER, 10);
        Action validAction = new MissileAttackAction(1, ActionType.MISSILE_ATTACK,
                4, MissileType.MISSILE_LV1);
        Assertions.assertNull(validAction.simulateApply(gameBoard));
        Assertions.assertNotNull(validAction.apply(gameBoard));

    }
}
