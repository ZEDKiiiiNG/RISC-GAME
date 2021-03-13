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
        return "Territory{" +
                "territoryId=" + territoryId +
                ", territoryName='" + territoryName + '\'' +
                " units map size=" + unitsMap.size() +
                '}';
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

    public void updateUnitsMap(UnitType unit_type, Integer safe_num) {
        if (unitsMap.get(unit_type) + safe_num == 0) {
            unitsMap.remove(unit_type, -safe_num);
            return;
        }
        if (unitsMap.containsKey(unit_type)) {
            Integer temp = unitsMap.get(unit_type);
            unitsMap.replace(unit_type, temp, temp + safe_num);
        } else {
            unitsMap.put(unit_type, safe_num);
        }

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
