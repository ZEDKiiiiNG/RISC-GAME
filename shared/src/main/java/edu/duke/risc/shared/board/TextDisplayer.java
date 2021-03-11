package edu.duke.risc.shared.board;

import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.users.Player;

import java.util.Map;

/**
 * @author eason
 * @date 2021/3/10 21:28
 */
public class TextDisplayer implements Displayable {

    @Override
    public void display(GameBoard gameBoard) {
        Map<Integer, Player> players = gameBoard.getPlayers();
        for (Map.Entry<Integer, Player> entry : players.entrySet()) {
            Player player = entry.getValue();
            System.out.println(player.getColor() + " player:");
            System.out.println("-----------------------");
            for (Integer territoryId : player.getOwnedTerritories()) {
                //printing units
                Territory territory = gameBoard.getTerritories().get(territoryId);
                if (territory.getUnitsMap().isEmpty()) {
                    System.out.print("No Units ");
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
