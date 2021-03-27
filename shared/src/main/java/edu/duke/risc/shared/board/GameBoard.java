package edu.duke.risc.shared.board;

import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.users.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Represents the game board, including all players, territories, game stage.
 *
 * @author eason
 * @date 2021/3/9 23:58
 */
public class GameBoard implements Serializable {

    /**
     * All territories
     */
    private final Map<Integer, Territory> territories;

    /**
     * All players
     */
    private final Map<Integer, Player> players;

    /**
     * The game stage
     */
    private GameStage gameStage;

    /**
     * Factory used to produce the whole map
     */
    private final TerritoryFactory territoryFactory;

    /**
     * unit type mapper
     */
    private final Map<String, UnitType> unitTypeMapper = UnitType.getUnitTypeMapper();

    /**
     * The board displayer
     */
    private final Displayable displayer;

    /**
     * max player, assigned at the beginning
     */
    private final int maxPlayer;

    /**
     * The max number of players, the map will be generated according to this value
     *
     * @param playerNum max number of players
     */
    public GameBoard(int playerNum) {
        this.maxPlayer = playerNum;
        territoryFactory = new WorldMapTerritoryFactory();
        territories = territoryFactory.makeTerritories(this.maxPlayer);
        players = new HashMap<>();
        gameStage = GameStage.WAITING_USERS;
        displayer = new TextDisplayer();
    }

    /**
     * Display the board to the standard output
     */
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
        int territoryPerPlayer = this.territoryFactory.territoryNum() / maxPlayer;
        Set<Integer> result = new HashSet<>();
        for (int i = numberPlayers * territoryPerPlayer; i < (numberPlayers + 1) * territoryPerPlayer; i++) {
            result.add(i);
        }
        this.players.put(player.getUserId(), player);
        return result;
    }

    /**
     * Returns the assigned territories of the player, in the string format.
     *
     * @param playerId id of the player
     * @return string representation of the assigned territories
     */
    public String getPlayerAssignedTerritoryInfo(Integer playerId) {
        StringBuilder builder = new StringBuilder();
        Player player = this.players.get(playerId);
        for (Integer territoryId : player.getOwnedTerritories()) {
            Territory territory = this.territories.get(territoryId);
            builder.append(territory.getTerritoryName()).append(" (").append(territory.getTerritoryId()).append(") ");
        }
        return builder.toString();
    }

    /**
     * Find territory based on id
     *
     * @param territoryId territory id
     * @return territory object, null if not exists
     */
    public Territory findTerritory(Integer territoryId) {
        return this.territories.get(territoryId);
    }

    /**
     * Find player based on id
     *
     * @param playerId player id
     * @return player object, null if not exists
     */
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
                //increase resources of the player based on their territories
                for (Map.Entry<ResourceType, Integer> entry : territory.getProductivity().entrySet()) {
                    player.updateResourceMap(entry.getKey(), entry.getValue());
                }
            }
        }
        builder.append("Increment units and resources at the end of the turn");
        return builder.toString();
    }

    /**
     * Pretend to move certain number of units from one place to another, only for the client-side pre-check.
     *
     * @param playerId          id of the player
     * @param sourceTerritoryId id of the source territory
     * @param unitType          unit type
     * @param number            number of the units
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
     * Note: Integer.MAX_VALUE distance means that destination is unreachable
     *
     * @param sourceId sourceId
     * @param destId   destId
     * @param playerId playerId
     * @return Whether we can reach from source to the destination.
     */
    public int calculateMoveCost(int sourceId, int destId, int playerId) {
        //initialization
        int totalNumber = territoryFactory.territoryNum();
        List<Integer> shortestPath = new ArrayList<>();
        for (int i = 0; i < totalNumber; i++) {
            shortestPath.add(Integer.MAX_VALUE);
        }
        PriorityQueue<Integer> queue = new PriorityQueue<>((o1, o2) -> shortestPath.get(o2) - shortestPath.get(o1));
        Set<Integer> visited = new HashSet<>();
        Player player = this.findPlayer(playerId);

        //before searching
        if (destId == sourceId) {
            return 0;
        }
        visited.add(sourceId);
        queue.add(sourceId);
        shortestPath.set(sourceId, 0);

        //start searching
        while (!queue.isEmpty()) {
            Integer current = queue.remove();
            Territory currentTerritory = findTerritory(current);
            for (Integer neighbor : currentTerritory.getAdjacentTerritories()) {
                if (neighbor == destId) {
                    shortestPath.set(neighbor, shortestPath.get(neighbor) == Integer.MAX_VALUE ?
                            currentTerritory.getSize() : currentTerritory.getSize() + shortestPath.get(neighbor));
                    return shortestPath.get(destId);
                }
                //area not visited and ( or territory is empty -- not owned by anyone)
                if (!visited.contains(neighbor) && findTerritory(neighbor).isValid()
                        && player.ownsTerritory(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    shortestPath.set(neighbor, shortestPath.get(neighbor) == Integer.MAX_VALUE ?
                            currentTerritory.getSize() : currentTerritory.getSize() + shortestPath.get(neighbor));
                }
            }
        }
        return shortestPath.get(destId);
    }

    /**
     * Get how many players should the current server wait
     *
     * @return number of players that the current server should wait
     */
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
     * @param playerId player id
     * @return player information in the string format
     */
    public String getPlayerInfo(int playerId) {
        return this.findPlayer(playerId).getPlayerInfo();
    }

    /**
     * Get the winner
     *
     * @return winner object, null if not winner
     */
    public Player getWinner() {
        for (Player player : players.values()) {
            if (player.isWin()) {
                return player;
            }
        }
        return null;
    }

    /**
     * setGameStart
     */
    public void setGameStart() {
        this.gameStage = GameStage.GAME_START;
    }

    /**
     * setGameOver
     */
    public void setGameOver() {
        this.gameStage = GameStage.GAME_OVER;
    }

    /**
     * isGameOver
     *
     * @return isGameOver
     */
    public boolean isGameOver() {
        return this.gameStage == GameStage.GAME_OVER;
    }

    /**
     * Get the map the players
     *
     * @return player map
     */
    public Map<Integer, Player> getPlayers() {
        return players;
    }

    /**
     * Set the game stage into placement
     */
    public void forwardPlacementPhase() {
        this.gameStage = GameStage.PLACEMENT;
    }

    /**
     * getTerritories
     *
     * @return getTerritories
     */
    public Map<Integer, Territory> getTerritories() {
        return territories;
    }

    /**
     * getUnitTypeMapper
     *
     * @return getUnitTypeMapper
     */
    public Map<String, UnitType> getUnitTypeMapper() {
        return unitTypeMapper;
    }

}
