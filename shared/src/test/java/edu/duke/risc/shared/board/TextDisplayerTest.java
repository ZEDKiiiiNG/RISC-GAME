package edu.duke.risc.shared.board;

import edu.duke.risc.shared.commons.UnitType;
import org.junit.jupiter.api.Test;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.users.Player;


import java.util.Set;

public class TextDisplayerTest {
  @Test
  public void test_TextDisplay() {
    TextDisplayer dis = new TextDisplayer();
    //board and display it
    GameBoard testBoard = new GameBoard(3);
    Player player = new Player(0, UserColor.BLUE);
    Player player2 = new Player(1, UserColor.GREEN);
    Player player3 = new Player(2, UserColor.YELLOW);
    Set<Integer> terris = testBoard.addPlayer(player);
    Set<Integer> terris2 = testBoard.addPlayer(player2);
    testBoard.addPlayer(player3);
    player.setOwnedTerritories(terris);
    player2.setOwnedTerritories(terris2);

    Territory territory = testBoard.findTerritory(1);
    territory.updateVirtualUnitsMap(UnitType.SOLDIER, 1);
    territory.updateUnitsMap(UnitType.SOLDIER, 2);

    dis.display(testBoard);

    //a LOST player TBC



  }

}
