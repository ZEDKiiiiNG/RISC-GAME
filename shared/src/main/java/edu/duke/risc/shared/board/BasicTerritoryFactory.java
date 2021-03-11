package edu.duke.risc.shared.board;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eason
 * @date 2021/3/10 20:29
 */
public class BasicTerritoryFactory implements TerritoryFactory {

    private final int TOTAL_TERRITORIES = 9;

    @Override
    public Map<Integer, Territory> makeTerritories() {
        Map<Integer, Territory> result = new HashMap<>(10);
        Territory narnia = new Territory(0, "Narnia");
        Territory midkemia = new Territory(1, "Midkemia");
        Territory oz = new Territory(2, "Oz");
        Territory gondor = new Territory(3, "Gondor");
        Territory elantris = new Territory(4, "Elantris");
        Territory scadrial = new Territory(5, "Scadrial");
        Territory mordor = new Territory(6, "Mordor");
        Territory roshar = new Territory(7, "Roshar");
        Territory hogwarts = new Territory(8, "Hogwarts");

        narnia.addNeighbor(midkemia, elantris);
        midkemia.addNeighbor(narnia, elantris, scadrial, oz);
        oz.addNeighbor(midkemia, scadrial, mordor, gondor);
        gondor.addNeighbor(oz, mordor);
        elantris.addNeighbor(narnia, midkemia, scadrial, roshar);
        scadrial.addNeighbor(elantris, roshar, hogwarts, mordor, oz, midkemia);
        mordor.addNeighbor(gondor, oz, scadrial, hogwarts);
        roshar.addNeighbor(elantris, scadrial, hogwarts);
        hogwarts.addNeighbor(roshar, scadrial, mordor);

        result.put(narnia.getTerritoryId(), narnia);
        result.put(midkemia.getTerritoryId(), midkemia);
        result.put(oz.getTerritoryId(), oz);
        result.put(gondor.getTerritoryId(), gondor);
        result.put(elantris.getTerritoryId(), elantris);
        result.put(scadrial.getTerritoryId(), scadrial);
        result.put(mordor.getTerritoryId(), mordor);
        result.put(roshar.getTerritoryId(), roshar);
        result.put(hogwarts.getTerritoryId(), hogwarts);
        return result;
    }

    @Override
    public int territoryNum() {
        return TOTAL_TERRITORIES;
    }

}
