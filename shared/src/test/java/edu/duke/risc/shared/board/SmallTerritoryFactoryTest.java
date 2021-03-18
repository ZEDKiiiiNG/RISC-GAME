package edu.duke.risc.shared.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SmallTerritoryFactoryTest {
  @Test
  public void test_smallTerritoryFactory() {
    SmallTerritoryFactory SF = new SmallTerritoryFactory();
    SF.makeTerritories(3);//3 players
    assertEquals(3, SF.territoryNum());
  }

}
