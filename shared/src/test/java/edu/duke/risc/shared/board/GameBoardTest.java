package edu.duke.risc.shared.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import edu.duke.risc.shared.users.Player;
import edu.duke.risc.shared.commons.UserColor;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameBoardTest {
  @Test
  public void test_GameBoard() {
    GameBoard testBoard = new GameBoard(3);
    testBoard.displayBoard();
    Player player = new Player(0, UserColor.BLUE);
    Player player2 = new Player(1, UserColor.GREEN);
    Set<Integer> terris = testBoard.addPlayer(player);
    Set<Integer> terris2 = testBoard.addPlayer(player2);
    player.setOwnedTerritories(terris);
    player2.setOwnedTerritories(terris2);
    testBoard.getPlayerAssignedTerritoryInfo(0);
    testBoard.toString();
    testBoard.territoryGrow();
    assertEquals(2, testBoard.getShouldWaitPlayers());
    testBoard.getPlayerInfo(0);
    testBoard.getWinner();
    testBoard.setGameStart();
    testBoard.setGameOver();
    assertEquals(true, testBoard.isGameOver());
    testBoard.forwardPlacementPhase();
    testBoard.getUnitTypeMapper();
  }


}
