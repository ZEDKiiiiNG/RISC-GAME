package edu.duke.risc.shared.board;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.users.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author eason
 * @date 2021/3/9 23:58
 */
public class GameBoard implements Serializable {

    private List<Territory> territories;

    private Set<Player> players;

    private GameStage gameStage;

    private TerritoryFactory territoryFactory;

    private Displayable displayer;

    public GameBoard() {
        territoryFactory = new BasicTerritoryFactory();
        territories = territoryFactory.makeTerritories();
        players = new HashSet<>();
        gameStage = GameStage.GAME_START;
        displayer = new TextDisplayer();
    }

    public void displayBoard() {
        displayer.display(this);
    }

    /**
     * Add player to this board, returns territories that should be assigned to this player
     *
     * @param player new player
     * @return territories that should be assigned to this player
     */
    public List<Territory> addPlayer(Player player) {
        int numberPlayers = this.players.size();
        int territoryPerPlayer = this.territoryFactory.territoryNum() / Configurations.MAX_PLAYERS;
        this.players.add(player);
        return new ArrayList<>(
                this.territories.subList(numberPlayers * territoryPerPlayer, (numberPlayers + 1) * territoryPerPlayer));
    }

    @Override
    public String toString() {
        return "GameBoard{" +
                ", players=" + players +
                ", gameStage=" + gameStage +
                '}';
    }

    public Set<Player> getPlayers() {
        return players;
    }

}
