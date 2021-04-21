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
@ExtendWith(ApplicationExtension.class)
class winUITest extends Application {
    private winUI WinUI;

    @Start
    public void start(Stage stage){
        WinUI= new winUI();

    }
//    @Test
//    void sexitGameTest(FxRobot robot) throws Exception {
//        Platform.runLater(()-> {
//            try {
//                WinUI.exitGame();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }


    @Test
    void startTest() {
        Platform.runLater(()->{
                    Stage s = new Stage();
                    Player a  = new Player(1, UserColor.BLUE);
                    WinUI.startWinUI(s, a);
                }
        );
    }
}