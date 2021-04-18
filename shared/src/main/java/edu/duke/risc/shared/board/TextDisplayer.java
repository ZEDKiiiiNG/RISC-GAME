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

    @Override
    public String displaySingleTerritory(GameBoard gameBoard, Territory territory) {
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

        //print size (move costs)
        builder.append("    Size (move costs): ").append(territory.getSize()).append(System.lineSeparator());

        //print neighbors
        builder.append("    Next to: ");
        for (Integer adjacent : territory.getAdjacentTerritories()) {
            Territory adjacentTerr = gameBoard.findTerritory(adjacent);
            if (adjacentTerr.isValid()) {
                builder.append(adjacentTerr.getTerritoryName()).append("(")
                        .append(adjacentTerr.getTerritoryId()).append("), ");
            }
        }
        builder.append(System.lineSeparator());

        //add spies here
        builder.append(" Spies: ");
        if (territory.getSpies().isEmpty()) {
            builder.append(" No Spies");
        } else {
            for (Map.Entry<Integer, Integer> entry : territory.getSpies().entrySet()) {
                builder.append(" Player ").append(entry.getKey())
                        .append(" with ").append(entry.getValue()).append(", ");
            }
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

        if (!territory.getVirtualUnitsMap().isEmpty()) {
            builder.append("(Ready to attack units: ");
            //virtual units for clients
            for (Map.Entry<UnitType, Integer> mapUnit : territory.getVirtualUnitsMap().entrySet()) {
                builder.append(mapUnit.getValue()).append(" ").append(mapUnit.getKey());
            }
            builder.append(")");
        }

        return builder.toString();
    }

}
