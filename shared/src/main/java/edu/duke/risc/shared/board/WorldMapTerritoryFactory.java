package edu.duke.risc.shared.board;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic factory to produce territories map
 *
 * @author eason
 * @date 2021/3/10 20:29
 */
public class WorldMapTerritoryFactory implements TerritoryFactory {

    /**
     * Total number of territories in the map
     */
    private int totalTerritoryNum;

    @Override
    public Map<Integer, Territory> makeTerritories(int playerNum) {
        switch (playerNum) {
            case 2:
                this.totalTerritoryNum = 4;
                return createMapForTwo();
            case 3:
                this.totalTerritoryNum = 6;
                return createMapForThree();
            case 4:
                this.totalTerritoryNum = 8;
                return createMapForFour();
            case 5:
                this.totalTerritoryNum = 10;
                return createMapForFive();
            default:
                System.out.println("Invalid number of players " + playerNum);
                return null;
        }
    }

    /**
     * @return world map
     */
    private Map<Integer, Territory> createWholeUsMap() {
        Map<Integer, Territory> result = new HashMap<>(20);
        Territory utah = new Territory(0, "Utah", 30, 5);
        Territory nevada = new Territory(1, "Nevada", 30, 5);
        Territory idaho = new Territory(2, "Idaho", 25, 4);
        Territory wyoming = new Territory(3, "Wyoming", 35, 6);
        Territory colorado = new Territory(4, "Colorado", 20, 3);
        Territory newMexico = new Territory(5, "New Mexico", 40, 7);
        Territory arizona = new Territory(6, "Arizona", 45, 8);
        Territory california = new Territory(7, "California", 15, 2);
        Territory oregon = new Territory(8, "Oregon", 10, 1);
        Territory washington = new Territory(9, "Washington", 50, 9);

        //Nevada, Idaho, Wyoming, Colorado, New Mexico, Arizona
        utah.addNeighbor(1, 2, 3, 4, 5, 6);
        //Utah, Idaho, Arizona, California, Oregon
        nevada.addNeighbor(0, 2, 6, 7, 8);
        //Utah, Nevada, Wyoming, Oregon, Washington
        idaho.addNeighbor(0, 1, 3, 8, 9);
        //Utah, Idaho, Colorado
        wyoming.addNeighbor(0, 2, 4);
        //Utah, Wyoming, New Mexico
        colorado.addNeighbor(0, 3, 5);
        //Colorado, Arizona
        newMexico.addNeighbor(4, 6);
        //Utah, Nevada, New Mexico, California
        arizona.addNeighbor(0, 1, 5, 7);
        //Nevada, Arizona, Oregon
        california.addNeighbor(1, 6, 8);
        //Nevada, Idaho, California, Washington
        oregon.addNeighbor(1, 2, 7, 9);
        //Idaho, Oregon
        washington.addNeighbor(2, 8);

        result.put(utah.getTerritoryId(), utah);
        result.put(nevada.getTerritoryId(), nevada);
        result.put(idaho.getTerritoryId(), idaho);
        result.put(wyoming.getTerritoryId(), wyoming);
        result.put(colorado.getTerritoryId(), colorado);
        result.put(newMexico.getTerritoryId(), newMexico);
        result.put(arizona.getTerritoryId(), arizona);
        result.put(california.getTerritoryId(), california);
        result.put(oregon.getTerritoryId(), oregon);
        result.put(washington.getTerritoryId(), washington);
        return result;
    }

    /**
     * createMapForFive
     *
     * @return map information
     */
    private Map<Integer, Territory> createMapForFive() {
        Map<Integer, Territory> result = createWholeUsMap();
        result.get(0).setValid(true);
        result.get(1).setValid(true);
        result.get(2).setValid(true);
        result.get(3).setValid(true);
        result.get(4).setValid(true);
        result.get(5).setValid(true);
        result.get(6).setValid(true);
        result.get(7).setValid(true);
        result.get(8).setValid(true);
        result.get(9).setValid(true);
        return result;
    }

    /**
     * createMapForFour
     *
     * @return map information
     */
    private Map<Integer, Territory> createMapForFour() {
        Map<Integer, Territory> result = createWholeUsMap();
        result.get(0).setValid(true);
        result.get(1).setValid(true);
        result.get(2).setValid(true);
        result.get(3).setValid(true);
        result.get(4).setValid(true);
        result.get(5).setValid(true);
        result.get(6).setValid(true);
        result.get(7).setValid(true);
        return result;
    }

    /**
     * createMapForThree
     *
     * @return map information
     */
    private Map<Integer, Territory> createMapForThree() {
        Map<Integer, Territory> result = createWholeUsMap();
        result.get(0).setValid(true);
        result.get(1).setValid(true);
        result.get(2).setValid(true);
        result.get(3).setValid(true);
        result.get(4).setValid(true);
        result.get(5).setValid(true);
        return result;
    }

    /**
     * createMapForTwo
     *
     * @return map information
     */
    private Map<Integer, Territory> createMapForTwo() {
        Map<Integer, Territory> result = createWholeUsMap();
        result.get(0).setValid(true);
        result.get(1).setValid(true);
        result.get(2).setValid(true);
        result.get(3).setValid(true);
        return result;
    }


    @Override
    public int territoryNum() {
        return this.totalTerritoryNum;
    }

}
