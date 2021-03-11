package edu.duke.risc.shared.board;

import java.io.Serializable;
import java.util.List;

/**
 * @author eason
 * @date 2021/3/10 20:17
 */
public interface TerritoryFactory extends Serializable {

    /**
     * makeTerritories as a connected undirected graph.
     *
     * @return set of territories
     */
    public List<Territory> makeTerritories();

    /**
     * get number of territories this factory make
     *
     * @return number of territories this factory make
     */
    public int territoryNum();

}
