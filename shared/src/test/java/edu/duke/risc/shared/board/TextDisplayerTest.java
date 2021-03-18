package edu.duke.risc.shared.board;

import static org.junit.jupiter.api.Assertions.*;
import edu.duke.risc.shared.board.BasicTerritoryFactory;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.board.TerritoryFactory;
import org.junit.jupiter.api.Test;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.TextDisplayer;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class TextDisplayerTest {
  @Test
  public void test_TextDisplay() {
    TextDisplayer dis = new TextDisplayer();
    //board and display it
    GameBoard testBoard = new GameBoard(3);
    Player player = new Player(0, UserColor.BLUE);
    Player player2 = new Player(1, UserColor.GREEN);
    Set<Integer> terris = testBoard.addPlayer(player);
    Set<Integer> terris2 = testBoard.addPlayer(player2);
    player.setOwnedTerritories(terris);
    player2.setOwnedTerritories(terris2);
    dis.display(testBoard);
    //a LOST player TBC

  }

}
