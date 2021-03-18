package edu.duke.risc.shared.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BasicTerritoryFactoryTest {
  @Test
  public void test_territoryNum() {
    BasicTerritoryFactory f = new BasicTerritoryFactory();
    f.makeTerritories(5);
    f.makeTerritories(1);//default
    f.makeTerritories(2);
    f.makeTerritories(4);
    f.makeTerritories(3);
    assertEquals(9, f.territoryNum());
  }

}
