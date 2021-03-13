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
        for (Integer territoryId : player.getInitAssignedTerritories()) {
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
     * Whether we can reach from source to the destination.
     * For places that are owned by the current player or empty place, we accept.
     *
     * @param sourceId sourceId
     * @param destId   destId
     * @param playerId playerId
     * @return Whether we can reach from source to the destination.
     */
    public boolean isReachable(int sourceId, int destId, int playerId) {
        Stack<Territory> stack = new Stack<>();
        Set<Territory> visited = new HashSet<>();
        Territory source = this.territories.get(sourceId);
        Territory dest = this.territories.get(destId);
        Player player = this.findPlayer(playerId);

        visited.add(source);
        stack.push(source);
        while (!stack.isEmpty()) {
            Territory current = stack.pop();
            if (dest.equals(source)) {
                return true;
            }
            for (Territory neighbor : current.getAdjacentTerritories()) {
                //area not visited and ( or territory is empty -- not owned by anyone)
                if (!visited.contains(neighbor)
                        && (player.ownsTerritory(neighbor.getTerritoryId()) || neighbor.isEmptyTerritory())) {
                    visited.add(neighbor);
                    stack.push(neighbor);
                }
            }
        }
        return false;
    }

    public UnitType getUnitType(String search) {
        return this.unitTypeMapper.get(search);
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public void forwardPlacementPhase() {
        this.gameStage = GameStage.PLACEMENT;
    }

    public Map<Integer, Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(Map<Integer, Territory> territories) {
        this.territories = territories;
    }

    public void setPlayers(Map<Integer, Player> players) {
        this.players = players;
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    public void setTerritoryFactory(TerritoryFactory territoryFactory) {
        this.territoryFactory = territoryFactory;
    }

    public void setDisplayer(Displayable displayer) {
        this.displayer = displayer;
    }

    public TerritoryFactory getTerritoryFactory() {
        return territoryFactory;
    }

    public Displayable getDisplayer() {
        return displayer;
    }

    public Map<String, UnitType> getUnitTypeMapper() {
        return unitTypeMapper;
    }
}
