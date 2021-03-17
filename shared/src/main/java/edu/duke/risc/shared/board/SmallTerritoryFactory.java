package edu.duke.risc.shared.board;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eason
 * @date 2021/3/14 23:15
 */
public class SmallTerritoryFactory implements TerritoryFactory {

    @Override
    public Map<Integer, Territory> makeTerritories(int playerNum) {
        Map<Integer, Territory> result = new HashMap<>(10);
        Territory narnia = new Territory(0, "Narnia");
        Territory midkemia = new Territory(1, "Midkemia");
        Territory oz = new Territory(2, "Oz");

        //midkemia, oz
        narnia.addNeighbor(1, 2);
        //narnia, oz
        midkemia.addNeighbor(0, 2);
        //narnia, midkemia
        oz.addNeighbor(0, 1);

        result.put(narnia.getTerritoryId(), narnia);
        result.put(midkemia.getTerritoryId(), midkemia);
        result.put(oz.getTerritoryId(), oz);
        return result;
    }

    @Override
    public int territoryNum() {
        return 3;
    }

}
