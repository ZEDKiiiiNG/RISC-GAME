package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
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
 * @date 2021/4/15 15:57
 */
public class CloakResearchActionTest {


    @Test
    public void cloakResearchActionTest() throws InvalidActionException {
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
        Action invalidPlayerAction = new CloakResearchAction(10);
        Assertions.assertNotNull(invalidPlayerAction.isValid(gameBoard));

        //invalid action: not enough tech level
        Action notEnoughTechAction = new CloakResearchAction(1);
        Assertions.assertNotNull(notEnoughTechAction.isValid(gameBoard));
        Assertions.assertThrows(InvalidActionException.class, () -> notEnoughTechAction.apply(gameBoard));

        //invalid action: not enough food resource
        player2.updateResourceMap(ResourceType.TECH, 80);
        player2.upgradeTechLevel(true);
        player2.upgradeTechLevel(true);
        Action notEnoughResource = new CloakResearchAction(2);
        Assertions.assertNotNull(notEnoughResource.isValid(gameBoard));

        //valid action
        player3.updateResourceMap(ResourceType.TECH, 1000);
        player3.upgradeTechLevel(true);
        player3.upgradeTechLevel(true);

        Action validAction = new CloakResearchAction(3);
        Assertions.assertNull(validAction.isValid(gameBoard));
        Assertions.assertNotNull(validAction.simulateApply(gameBoard));
        //invalid action: already researched cloaking
        Assertions.assertNotNull(validAction.isValid(gameBoard));

    }

}
