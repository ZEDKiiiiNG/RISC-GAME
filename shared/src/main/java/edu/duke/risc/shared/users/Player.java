package edu.duke.risc.shared.users;

import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private Map<UnitType, Integer> unitsMap;

    /**
     * Owned territories.
     */
    private List<Territory> ownedTerritories;

    /**
     * Assigned Color
     */
    private UserColor color;

    /**
     * Player status
     */
    private PlayerStatus status;

    public Player(int userId, UserColor color) {
        this.userId = userId;
        this.color = color;
    }

    @Override
    public boolean isMaster() {
        return false;
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

    public Map<UnitType, Integer> getUnitsMap() {
        return unitsMap;
    }

    public List<Territory> getOwnedTerritories() {
        return ownedTerritories;
    }

    public UserColor getColor() {
        return color;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setOwnedTerritories(List<Territory> ownedTerritories) {
        this.ownedTerritories = ownedTerritories;
    }
}
