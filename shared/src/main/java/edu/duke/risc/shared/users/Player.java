package edu.duke.risc.shared.users;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Player stands for players of the game.
 *
 * @author yichen.hua
 * @date 2021/3/9 20:01
 */
public class Player implements GameUser, Serializable {

    /**
     * Unique user identifier
     */
    private int userId;

    /**
     * Total unitsMap, key for unit type, value for counts
     */
    private Map<UnitType, Integer> totalUnitsMap;

    /**
     * Initial unitsMap, key for unit type, value for counts
     */
    private Map<UnitType, Integer> initUnitsMap;

    private Set<Integer> initAssignedTerritories;

    /**
     * Owned territories.
     */
    private Set<Integer> ownedTerritories;

    /**
     * Assigned Color
     */
    private UserColor color;

    /**
     * Player status
     */
    private PlayerStatus status;

    public Player(int userId, UserColor color) {
        //init units map
        this.ownedTerritories = new HashSet<>();
        this.initAssignedTerritories = new HashSet<>();
        this.initUnitsMap = new HashMap<>();
        initUnitsMap.put(UnitType.SOLDIER, Configurations.INIT_SOLDIER_NUM);
        this.userId = userId;
        this.color = color;
        this.totalUnitsMap = new HashMap<>();
    }

    /**
     * Get units information in string
     *
     * @return units info in string
     */
    public String getUnitsInfo(Map<UnitType, Integer> unitMap) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            builder.append(entry.getKey()).append(" : ").append(entry.getValue());
        }
        return builder.toString();
    }

    @Override
    public boolean isMaster() {
        return false;
    }

    @Override
    public int getId() {
        return this.userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return userId == player.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "Player{" +
                "userId=" + userId +
                ", color=" + color +
                ", status=" + status +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public Map<UnitType, Integer> getTotalUnitsMap() {
        return totalUnitsMap;
    }

    /**
     * updateInitUnitMap
     *
     * @param unitType
     * @param diff
     */
    public void updateInitUnitMap(UnitType unitType, Integer diff) {
        this.updateUnitsMap(this.initUnitsMap, unitType, diff);
    }

    /**
     * updateTotalUnitMap
     *
     * @param unitType
     * @param diff
     */
    public void updateTotalUnitMap(UnitType unitType, Integer diff) {
        this.updateUnitsMap(this.totalUnitsMap, unitType, diff);
    }

    /**
     * no throw here, like Territory
     */
    private void updateUnitsMap(Map<UnitType, Integer> unitsMap, UnitType unitType, Integer diff) {
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

    public void removeOwnedTerritory(Integer territoryId) {
        this.ownedTerritories.remove(territoryId);
    }

    public void addOwnedTerritory(Integer territoryId) {
        this.ownedTerritories.add(territoryId);
    }

    public Set<Integer> getOwnedTerritories() {
        return ownedTerritories;
    }

    public UserColor getColor() {
        return color;
    }

    public void setOwnedTerritories(Set<Integer> ownedTerritories) {
        this.ownedTerritories = ownedTerritories;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public Map<UnitType, Integer> getInitUnitsMap() {
        return initUnitsMap;
    }

    public Set<Integer> getInitAssignedTerritories() {
        return initAssignedTerritories;
    }

    public void setInitAssignedTerritories(Set<Integer> initAssignedTerritories) {
        this.initAssignedTerritories = initAssignedTerritories;
    }
}
