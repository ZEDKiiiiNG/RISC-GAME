package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlacementActionTest {
    private InvalidActionException InvalidActionException;

    @Test
    public void placementActionTest() {
        GameBoard gameBoard = new GameBoard(3);
        Player player = new Player(0, UserColor.BLUE);
        assertEquals(UserColor.BLUE.toString(), "Blue");
        player.addOwnedTerritory(0);

        gameBoard.addPlayer(player);
        AbstractAction action = new PlacementAction(0, UnitType.SOLDIER, 1, 0);
        AbstractAction action1 = new PlacementAction(0, UnitType.SOLDIER, 1, 1);
        AbstractAction action2 = new PlacementAction(3, UnitType.SOLDIER, 1, 0);
        AbstractAction action3 = new PlacementAction(0, UnitType.SOLDIER, 20, 0);
        action.isValid(gameBoard);
        action1.isValid(gameBoard);
        action2.isValid(gameBoard);
        action3.isValid(gameBoard);
        assertNull(action.isValid(gameBoard));
        assertNotNull(action1.isValid(gameBoard));
        assertNotNull(action2.isValid(gameBoard));
        assertNotNull(action3.isValid(gameBoard));

        assertThrows(InvalidActionException.class, ()-> action1.apply(gameBoard));
        assertDoesNotThrow(()-> action.apply(gameBoard));
        assertDoesNotThrow(()-> action.simulateApply(gameBoard));
    }

}
