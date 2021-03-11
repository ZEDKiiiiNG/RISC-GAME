package edu.duke.risc.shared.board;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eason
 * @date 2021/3/10 20:29
 */
public class BasicTerritoryFactory implements TerritoryFactory {

    private final int TOTAL_TERRITORIES = 9;

    @Override
    public List<Territory> makeTerritories() {
        List<Territory> result = new ArrayList<>();
        Territory narnia = new Territory(1, "Narnia");
        Territory midkemia = new Territory(2, "Midkemia");
        Territory oz = new Territory(3, "Oz");
        Territory gondor = new Territory(4, "Gondor");
        Territory elantris = new Territory(5, "Elantris");
        Territory scadrial = new Territory(6, "Scadrial");
        Territory mordor = new Territory(7, "Mordor");
        Territory roshar = new Territory(8, "Roshar");
        Territory hogwarts = new Territory(9, "Hogwarts");

        narnia.addNeighbor(midkemia, elantris);
        midkemia.addNeighbor(narnia, elantris, scadrial, oz);
        oz.addNeighbor(midkemia, scadrial, mordor, gondor);
        gondor.addNeighbor(oz, mordor);
        elantris.addNeighbor(narnia, midkemia, scadrial, roshar);
        scadrial.addNeighbor(elantris, roshar, hogwarts, mordor, oz, midkemia);
        mordor.addNeighbor(gondor, oz, scadrial, hogwarts);
        roshar.addNeighbor(elantris, scadrial, hogwarts);
        hogwarts.addNeighbor(roshar, scadrial, mordor);

        result.add(narnia);
        result.add(midkemia);
        result.add(oz);
        result.add(gondor);
        result.add(elantris);
        result.add(scadrial);
        result.add(mordor);
        result.add(roshar);
        result.add(hogwarts);
        return result;
    }

    @Override
    public int territoryNum() {
        return TOTAL_TERRITORIES;
    }

}
