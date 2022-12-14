package edu.duke.risc.client;

import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(ApplicationExtension.class)
class placementTest extends Application {
    private placement placeView;

    @Start
    public void start(Stage stage){
        placeView = new placement();
        stage = new Stage();
        placeView.stage = stage;

    }


    @Test
    public void test_placement() throws Exception {
        placeView.init();
        Group g = new Group();//all widgets
        Player self = new Player(1, UserColor.RED);
        GameBoard gameBoard = new GameBoard(2);
        String introduce = "player RED";
        int gameId = 1;
        Platform.runLater(()-> {
            try {
                placeView.startPlacement(new Stage(), g, self, gameBoard, introduce, gameId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    void showWaitingSenceTest(FxRobot robot) throws Exception {
        placeView.init();
        Platform.runLater(()->placeView.showWaitingSence());
    }

    @Test
    void getPlacementsTest() {
        Platform.runLater(()->{
            javafx.scene.control.ChoiceBox<String> terr = new ChoiceBox<>();
            ChoiceBox<Integer> num = new ChoiceBox<>();
            Player player = new Player(1, UserColor.RED);
            List< Action > actions = new ArrayList<>();
            try {
                placeView.getPlacements(terr, num, player, actions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    void getPlacementsTestNotNull() {
        Platform.runLater(()->{
            javafx.scene.control.ChoiceBox<String> terr = new ChoiceBox<>();
            terr.setValue("Utah(1)");
            ChoiceBox<Integer> num = new ChoiceBox<>();
            num.setValue(1);
            Player player = new Player(1, UserColor.RED);
            List< Action > actions = new ArrayList<>();
            try {
                placeView.getPlacements(terr, num, player, actions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    void territoryInfoSceneTest() {
        Platform.runLater(()->placeView.territoryInfoScene(new GameBoard(2), new Territory(1, "a")));
    }



    @Test
    void getPlaceInfoTest() {
        Platform.runLater(()->placeView.getPlaceInfo());

    }


    @Test
    void showNullPlacementAlertTest() {
        Platform.runLater(()->placeView.showNullPlacementAlert(""));
    }
}
