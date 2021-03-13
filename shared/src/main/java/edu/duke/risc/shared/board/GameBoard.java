package edu.duke.risc.shared.board;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.users.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author eason
 * @date 2021/3/9 23:58
 */
public class GameBoard implements Serializable {

    private Map<Integer, Territory> territories;

    private Map<Integer, Player> players;

    private GameStage gameStage;

    private TerritoryFactory territoryFactory;

    private Displayable displayer;

    public GameBoard() {
        territoryFactory = new BasicTerritoryFactory();
        territories = territoryFactory.makeTerritories();
        players = new HashMap<>();
        gameStage = GameStage.WAITING_USERS;
        displayer = new TextDisplayer();
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

    @Override
    public String toString() {
        return "GameBoard{" +
                ", players=" + players +
                ", gameStage=" + gameStage +
                '}';
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
}
