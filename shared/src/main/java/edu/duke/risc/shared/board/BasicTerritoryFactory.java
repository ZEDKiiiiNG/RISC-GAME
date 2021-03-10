package edu.duke.risc.shared.board;

import java.util.HashSet;
import java.util.Set;

/**
 * @author eason
 * @date 2021/3/10 20:29
 */
public class BasicTerritoryFactory implements TerritoryFactory {

    @Override
    public Set<Territory> makeTerritories() {
        Set<Territory> result = new HashSet<>();
        Territory narnia = new Territory(1, "Narnia");
        result.add(narnia);
        return result;
    }

}
