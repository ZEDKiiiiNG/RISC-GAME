package edu.duke.risc.shared.board;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic factory to produce territories map
 *
 * @author eason
 * @date 2021/3/10 20:29
 */
public class BasicTerritoryFactory implements TerritoryFactory {

    /**
     * Total number of territories in the map
     */
    private int totalTerritoryNum;

    @Override
    public Map<Integer, Territory> makeTerritories(int playerNum) {
        switch (playerNum) {
            case 2:
                this.totalTerritoryNum = 2;
                return createMapForTwo();
            case 3:
                this.totalTerritoryNum = 9;
                return createMapForThree();
            case 4:
                this.totalTerritoryNum = 12;
                return createMapForFour();
            case 5:
                this.totalTerritoryNum = 15;
                return createMapForFive();
            default:
                System.out.println("Invalid number of players " + playerNum);
                return null;
        }
    }

    /**
     * createMapForFive
     * @return map information
     */
    private Map<Integer, Territory> createMapForFive() {
        Map<Integer, Territory> result = createMapForFour();
        Territory shanghai = new Territory(12, "Shanghai");
        Territory hangzhou = new Territory(13, "Hangzhou");
        Territory peking = new Territory(14, "Peking");

        shanghai.addNeighbor(0, 13, 5, 7);
        hangzhou.addNeighbor(6, 8, 5);
        peking.addNeighbor(0, 7, 5, 9);

        result.put(shanghai.getTerritoryId(), shanghai);
        result.put(hangzhou.getTerritoryId(), hangzhou);
        result.put(peking.getTerritoryId(), peking);

        return result;
    }

    /**
     * createMapForFour
     * @return map information
     */
    private Map<Integer, Territory> createMapForFour() {
        Map<Integer, Territory> result = createMapForThree();
        Territory durham = new Territory(9, "Durham");
        Territory cary = new Territory(10, "Cary");
        Territory la = new Territory(11, "LA");
        Territory dc = new Territory(12, "D.C.");

        durham.addNeighbor(0, 3, 5, 7);
        cary.addNeighbor(6, 8, 5, 3, 2, 1);
        la.addNeighbor(0, 7, 5, 9);
        dc.addNeighbor(0, 3, 5, 11);

        result.put(durham.getTerritoryId(), durham);
        result.put(cary.getTerritoryId(), cary);
        result.put(la.getTerritoryId(), la);
        result.put(dc.getTerritoryId(), dc);

        return result;
    }

    /**
     * createMapForThree
     * @return map information
     */
    private Map<Integer, Territory> createMapForThree() {
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

    /**
     * createMapForTwo
     * @return map information
     */
    private Map<Integer, Territory> createMapForTwo() {
        Map<Integer, Territory> result = new HashMap<>(5);
        Territory narnia = new Territory(0, "Narnia");
        Territory midkemia = new Territory(1, "Midkemia");

        //midkemia, elantris
        narnia.addNeighbor(1);
        //narnia, elantris, scadrial, oz
        midkemia.addNeighbor(0);

        result.put(narnia.getTerritoryId(), narnia);
        result.put(midkemia.getTerritoryId(), midkemia);

        return result;
    }


    @Override
    public int territoryNum() {
        return this.totalTerritoryNum;
    }

}
