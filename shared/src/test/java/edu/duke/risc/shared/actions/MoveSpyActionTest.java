package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author eason
 * @date 2021/4/15 19:01
 */
public class MoveSpyActionTest {

    @Test
    public void moveSpyActionTest() throws InvalidActionException {
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

        //invalid input: invalid player id
        Action invalidPlayerIdAction = new MoveSpyAction(10, ActionType.MOVE_SPY, 0, 1, 2);
        Assertions.assertEquals("Does not contain user: 10", invalidPlayerIdAction.isValid(gameBoard));
        Assertions.assertThrows(InvalidActionException.class, () -> invalidPlayerIdAction.apply(gameBoard));

        //invalid input: not adjacent to each other
        Action notAdjacentTerritoriesAction = new MoveSpyAction(1, ActionType.MOVE_SPY,
                1, 5, 2);
        Assertions.assertEquals("Source and destination should be adjacent to each other",
                notAdjacentTerritoriesAction.isValid(gameBoard));

        //invalid input: not enough spies in territory
        Action notEnoughSpiesAction = new MoveSpyAction(1, ActionType.MOVE_SPY,
                1, 0, 2);
        Assertions.assertEquals(
                "Player 1 does not has enough spies (0 < 2) in territory Nevada(1)",
                notEnoughSpiesAction.isValid(gameBoard));

        //valid action
        gameBoard.findTerritory(1).updateSpiesMap(1, 2);
        Action validAction = new MoveSpyAction(1, ActionType.MOVE_SPY, 1, 0, 1);
        Assertions.assertNotNull(validAction.simulateApply(gameBoard));
        Assertions.assertNotNull(validAction.apply(gameBoard));

    }

}
