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

    /**
     * Constructor
     * @param userId id of the user
     * @param color color of the user
     */
    public Player(int userId, UserColor color) {
        //init units map
        this.ownedTerritories = new HashSet<>();
        this.initUnitsMap = new HashMap<>();
        this.status = PlayerStatus.IN_GAME;
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

    /**
     * Get the user id
     * @return user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * getTotalUnitsMap
     * @return getTotalUnitsMap
     */
    public Map<UnitType, Integer> getTotalUnitsMap() {
        return totalUnitsMap;
    }

    /**
     * updateInitUnitMap
     *
     * @param unitType unitType
     * @param diff either add or subtract
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
     * Whether this user owns this territory
     * @param territoryId territory id
     * @return Whether this user owns this territory
     */
    public boolean ownsTerritory(Integer territoryId) {
        return this.ownedTerritories.contains(territoryId);
    }

    /**
     * updateUnitsMap
     * @param unitsMap unitsMap
     * @param unitType unitType
     * @param diff either add or subtract
     */
    private void updateUnitsMap(Map<UnitType, Integer> unitsMap, UnitType unitType, Integer diff) {
        assert unitsMap != null;
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
     * removeOwnedTerritory
     * @param territoryId territory id
     */
    public void removeOwnedTerritory(Integer territoryId) {
        this.ownedTerritories.remove(territoryId);
    }

    /**
     * addOwnedTerritory
     * @param territoryId territory id
     */
    public void addOwnedTerritory(Integer territoryId) {
        this.ownedTerritories.add(territoryId);
    }

    /**
     * getOwnedTerritories
     * @return getOwnedTerritories
     */
    public Set<Integer> getOwnedTerritories() {
        return ownedTerritories;
    }

    /**
     * Get user color
     * @return user's color
     */
    public UserColor getColor() {
        return color;
    }

    /**
     * setOwnedTerritories
     * @param ownedTerritories setOwnedTerritories
     */
    public void setOwnedTerritories(Set<Integer> ownedTerritories) {
        this.ownedTerritories = ownedTerritories;
    }

    /**
     * getInitUnitsMap
     * @return getInitUnitsMap
     */
    public Map<UnitType, Integer> getInitUnitsMap() {
        return initUnitsMap;
    }

    /**
     * Mark this player to be lost
     */
    public void markLost() {
        this.status = PlayerStatus.LOST;
    }

    /**
     * Judge whether this player lost
     * @return whether this player lost
     */
    public boolean isLost() {
        return this.status == PlayerStatus.LOST;
    }

    /**
     * Mark this player to win
     */
    public void markWin() {
        this.status = PlayerStatus.WIN;
    }

    /**
     * Judge whether this player wins
     * @return whether this player wins
     */
    public boolean isWin() {
        return this.status == PlayerStatus.WIN;
    }
}
