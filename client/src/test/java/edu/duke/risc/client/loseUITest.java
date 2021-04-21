package edu.duke.risc.client;

import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(ApplicationExtension.class)
class loseUITest extends Application {
    private loseUI LoseUI;

    @Start
    public void start(Stage stage){
        LoseUI = new loseUI();

    }

    @Test
    void start() {

    }



    @Test
    void exitGame() {
        Platform.runLater(()->{
                    Player a = new Player(1, UserColor.BLUE);
                    assertThrows(NullPointerException.class,()->LoseUI.exitGame(a));
                }
        );
    }

    @Test
    void startUI() {
        Platform.runLater(()->{
                    Stage s = new Stage();
                    Player a = new Player(1, UserColor.BLUE);
                    int gameId = 1;
                    assertThrows(NullPointerException.class,()->LoseUI.startLoseUI(s, a, gameId));
                }
        );
    }
}