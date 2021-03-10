package edu.duke.risc.shared.board;

import edu.duke.risc.shared.users.Player;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author eason
 * @date 2021/3/9 23:58
 */
public class GameBoard implements Serializable {

    private Set<Territory> territories;

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

    @Override
    public String toString() {
        return "GameBoard{" +
                "territories=" + territories +
                ", players=" + players +
                ", gameStage=" + gameStage +
                '}';
    }
}
