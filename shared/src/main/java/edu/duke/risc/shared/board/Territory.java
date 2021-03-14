package edu.duke.risc.shared.board;

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
    private int territoryId;

    /**
     * Territory Name
     */
    private String territoryName;

    /**
     * Total unitsMap, key for unit type, value for counts
     */
    private Map<UnitType, Integer> unitsMap;

    /**
     * virtual unitsMap, only for client simulation of attack
     */
    private Map<UnitType, Integer> virtualUnitsMap;

    /**
     * Adjacent territories
     */
    private Set<Integer> adjacentTerritories;

    public Territory(int territoryId, String territoryName) {
        this(territoryId, territoryName, new HashMap<>(), new HashSet<>(), new HashMap<>());
    }

    private Territory(int territoryId, String territoryName, Map<UnitType, Integer> unitsMap,
                      Set<Integer> adjacentTerritories, Map<UnitType, Integer> virtualUnitsMap) {
        this.territoryId = territoryId;
        this.territoryName = territoryName;
        this.unitsMap = unitsMap;
        this.adjacentTerritories = adjacentTerritories;
        this.virtualUnitsMap = virtualUnitsMap;
    }

    public void addNeighbor(Integer... territories) {
        this.adjacentTerritories.addAll(Arrays.asList(territories));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.isEmptyTerritory()) {
            builder.append("No Units ");
        } else {
            //real units
            for (Map.Entry<UnitType, Integer> mapUnit : this.unitsMap.entrySet()) {
                builder.append(mapUnit.getValue()).append(" ").append(mapUnit.getKey()).append(" ");
            }
        }
        builder.append("in ").append(this.territoryName)
                .append("(").append(this.territoryId).append(")").append(" (next to: ");
        for (Integer adjacent : this.adjacentTerritories) {
            builder.append(adjacent).append(", ");
        }
        builder.append(")");
        //virtual units for clients
        for (Map.Entry<UnitType, Integer> mapUnit : this.virtualUnitsMap.entrySet()) {
            builder.append("(Ready to attack units: ")
                    .append(mapUnit.getValue()).append(" ").append(mapUnit.getKey()).append(")");
        }
        return builder.toString();
    }

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

    public void setTerritoryId(int territoryId) {
        this.territoryId = territoryId;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public void setUnitsMap(Map<UnitType, Integer> unitsMap) {
        this.unitsMap = unitsMap;
    }


    public void updateVirtualUnitsMap(UnitType unitType, Integer diff) {
        this.generalUpdateUnitsMap(this.virtualUnitsMap, unitType, diff);
    }

    public void updateUnitsMap(UnitType unitType, Integer diff) {
        this.generalUpdateUnitsMap(this.unitsMap, unitType, diff);
    }

    /**
     * Update units map in this territory. If no more units like this, remove from map
     *
     * @param unitType
     * @param diff
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
     * @return
     */
    public boolean isEmptyTerritory() {
        return this.unitsMap.isEmpty();
    }

    public String getTerritoryName() {
        return territoryName;
    }

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

    public void setVirtualUnitsMap(Map<UnitType, Integer> virtualUnitsMap) {
        this.virtualUnitsMap = virtualUnitsMap;
    }
}
