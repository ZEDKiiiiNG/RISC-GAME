package edu.duke.risc.server;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.PlayerHandler;
import edu.duke.risc.shared.SocketCommunicator;
import edu.duke.risc.shared.ThreadBarrier;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.PayloadType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.users.GameUser;
import edu.duke.risc.shared.users.Master;
import edu.duke.risc.shared.users.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.duke.risc.shared.Configurations.GAME_BOARD_STRING;
import static edu.duke.risc.shared.Configurations.PLAYER_STRING;

/**
 * @author eason
 * @date 2021/3/10 0:01
 */
public class GameController {

    private GameBoard board;

    private GameUser root;

    private final List<UserColor> colors = new ArrayList<>();

    private ServerSocket serverSocket;

    private Map<Player, SocketCommunicator> playerConnections;

    private ThreadBarrier barrier;

    public GameController() {
        barrier = new ThreadBarrier(Configurations.MAX_PLAYERS);
        root = new Master();
        board = new GameBoard();
        playerConnections = new HashMap<>();
        this.addColors();
        try {
            serverSocket = new ServerSocket(Configurations.DEFAULT_SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() throws IOException {
        this.waitPlayers();

    }

    private void waitPlayers() throws IOException {
        while (playerConnections.size() < Configurations.MAX_PLAYERS) {
            int playerIndex = playerConnections.size();
            System.out.println("Waiting for " + (Configurations.MAX_PLAYERS - playerIndex)
                    + " players to join the game.");
            Socket clientSocket = serverSocket.accept();
            SocketCommunicator communicator = new SocketCommunicator(clientSocket);
            Player player = new Player(playerConnections.size(), this.colors.get(playerIndex));
            //assign territories
            this.assignTerritories(player, this.board);
            playerConnections.put(player, communicator);
            System.out.println("Successfully connect with player " + player);
            PlayerHandler handler = new PlayerHandler(communicator, barrier);
            handler.start();
        }
        System.out.println("All player are ready");
        //share game map with every player
        for (Map.Entry<Player, SocketCommunicator> entry : playerConnections.entrySet()) {
            Player player = entry.getKey();
            Map<String, Object> content = new HashMap<>(10);
            content.put(GAME_BOARD_STRING, this.board);
            content.put(PLAYER_STRING, player);
            PayloadObject payloadObject = new PayloadObject(root, player, PayloadType.UPDATE, content);
            entry.getValue().writeMessage(payloadObject);
        }
    }

    private void addColors() {
        colors.add(UserColor.BLUE);
        colors.add(UserColor.GREEN);
        colors.add(UserColor.RED);
    }

    /**
     * Assign territories to player.
     *
     * @param player
     * @param gameBoard
     */
    private void assignTerritories(Player player, GameBoard gameBoard) {
        List<Territory> assignedTerritories = gameBoard.addPlayer(player);
        player.setOwnedTerritories(assignedTerritories);
    }

}
