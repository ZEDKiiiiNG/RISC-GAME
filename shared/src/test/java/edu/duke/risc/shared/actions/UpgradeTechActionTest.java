package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * UpgradeTechAction
 *
 * @author eason
 * @date 2021/4/15 16:33
 */
public class UpgradeTechActionTest {

    @Test
    public void upgradeTechActionTest() throws InvalidActionException {
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


        //invalid action: invalid player id
        Action invalidPlayerAction = new UpgradeTechAction(10, ActionType.UPGRADE_TECH);
        Assertions.assertNotNull(invalidPlayerAction.isValid(gameBoard));
        Assertions.assertThrows(InvalidActionException.class, ()->invalidPlayerAction.simulateApply(gameBoard));

        //invalid action: already at top level
        player1.updateResourceMap(ResourceType.TECH, 1000);
        player1.upgradeTechLevel(true);
        player1.upgradeTechLevel(true);
        player1.upgradeTechLevel(true);
        player1.upgradeTechLevel(true);
        player1.upgradeTechLevel(true);
        Action topLevelAction = new UpgradeTechAction(1, ActionType.UPGRADE_TECH);
        Assertions.assertNotNull(topLevelAction.isValid(gameBoard));
        Assertions.assertThrows(InvalidActionException.class, () -> topLevelAction.apply(gameBoard));


        //invalid action: not enough resource
        player2.updateResourceMap(ResourceType.TECH, -30);
        Action notEnoughResourceAction = new UpgradeTechAction(2, ActionType.UPGRADE_TECH);
        Assertions.assertNotNull(notEnoughResourceAction.isValid(gameBoard));

        //valid action
        player3.updateResourceMap(ResourceType.TECH, 100);
        Action validAction = new UpgradeTechAction(3, ActionType.UPGRADE_TECH);
        Assertions.assertNull(validAction.simulateApply(gameBoard));
        Assertions.assertNotNull(validAction.apply(gameBoard));

    }

}
