package edu.duke.risc.shared.board;

import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.users.Player;

import java.util.Map;
import java.util.Set;

/**
 * @author eason
 * @date 2021/3/10 21:28
 */
public class TextDisplayer implements Displayable {

    @Override
    public void display(Set<Player> players) {
        for (Player player : players) {
            System.out.println(player.getColor() + "player:");
            for (Territory territory : player.getOwnedTerritories()) {
                for (Map.Entry<UnitType, Integer> mapunit : territory.getUnitsMap().entrySet()) {
                    System.out.print(mapunit.getValue() + " " + mapunit.getKey()+ " ");
                }
                System.out.print("in " + territory.getTerritoryName() + " (next to: ");
                for(Territory adjacant : territory.getAdjacentTerritories()){
                    System.out.print(adjacant.getTerritoryName()+ ", ");
                }
                System.out.println(")");
            }

        }
    }

}
