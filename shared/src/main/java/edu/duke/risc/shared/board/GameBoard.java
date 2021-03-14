package edu.duke.risc.shared.board;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.users.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @author eason
 * @date 2021/3/9 23:58
 */
public class GameBoard implements Serializable {

    private Map<Integer, Territory> territories;

    private Map<Integer, Player> players;

    private GameStage gameStage;

    private TerritoryFactory territoryFactory;

    private final Map<String, UnitType> unitTypeMapper = new HashMap<>();

    private Displayable displayer;

    public GameBoard() {
        territoryFactory = new BasicTerritoryFactory();
        territories = territoryFactory.makeTerritories();
        players = new HashMap<>();
        gameStage = GameStage.WAITING_USERS;
        displayer = new TextDisplayer();
        this.initUnitTypeMapping();
    }

    public void displayBoard() {
        displayer.display(this);
    }

    /**
     * Add player to this board, returns set of territoryId that should be assigned to this player
     *
     * @param player new player
     * @return Set<territoriesId></territoriesId> that should be assigned to this player
     */
    public Set<Integer> addPlayer(Player player) {
        int numberPlayers = this.players.size();
        int territoryPerPlayer = this.territoryFactory.territoryNum() / Configurations.MAX_PLAYERS;
        Set<Integer> result = new HashSet<>();
        for (int i = numberPlayers * territoryPerPlayer; i < (numberPlayers + 1) * territoryPerPlayer; i++) {
            result.add(i);
        }
        this.players.put(player.getUserId(), player);
        return result;
    }

    /**
     * @return
     */
    public String getPlayerAssignedTerritoryInfo(Integer playerId) {
        StringBuilder builder = new StringBuilder();
        Player player = this.players.get(playerId);
        for (Integer territoryId : player.getOwnedTerritories()) {
            Territory territory = this.territories.get(territoryId);
            builder.append(territory.getTerritoryName() + " (" + territory.getTerritoryId() + ") ");
        }
        return builder.toString();
    }

    public Territory findTerritory(Integer territoryId) {
        return this.territories.get(territoryId);
    }

    public Player findPlayer(Integer playerId) {
        return this.players.get(playerId);
    }

    @Override
    public String toString() {
        return "GameBoard{" +
                ", players=" + players +
                ", gameStage=" + gameStage +
                '}';
    }

    private void initUnitTypeMapping() {
        this.unitTypeMapper.put("s", UnitType.SOLDIER);
        this.unitTypeMapper.put("S", UnitType.SOLDIER);
    }

    /**
     * Grow the territory by 1.
     *
     * @return action log
     */
    public String territoryGrow() {
        StringBuilder builder = new StringBuilder();
        for (Player player : players.values()) {
            for (Integer territoryId : player.getOwnedTerritories()) {
                Territory territory = this.findTerritory(territoryId);
                UnitType unitType = UnitType.SOLDIER;
                territory.updateUnitsMap(unitType, 1);
                player.updateTotalUnitMap(unitType, 1);
            }
        }
        builder.append("Increment territories by 1");
        return builder.toString();
    }

    /**
     * @param playerId
     * @param sourceTerritoryId
     * @param unitType
     * @param number
     */
    public void playerMoveFromTerritory(int playerId, int sourceTerritoryId, UnitType unitType, int number) {
        Territory sourceTerritory = this.getTerritories().get(sourceTerritoryId);
        sourceTerritory.updateUnitsMap(unitType, -number);
    }

    /**
     * Get the total size of territories
     *
     * @return total size of territories
     */
    public int getTerritoriesSize() {
        return this.territories.size();
    }

    /**
     * Whether we can reach from source to the destination.
     * For places that are owned by the current player or empty place, we accept.
     * We are able to reach first adjacent enemy's territory
     *
     * @param sourceId sourceId
     * @param destId   destId
     * @param playerId playerId
     * @return Whether we can reach from source to the destination.
     */
    public boolean isReachable(int sourceId, int destId, int playerId) {
        Stack<Integer> stack = new Stack<>();
        Set<Integer> visited = new HashSet<>();
        Player player = this.findPlayer(playerId);

        if (destId == sourceId) {
            return true;
        }
        visited.add(sourceId);
        stack.push(sourceId);
        while (!stack.isEmpty()) {
            Integer current = stack.pop();
            Territory currentTerritory = this.findTerritory(current);
            for (Integer neighbor : currentTerritory.getAdjacentTerritories()) {
                if (destId == neighbor) {
                    return true;
                }
                //area not visited and ( or territory is empty -- not owned by anyone)
                if (!visited.contains(neighbor) && player.ownsTerritory(neighbor)) {
                    visited.add(neighbor);
                    stack.push(neighbor);
                }
            }
        }
        return false;
    }

    public int getShouldWaitPlayers() {
        int count = 0;
        for (Map.Entry<Integer, Player> playerEntry : this.players.entrySet()) {
            if (!playerEntry.getValue().isLost()) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * Get the player information
     *
     * @param playerId
     * @return
     */
    public String getPlayerInfo(int playerId) {
        StringBuilder builder = new StringBuilder();
        builder.append("-------------").append(System.lineSeparator());
        Player player = this.findPlayer(playerId);

        //print total units
        builder.append("You have in total: ").append(System.lineSeparator());
        for (Map.Entry<UnitType, Integer> unitTypeIntegerEntry : player.getTotalUnitsMap().entrySet()) {
            builder.append(unitTypeIntegerEntry.getKey()).append(" : ")
                    .append(unitTypeIntegerEntry.getValue())
                    .append(System.lineSeparator());
        }

        return builder.toString();
    }

    public void setGameStart() {
        this.gameStage = GameStage.GAME_START;
    }

    public void setGameOver() {
        this.gameStage = GameStage.GAME_OVER;
    }

    public boolean isGameOver() {
        return this.gameStage == GameStage.GAME_OVER;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void forwardPlacementPhase() {
        this.gameStage = GameStage.PLACEMENT;
    }

    public Map<Integer, Territory> getTerritories() {
        return territories;
    }

    public Map<String, UnitType> getUnitTypeMapper() {
        return unitTypeMapper;
    }
}
