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

        //midkemia, elantris
        narnia.addNeighbor(1, 4);
        //narnia, elantris, scadrial, oz
        midkemia.addNeighbor(0, 4, 5, 2);
        //midkemia, scadrial, mordor, gondor
        oz.addNeighbor(1, 5, 6, 3);
        //oz, mordor
        gondor.addNeighbor(2, 6);
        //narnia, midkemia, scadrial, roshar
        elantris.addNeighbor(0, 1, 5, 7);
        //elantris, roshar, hogwarts, mordor, oz,midkemia
        scadrial.addNeighbor(4, 7, 8, 6, 2, 1);
        //gondor, oz, scadrial, hogwarts
        mordor.addNeighbor(3, 2, 5, 8);
        //elantris, scadrial, hogwarts
        roshar.addNeighbor(4, 5, 8);
        //roshar, scadrial, mordor
        hogwarts.addNeighbor(7, 5, 6);

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
