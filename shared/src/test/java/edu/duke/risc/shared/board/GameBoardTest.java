package edu.duke.risc.shared.board;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.risc.shared.commons.UnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import edu.duke.risc.shared.users.Player;
import edu.duke.risc.shared.commons.UserColor;

import java.util.HashMap;
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
    Player player3 = new Player(3, UserColor.YELLOW);
    Set<Integer> terris = testBoard.addPlayer(player);
    Set<Integer> terris2 = testBoard.addPlayer(player2);
    Set<Integer> terris3 = testBoard.addPlayer(player3);
    player.setOwnedTerritories(terris);
    player2.setOwnedTerritories(terris2);
    player3.setOwnedTerritories(terris3);
    testBoard.getPlayerAssignedTerritoryInfo(0);
    testBoard.toString();
    testBoard.getDisplayer();
    testBoard.getValidTerritoriesSize();
    testBoard.territoryGrow();
    assertEquals(3, testBoard.getShouldWaitPlayers());
    testBoard.getPlayerInfo(0);
    testBoard.getWinner();
    testBoard.setGameStart();
    testBoard.setGameOver();
    assertEquals(true, testBoard.isGameOver());
    testBoard.forwardPlacementPhase();
    testBoard.getUnitTypeMapper();

    testBoard.calculateMoveCost(1, 5, 1);
    testBoard.calculateMoveCost(1, 4, 1);
    testBoard.calculateMoveCost(1, 3, 1);
    testBoard.calculateMoveCost(1, 2, 1);

    //player move from territory
    Map<UnitType, Integer> moveMap = new HashMap<>();
    moveMap.put(UnitType.SOLDIER, 2);
    testBoard.playerMoveFromTerritory(1, moveMap);

    Territory territory4 = testBoard.findTerritory(4);
    territory4.updateSpiesMap(1, 3);

    testBoard.isTerritoryVisible(1, 1);
    testBoard.isTerritoryVisible(1, 3);
    testBoard.isTerritoryVisible(1, 4);
    testBoard.isTerritoryVisible(1, 5);

    //mark lost
    player.markLost();
    testBoard.getShouldWaitPlayers();
    player2.markWin();
    testBoard.getWinner();

    testBoard.isTerritoryVisible(1, 1);
    testBoard.isTerritoryVisible(1, 1);

    //find player owns territory
    testBoard.findPlayerOwnsTerritory(10);
    testBoard.findPlayerOwnsTerritory(1);

    testBoard.reduceCloaking();


  }


}
