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
 * @author eason
 * @date 2021/4/15 16:21
 */
public class CloakTerritoryActionTest {


    @Test
    public void cloakTerritoryActionTest() throws InvalidActionException {
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
        Action invalidPlayerAction = new CloakTerritoryAction(10, ActionType.CLOAK_CONDUCT,
                1, null);
        Assertions.assertNotNull(invalidPlayerAction.isValid(gameBoard));

        //invalid action: cloaking not researched
        Action cloakNotResearchedAction = new CloakTerritoryAction(1, ActionType.CLOAK_CONDUCT,
                1, null);
        Assertions.assertEquals("Player has not yet research cloaking", cloakNotResearchedAction.isValid(gameBoard));
        Assertions.assertThrows(InvalidActionException.class, () -> cloakNotResearchedAction.apply(gameBoard));

        //invalid action: not own territory
        player2.doResearchCloaking();
        Action notOwnTerritoryAction = new CloakTerritoryAction(2, ActionType.CLOAK_CONDUCT,
                0, null);
        Assertions.assertEquals("The player does not own the destination territory Utah(0)",
                notOwnTerritoryAction.isValid(gameBoard));

        //invalid action: not have enough resources
        player2.updateResourceMap(ResourceType.TECH, -40);
        Action notEnoughResourceAction = new CloakTerritoryAction(2, ActionType.CLOAK_CONDUCT,
                3, null);
        Assertions.assertNotNull(notEnoughResourceAction.isValid(gameBoard));

        //valid action
        player2.updateResourceMap(ResourceType.TECH, 100);

        Action validAction = new CloakTerritoryAction(2, ActionType.CLOAK_CONDUCT,
                3, null);
        Assertions.assertNotNull(validAction.simulateApply(gameBoard));

    }
}
