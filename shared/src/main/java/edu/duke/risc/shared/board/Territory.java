package edu.duke.risc.shared.board;

import edu.duke.risc.shared.commons.UnitType;

import java.io.Serializable;
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
    private int territoryName;

    /**
     * Total unitsMap, key for unit type, value for counts
     */
    private Map<UnitType, Integer> unitsMap;

    /**
     * Adjacent territories
     */
    private Set<Territory> adjacentTerritories;


    public Territory(int territoryId, Set<Territory> adjacentTerritories) {
        this.territoryId = territoryId;
        this.adjacentTerritories = adjacentTerritories;
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

}
