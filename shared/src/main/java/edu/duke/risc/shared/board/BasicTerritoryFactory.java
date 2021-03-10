package edu.duke.risc.shared.board;

import java.util.HashSet;
import java.util.Set;

/**
 * @author eason
 * @date 2021/3/10 20:29
 */
public class BasicTerritoryFactory implements TerritoryFactory {

    @Override
    public Set<Territory> makeTerritories() {
        Set<Territory> result = new HashSet<>();
        Territory narnia = new Territory(1, "Narnia");
        Territory midkemia = new Territory(2, "Midkemia");
        Territory oz = new Territory(3, "Oz");
        Territory gondor = new Territory(4, "Gondor");
        Territory elantris = new Territory(1, "Elantris");
        Territory scadrial = new Territory(1, "Scadrial");
        Territory mordor = new Territory(1, "Mordor");
        Territory roshar = new Territory(1, "Roshar");
        Territory hogwarts = new Territory(1, "Hogwarts");

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
        return result;
    }

}
