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

        Territory mordor = new Territory(3, "Mordor");
        Territory gondor = new Territory(4, "Gondor");
        Territory hogwarts = new Territory(5, "Hogwarts");

        Territory elantris = new Territory(6, "Elantris");
        Territory scadrial = new Territory(7, "Scadrial");
        Territory roshar = new Territory(8, "Roshar");

        //midkemia, elantris
        narnia.addNeighbor(1, 6);
        //narnia, elantris, scadrial, oz
        midkemia.addNeighbor(0, 6, 7, 2);
        //midkemia, scadrial, mordor, gondor
        oz.addNeighbor(1, 7, 3, 4);
        //oz, mordor
        gondor.addNeighbor(2, 3);
        //narnia, midkemia, scadrial, roshar
        elantris.addNeighbor(0, 1, 7, 8);
        //elantris, roshar, hogwarts, mordor, oz,midkemia
        scadrial.addNeighbor(6, 8, 5, 3, 2, 1);
        //gondor, oz, scadrial, hogwarts
        mordor.addNeighbor(4, 2, 7, 5);
        //elantris, scadrial, hogwarts
        roshar.addNeighbor(6, 7, 5);
        //roshar, scadrial, mordor
        hogwarts.addNeighbor(8, 7, 3);

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
