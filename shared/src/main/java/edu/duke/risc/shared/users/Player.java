package edu.duke.risc.shared.users;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.commons.MissileType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.exceptions.InvalidInputException;
import edu.duke.risc.shared.util.MapHelper;
import edu.duke.risc.shared.util.TechHelper;

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
    private final int userId;

    /**
     * Total unitsMap, key for unit type, value for counts
     */
    private final Map<UnitType, Integer> totalUnitsMap;

    /**
     * Initial unitsMap, key for unit type, value for counts
     */
    private final Map<UnitType, Integer> initUnitsMap;

    /**
     * Spy map of the player, key for territory id and value for number of spies
     */
    private final Map<Integer, Integer> spiesMap;

    /**
     * Owned territories.
     */
    private Set<Integer> ownedTerritories;

    /**
     * Assigned Color
     */
    private final UserColor color;

    /**
     * Player status
     */
    private PlayerStatus status;

    /**
     * Whether cloaking is researched
     */
    private boolean cloakingResearched = false;

    /**
     * The missiles that this player owns
     */
    private final Map<MissileType, Integer> missiles;

    /**
     * The resources that this player owns
     */
    private final Map<ResourceType, Integer> resources;

    /**
     * The technology level that this player owns
     */
    private int technology = Configurations.DEFAULT_TECHNOLOGY_LEVEL;

    /**
     * The virtual technology level means that player wants to upgrade
     */
    private int virtualTechnology = Configurations.DEFAULT_TECHNOLOGY_LEVEL;

    /**
     * Constructor
     *
     * @param userId id of the user
     * @param color  color of the user
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
        this.spiesMap = new HashMap<>();

        //initialize missiles
        this.missiles = new HashMap<>();
        missiles.put(MissileType.MISSILE_LV1, 1);

        //initialize resources
        this.resources = new HashMap<>();
        resources.put(ResourceType.FOOD, Configurations.DEFAULT_FOOD_RESOURCE);
        resources.put(ResourceType.TECH, Configurations.DEFAULT_TECH_RESOURCE);
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


    /**
     * Get player information: units owned, tech, resources
     *
     * @return player information in the string format
     */
    public String getPlayerInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("-------------").append(System.lineSeparator());

        //color info
        builder.append("You are the ").append(this.color).append(" player").append(System.lineSeparator());

        //tech, resources
        builder.append("You have ");
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            builder.append(entry.getValue()).append(" ").append(entry.getKey()).append(", ");
        }
        builder.append(System.lineSeparator());
        for (Map.Entry<MissileType, Integer> entry : missiles.entrySet()) {
            builder.append(entry.getValue()).append(" ").append(entry.getKey()).append(", ");
        }
        builder.append(System.lineSeparator());
        builder.append(" and is currently in tech level ").append(this.technology);
        if (virtualTechnology != technology) {
            builder.append(" -> (").append(virtualTechnology).append(")");
        }
        builder.append(System.lineSeparator());

        //print total units
        builder.append("You have in total: ").append(System.lineSeparator());
        for (Map.Entry<UnitType, Integer> unitTypeIntegerEntry : this.totalUnitsMap.entrySet()) {
            builder.append(unitTypeIntegerEntry.getKey()).append(" : ")
                    .append(unitTypeIntegerEntry.getValue())
                    .append(System.lineSeparator());
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
     *
     * @return user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * getTotalUnitsMap
     *
     * @return getTotalUnitsMap
     */
    public Map<UnitType, Integer> getTotalUnitsMap() {
        return totalUnitsMap;
    }

    /**
     * updateInitUnitMap
     *
     * @param unitType unitType
     * @param diff     either add or subtract
     */
    public void updateInitUnitMap(UnitType unitType, Integer diff) {
        MapHelper.updateMap(this.initUnitsMap, unitType, diff);
    }

    /**
     * updateInitUnitMap
     *
     * @param territoryId territoryId
     * @param diff     either add or subtract
     */
    public void updateSpiesMap(Integer territoryId, Integer diff) {
        MapHelper.updateMap(this.spiesMap, territoryId, diff);
    }

    /**
     * updateTotalUnitMap
     *
     * @param unitType unitType
     * @param diff     diff
     */
    public void updateTotalUnitMap(UnitType unitType, Integer diff) {
        MapHelper.updateMap(this.totalUnitsMap, unitType, diff);
    }

    /**
     * updateTotalUnitMap
     *
     * @param resourceType unitType
     * @param diff         diff
     */
    public void updateResourceMap(ResourceType resourceType, Integer diff) {
        MapHelper.updateMap(this.resources, resourceType, diff);
    }

    /**
     * Whether this user owns this territory
     *
     * @param territoryId territory id
     * @return Whether this user owns this territory
     */
    public boolean ownsTerritory(Integer territoryId) {
        return this.ownedTerritories.contains(territoryId);
    }

    /**
     * removeOwnedTerritory
     *
     * @param territoryId territory id
     */
    public void removeOwnedTerritory(Integer territoryId) {
        this.ownedTerritories.remove(territoryId);
    }

    /**
     * addOwnedTerritory
     *
     * @param territoryId territory id
     */
    public void addOwnedTerritory(Integer territoryId) {
        this.ownedTerritories.add(territoryId);
    }

    /**
     * getOwnedTerritories
     *
     * @return getOwnedTerritories
     */
    public Set<Integer> getOwnedTerritories() {
        return ownedTerritories;
    }

    /**
     * Get user color
     *
     * @return user's color
     */
    public UserColor getColor() {
        return color;
    }

    /**
     * setOwnedTerritories
     *
     * @param ownedTerritories setOwnedTerritories
     */
    public void setOwnedTerritories(Set<Integer> ownedTerritories) {
        this.ownedTerritories = ownedTerritories;
    }

    /**
     * getInitUnitsMap
     *
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
     *
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
     *
     * @return whether this player wins
     */
    public boolean isWin() {
        return this.status == PlayerStatus.WIN;
    }

    /**
     * Whether the current players owns enough resources to make move/upgrade/attack action
     *
     * @param required resources required
     * @return boolean
     */
    public String hasEnoughResources(ResourceType resourceType, int required) {
        if (resources.containsKey(resourceType) && resources.get(resourceType) >= required) {
            return null;
        } else {
            return "The player does not have enough " + resourceType + " resources: "
                    + getResources(resourceType) + " < " + required;
        }
    }

    /**
     * Use certain number of resources
     *
     * @param used resources used
     */
    public void useResources(ResourceType resourceType, int used) {
        assert hasEnoughResources(resourceType, used) == null;
        this.resources.put(resourceType, resources.get(resourceType) - used);
    }

    /**
     * hasEnoughTechLevel
     *
     * @param requiredTech requiredTech
     * @return hasEnoughTechLevel
     */
    public String hasEnoughTechLevel(int requiredTech) {
        if (this.technology >= requiredTech) {
            return null;
        } else {
            return "Not enough tech level: " + technology + " < " + requiredTech;
        }
    }

    /**
     * getResources
     *
     * @return getResources
     */
    public int getResources(ResourceType resourceType) {
        return resources.get(resourceType);
    }

    /**
     * hasEnoughResourcesForTechUpgrade
     *
     * @return null if success, error message if failed
     */
    public String hasEnoughResourcesForTechUpgrade() {
        try {
            Map<ResourceType, Integer> required = TechHelper.getRequiredForTechUpgrade(technology);
            for (Map.Entry<ResourceType, Integer> entry : required.entrySet()) {
                String error;
                if ((error = hasEnoughResources(entry.getKey(), entry.getValue())) != null) {
                    return error;
                }
            }
            return null;
        } catch (InvalidInputException e) {
            return e.getMessage();
        }
    }

    /**
     * @param isReal client-side set to false, server-side set to true
     * @return null if success, error message if failed
     */
    public String upgradeTechLevel(boolean isReal) {
        assert hasEnoughResourcesForTechUpgrade() == null;
        int nextTech = TechHelper.getNextTechLevel(technology);

        Map<ResourceType, Integer> required = null;
        try {
            required = TechHelper.getRequiredForTechUpgrade(technology);
        } catch (InvalidInputException e) {
            return e.getMessage();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("UPGRADE TECH ACTION { ")
                .append(" conducted by player ").append(userId)
                .append(", from tech ").append(technology)
                .append(", to tech ").append(nextTech).append(", used ");

        //upgrade action
        if (isReal) {
            this.technology = nextTech;
        }
        this.virtualTechnology = nextTech;

        //gain missile for the tech
        try {
            MissileType gainMissile = MissileType.getMissileTypeWithTechLevel(nextTech);
            MapHelper.updateMap(missiles, gainMissile, 1);
        } catch (InvalidInputException e) {
            e.printStackTrace();
        }


        for (Map.Entry<ResourceType, Integer> entry : required.entrySet()) {
            useResources(entry.getKey(), entry.getValue());
            builder.append(entry.getValue()).append(" ").append(entry.getKey());
        }
        builder.append(" }").append(System.lineSeparator());
        return builder.toString();
    }

    /**
     * Whether the current player is at the top tech level
     *
     * @return Whether the current player is at the top tech level
     */
    public boolean isAtTopLevel() {
        return this.technology == TechHelper.getTopTechLevel();
    }

    /**
     * isAlreadyUpgradeTechInTurn
     *
     * @return isAlreadyUpgradeTechInTurn
     */
    public boolean isAlreadyUpgradeTechInTurn() {
        return this.virtualTechnology != this.technology;
    }

    /**
     * Obtain missiles for the player
     *
     * @param missileType missileType
     * @param amount      amount of the missile
     */
    public void obtainMissile(MissileType missileType, int amount) {
        MapHelper.updateMap(this.missiles, missileType, amount);
    }

    /**
     * Use missiles for the player
     *
     * @param missileType missileType
     * @param amount      amount of the missile
     */
    public void useMissiles(MissileType missileType, int amount) {
        MapHelper.updateMap(this.missiles, missileType, -amount);
    }

    /**
     * Whether the player has enough missiles
     *
     * @param missileType missileType
     * @param required    required amount of the missile
     */
    public boolean hasEnoughMissiles(MissileType missileType, int required) {
        return missiles.containsKey(missileType) && missiles.get(missileType) >= required;
    }

    /**
     * getMissiles
     *
     * @return getMissiles
     */
    public Map<MissileType, Integer> getMissiles() {
        return missiles;
    }

    /**
     * Whether cloaking is researched
     *
     * @return Whether cloaking is researched
     */
    public boolean isCloakingResearched() {
        return cloakingResearched;
    }

    /**
     * Do research on Cloaking
     */
    public String doResearchCloaking() {
        this.cloakingResearched = true;
        return "RESEARCH CLOAKING ACTION { conducted by player " + userId + " }";
    }

    /**
     *
     * @return the player's tech level
     */
    public int getTechnology() {
        return technology;
    }



}
