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
                    System.out.println(territory);
                }
            }
            System.out.println("--------------");
            System.out.println();
        }
    }

}
