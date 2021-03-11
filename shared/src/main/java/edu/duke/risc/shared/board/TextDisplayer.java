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
    public void display(GameBoard gameBoard) {
        Set<Player> players = gameBoard.getPlayers();
        for (Player player : players) {
            System.out.println(player.getColor() + " player:");
            System.out.println("-----------------------");
            for (Territory territory : player.getOwnedTerritories()) {
                //printing units
                if (territory.getUnitsMap().isEmpty()) {
                    System.out.println("No Units");
                } else {
                    for (Map.Entry<UnitType, Integer> mapUnit : territory.getUnitsMap().entrySet()) {
                        System.out.print(mapUnit.getValue() + " " + mapUnit.getKey() + " ");
                    }
                }
                System.out.print("in " + territory.getTerritoryName() + " (next to: ");
                for (Territory adjacent : territory.getAdjacentTerritories()) {
                    System.out.print(adjacent.getTerritoryName() + ", ");
                }
                System.out.println(")");
            }
            System.out.println();
        }
    }

}
