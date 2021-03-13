package edu.duke.risc.shared.board;

import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.users.Player;

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
     * Adjacent territories
     */
    private Set<Territory> adjacentTerritories;

    public Territory(int territoryId, String territoryName) {
        this(territoryId, territoryName, new HashMap<>(), new HashSet<>());
    }

    public Territory(int territoryId, String territoryName, Map<UnitType, Integer> unitsMap, Set<Territory> adjacentTerritories) {
        this.territoryId = territoryId;
        this.territoryName = territoryName;
        this.unitsMap = unitsMap;
        this.adjacentTerritories = adjacentTerritories;
    }

    public void addNeighbor(Territory... territories) {
        this.adjacentTerritories.addAll(Arrays.asList(territories));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.isEmptyTerritory()) {
            builder.append("No Units ");
        } else {
            for (Map.Entry<UnitType, Integer> mapUnit : this.unitsMap.entrySet()) {
                builder.append(mapUnit.getValue()).append(" ").append(mapUnit.getKey()).append(" ");
            }
        }
        builder.append("in ").append(this.territoryName).append(" (next to: ");
        for (Territory adjacent :this.adjacentTerritories) {
            builder.append(adjacent.getTerritoryName()).append(", ");
        }
        builder.append(")");
        return builder.toString();
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

    /**
     * Update units map in this territory. If no more units like this, remove from map
     *
     * @param unitType
     * @param diff
     */
    public void updateUnitsMap(UnitType unitType, Integer diff) {
        assert unitsMap != null;
        if (unitsMap.containsKey(unitType)) {
            int originVal = unitsMap.get(unitType);
            if (diff >= 0) {
                unitsMap.put(unitType, diff + originVal);
            } else {
                if (originVal + diff <= 0){
                    unitsMap.remove(unitType);
                }else{
                    unitsMap.put(unitType, diff + originVal);
                }
            }
        } else {
            if (diff > 0) {
                unitsMap.put(unitType, diff);
            }
        }
    }

    //TODO
    public static boolean isReachable(Territory source, Territory destination, Player player) {
        return false;
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

    public void setAdjacentTerritories(Set<Territory> adjacentTerritories) {
        this.adjacentTerritories = adjacentTerritories;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public Map<UnitType, Integer> getUnitsMap() {
        return unitsMap;
    }

    public Set<Territory> getAdjacentTerritories() {
        return adjacentTerritories;
    }

    public int getTerritoryId() {
        return territoryId;
    }
}
