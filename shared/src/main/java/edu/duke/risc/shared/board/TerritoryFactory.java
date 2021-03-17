package edu.duke.risc.shared.board;

import edu.duke.risc.shared.exceptions.InvalidInputException;

import java.io.Serializable;
import java.util.Map;

/**
 * @author eason
 * @date 2021/3/10 20:17
 */
public interface TerritoryFactory extends Serializable {

    /**
     * makeTerritories as a connected undirected graph.
     *
     * @param playerNum number of players
     * @return set of territories
     */
    public Map<Integer, Territory> makeTerritories(int playerNum) ;

    /**
     * get number of territories this factory make
     *
     * @return number of territories this factory make
     */
    public int territoryNum();

}
