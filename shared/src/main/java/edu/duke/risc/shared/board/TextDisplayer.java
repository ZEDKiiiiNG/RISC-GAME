package edu.duke.risc.shared.board;

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
            if (player.getOwnedTerritories().size() == 0) {
                System.out.println("Not owned any territory");
            } else {
                for (Integer territoryId : player.getOwnedTerritories()) {
                    //printing units
                    Territory territory = gameBoard.getTerritories().get(territoryId);
                    System.out.println(territory);
                }
            }
            System.out.println();
        }
        //display un-owned territories
        System.out.println("Territories that are not owned yet");
        System.out.println("-----------------------");
        for (Map.Entry<Integer, Territory> territoryEntry : gameBoard.getTerritories().entrySet()) {
            if (territoryEntry.getValue().isEmptyTerritory()) {
                //assert not owned by anyone
                for (Map.Entry<Integer, Player> playerEntry : gameBoard.getPlayers().entrySet()) {
                    assert playerEntry.getValue().ownsTerritory(territoryEntry.getKey());
                }
                System.out.println(territoryEntry.getValue());
            }
        }


    }

}
