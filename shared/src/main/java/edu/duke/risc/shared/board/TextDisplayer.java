package edu.duke.risc.shared.board;

import edu.duke.risc.shared.commons.ResourceType;
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
        System.out.println();
        System.out.println("--------------");
        for (Map.Entry<Integer, Player> entry : players.entrySet()) {
            Player player = entry.getValue();
            System.out.println(player.getColor() + " player:");
            System.out.println("-----------------------");
            if (player.getOwnedTerritories().size() == 0) {
                System.out.println("Not owned any territory, LOST");
            } else {
                for (Integer territoryId : player.getOwnedTerritories()) {
                    //printing units
                    Territory territory = gameBoard.getTerritories().get(territoryId);
                    System.out.println(this.displaySingleTerritory(gameBoard, territory));
                }
            }
            System.out.println("--------------");
            System.out.println();
        }
    }

    private String displaySingleTerritory(GameBoard gameBoard, Territory territory) {
        StringBuilder builder = new StringBuilder();

        //print territory info
        builder.append(territory.getTerritoryName()).append("(")
                .append(territory.getTerritoryId()).append(")").append(System.lineSeparator());

        //print resources
        builder.append("    Productivity: ");
        for (Map.Entry<ResourceType, Integer> entry : territory.getProductivity().entrySet()) {
            builder.append(entry.getValue()).append(" ").append(entry.getKey()).append(" ");
        }
        builder.append(System.lineSeparator());

        //print neighbors
        builder.append("    Next to: ");
        for (Integer adjacent : territory.getAdjacentTerritories()) {
            Territory adjacentTerr = gameBoard.findTerritory(adjacent);
            builder.append(adjacentTerr.getTerritoryName()).append("(")
                    .append(adjacentTerr.getTerritoryId()).append("), ");
        }
        builder.append(System.lineSeparator());

        //print units in that territory
        builder.append("    Current Units: ");
        if (territory.isEmptyTerritory()) {
            builder.append("No Units ");
        } else {
            //real units
            for (Map.Entry<UnitType, Integer> mapUnit : territory.getUnitsMap().entrySet()) {
                builder.append(mapUnit.getValue()).append(" ").append(mapUnit.getKey()).append(" ");
            }
        }

        //virtual units for clients
        for (Map.Entry<UnitType, Integer> mapUnit : territory.getVirtualUnitsMap().entrySet()) {
            builder.append("(Ready to attack units: ")
                    .append(mapUnit.getValue()).append(" ").append(mapUnit.getKey()).append(")");
        }
        return builder.toString();
    }

}
