package edu.duke.risc.shared.board;

import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Stands for Territory on the game board
 *
 * @author eason
 * @date 2021/3/9 20:14
 */
public class Territory implements Serializable {

    /**
     * Unique Territory identifier
     */
    private final int territoryId;

    /**
     * Territory Name
     */
    private final String territoryName;

    /**
     * Total unitsMap, key for unit type, value for counts
     */
    private final Map<UnitType, Integer> unitsMap;

    /**
     * virtual unitsMap, only for client simulation of attack
     */
    private final Map<UnitType, Integer> virtualUnitsMap;

    /**
     * Adjacent territories
     */
    private final Set<Integer> adjacentTerritories;

    /**
     * We created a big world map, factory will invalidate some of them depends on number of players
     */
    private boolean isValid;

    /**
     * Number of resources this territory produces at the end of each turn
     */
    private final Map<ResourceType, Integer> productivity;

    /**
     * Cost of resources when moving units from this area
     */
    private final int size;

    /**
     * Constructor
     *
     * @param territoryId   territory id
     * @param territoryName territory name
     */
    public Territory(int territoryId, String territoryName) {
        this(territoryId, territoryName, 1, 1, 1);
    }

    /**
     * Constructor
     *
     * @param territoryId   territory id
     * @param territoryName territory name
     */
    public Territory(int territoryId, String territoryName, int techProd, int foodProd, int size) {
        this(territoryId, territoryName, new HashMap<>(), new HashSet<>(), new HashMap<>(),
                false, techProd, foodProd, size);
    }

    /**
     * Constructor
     *
     * @param territoryId         territory id
     * @param territoryName       territory name
     * @param unitsMap            units map
     * @param adjacentTerritories adjacentTerritories
     * @param virtualUnitsMap     virtualUnitsMap
     * @param isValid             is current territory valid or not
     */
    private Territory(int territoryId, String territoryName, Map<UnitType, Integer> unitsMap,
                      Set<Integer> adjacentTerritories, Map<UnitType, Integer> virtualUnitsMap,
                      boolean isValid, int techProd, int foodProd, int size) {
        this.territoryId = territoryId;
        this.territoryName = territoryName;
        this.unitsMap = unitsMap;
        this.adjacentTerritories = adjacentTerritories;
        this.virtualUnitsMap = virtualUnitsMap;
        this.isValid = isValid;
        this.productivity = new HashMap<>();
        productivity.put(ResourceType.FOOD, foodProd);
        productivity.put(ResourceType.TECH, techProd);
        this.size = size;
    }

    /**
     * Add neighbors to the territory, used in the factory
     *
     * @param territories territories
     */
    public void addNeighbor(Integer... territories) {
        this.adjacentTerritories.addAll(Arrays.asList(territories));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        //print territory info
        builder.append(territoryName).append("(").append(this.territoryId).append(")").append(System.lineSeparator());

        //print resources
        builder.append("    Productivity: ");
        for (Map.Entry<ResourceType, Integer> entry : productivity.entrySet()) {
            builder.append(entry.getValue()).append(" ").append(entry.getKey());
        }
        builder.append(System.lineSeparator());

        //print neighbors
        builder.append("    ").append(" (next to: ");
        for (Integer adjacent : this.adjacentTerritories) {
            builder.append(adjacent).append(", ");
        }
        builder.append(")").append(System.lineSeparator());

        //print units in that territory
        if (this.isEmptyTerritory()) {
            builder.append("No Units ");
        } else {
            //real units
            for (Map.Entry<UnitType, Integer> mapUnit : this.unitsMap.entrySet()) {
                builder.append(mapUnit.getValue()).append(" ").append(mapUnit.getKey()).append(" ");
            }
        }

        //virtual units for clients
        for (Map.Entry<UnitType, Integer> mapUnit : this.virtualUnitsMap.entrySet()) {
            builder.append("(Ready to attack units: ")
                    .append(mapUnit.getValue()).append(" ").append(mapUnit.getKey()).append(")");
        }
        return builder.toString();
    }

    /**
     * getBasicInfo
     *
     * @return basic territory information in the string format
     */
    public String getBasicInfo() {
        return this.territoryName + "(" + this.territoryId + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(territoryId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Territory territory = (Territory) o;
        return territoryId == territory.territoryId;
    }

    /**
     * Update virtual units map
     *
     * @param unitType unitType
     * @param diff     difference, -1 for subtract 1,
     */
    public void updateVirtualUnitsMap(UnitType unitType, Integer diff) {
        this.generalUpdateUnitsMap(this.virtualUnitsMap, unitType, diff);
    }

    /**
     * Update units map
     *
     * @param unitType unitType
     * @param diff     difference, -1 for subtract 1,
     */
    public void updateUnitsMap(UnitType unitType, Integer diff) {
        this.generalUpdateUnitsMap(this.unitsMap, unitType, diff);
    }

    /**
     * Update units map in this territory. If no more units like this, remove from map
     *
     * @param unitType unit type
     * @param diff     difference, -1 for subtract 1
     */
    private void generalUpdateUnitsMap(Map<UnitType, Integer> unitsMap, UnitType unitType, Integer diff) {
        if (unitsMap.containsKey(unitType)) {
            int originVal = unitsMap.get(unitType);
            if (diff >= 0) {
                unitsMap.put(unitType, diff + originVal);
            } else {
                if (originVal + diff <= 0) {
                    unitsMap.remove(unitType);
                } else {
                    unitsMap.put(unitType, diff + originVal);
                }
            }
        } else {
            if (diff > 0) {
                unitsMap.put(unitType, diff);
            }
        }
    }

    /**
     * A empty territory is the territory without units.
     * A empty territory should not be owned by anyone.
     *
     * @return whether is a empty territory
     */
    public boolean isEmptyTerritory() {
        return this.unitsMap.isEmpty();
    }

    /**
     * Name of the territory
     *
     * @return Name of the territory
     */
    public String getTerritoryName() {
        return territoryName;
    }

    /**
     * getUnitsMap
     *
     * @return units map
     */
    public Map<UnitType, Integer> getUnitsMap() {
        return unitsMap;
    }

    public Set<Integer> getAdjacentTerritories() {
        return adjacentTerritories;
    }

    public int getTerritoryId() {
        return territoryId;
    }

    public Map<UnitType, Integer> getVirtualUnitsMap() {
        return virtualUnitsMap;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isValid() {
        return isValid;
    }

    public int getSize() {
        return size;
    }

    public Map<ResourceType, Integer> getProductivity() {
        return productivity;
    }
}
