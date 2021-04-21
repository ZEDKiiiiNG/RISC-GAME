package edu.duke.risc.client;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(ApplicationExtension.class)
class observerUITest extends Application {
    private observerUI ObserverUI;

    @Start
    public void start(Stage stage){
        ObserverUI = new observerUI();

    }

    @Test
    void showSecondWindow()  {
        Platform.runLater(()->ObserverUI.showSecondWindow(""));
    }

    @Test
    void getScrollPane() {
        Platform.runLater(()->ObserverUI.getScrollPane());
    }

    @Test
    void exitGame() {
        Platform.runLater(()->{
            Player a  = new Player(1, UserColor.BLUE);
                    assertThrows(NullPointerException.class,()->ObserverUI.exitGame(a));
                }
        );
    }

    @Test
    void startTest() {
        Platform.runLater(()->{
                    Stage s = new Stage();
                    Map<Integer, Color> territoryIds = new HashMap<>();
                    territoryIds.put(1, Color.BLUE);
                    territoryIds.put(2, Color.GREEN);
                    Player self = new Player(1, UserColor.BLUE);
                    GameBoard gameBoard = new GameBoard(2);
                    Map<Integer, Player> players = gameBoard.getPlayers();
                    players.put(1, self);
                    players.put(2, new Player(2, UserColor.GREEN));
                    int gameId = 1;
                    assertThrows(NullPointerException.class,
                            ()->ObserverUI.startObserver(s, territoryIds, self, gameBoard, players, gameId));
                }
        );
    }


}