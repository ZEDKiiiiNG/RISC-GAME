package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author eason
 * @date 2021/4/15 17:23
 */
public class TrainSpyActionTest {

    @Test
    public void TrainSpyActionTest() throws InvalidActionException {
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
        Action invalidPlayerIdAction = new TrainSpyAction(10, ActionType.TRAIN_SPY, 0, 1);
        Assertions.assertEquals("Does not contain user: 10", invalidPlayerIdAction.isValid(gameBoard));
        Assertions.assertThrows(InvalidActionException.class, () -> invalidPlayerIdAction.apply(gameBoard));

        //invalid input: invalid player id
        Action notOwnTerritoryAction = new TrainSpyAction(1, ActionType.TRAIN_SPY, 4, 1);
        Assertions.assertEquals("The player does not own the destination territory Colorado(4)",
                notOwnTerritoryAction.isValid(gameBoard));

        //invalid input: invalid player id
        gameBoard.findTerritory(0).updateUnitsMap(UnitType.SOLDIER, 5);
        player1.updateResourceMap(ResourceType.TECH, -50);
        Action notEnoughResourceAction = new TrainSpyAction(1, ActionType.TRAIN_SPY, 0, 1);
        Assertions.assertEquals("The player does not have enough tech resources: 0 < 20",
                notEnoughResourceAction.isValid(gameBoard));

        //invalid input: not enough units
        player1.updateResourceMap(ResourceType.TECH, 500);
        Action notEnoughUnitsAction = new TrainSpyAction(1, ActionType.TRAIN_SPY, 0, 10);
        Assertions.assertEquals("Does not have enough units (5<10) in territory Utah(0)",
                notEnoughUnitsAction.isValid(gameBoard));


        //valid action
        player2.updateResourceMap(ResourceType.TECH, 500);
        gameBoard.findTerritory(1).updateUnitsMap(UnitType.SOLDIER, 10);
        Action validAction = new TrainSpyAction(1, ActionType.TRAIN_SPY, 1, 1);
        Assertions.assertNotNull(validAction.simulateApply(gameBoard));

        //valid action: not have enough current type
        player3.updateResourceMap(ResourceType.TECH, 500);
        gameBoard.findTerritory(4).updateUnitsMap(UnitType.SOLDIER, 1);
        gameBoard.findTerritory(4).updateUnitsMap(UnitType.INFANTRY, 1);
        Action validAction2 = new TrainSpyAction(3, ActionType.TRAIN_SPY, 4, 2);
        Assertions.assertNotNull(validAction2.simulateApply(gameBoard));

    }
}
