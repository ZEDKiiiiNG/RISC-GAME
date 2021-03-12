package edu.duke.risc.shared.users;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;

import java.io.Serializable;
import java.util.HashMap;
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

    public Player(int userId, UserColor color) {
        //init units map
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
    public String getUnitsInfo() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<UnitType, Integer> entry : this.initUnitsMap.entrySet()) {
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
}
