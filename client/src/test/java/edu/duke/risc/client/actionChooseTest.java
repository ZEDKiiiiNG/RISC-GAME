package edu.duke.risc.client;

import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.commons.UserColor;
import edu.duke.risc.shared.users.Player;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class actionChooseTest {
    private actionChoose actionChooseView;

    @Start
    public void start(Stage stage){
        actionChooseView = new actionChoose();
        stage = new Stage();
        actionChooseView.stage = stage;

    }

    @Test
    void actMove() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Player player= new Player(1, UserColor.BLUE);
            List<Action> moveActions = new ArrayList<>();
            actionChooseView.actMove(player, moveActions);
        });
    }

    @Test
    void actAttack() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Player player= new Player(1, UserColor.BLUE);
            List<Action> moveActions = new ArrayList<>();
            actionChooseView.actAttack(player, moveActions);
        });
    }

    @Test
    void doMoveNull() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Map<UnitType, javafx.scene.control.TextField> unitNums = new HashMap<>();
            unitNums.put(UnitType.QUEEN, new TextField());
            TextField source = new TextField();
            javafx.scene.control.TextField dest = new TextField("Utah(5)");
            List<Action> moveActions = new ArrayList<>();
            Stage secondStage = new Stage();
            try {
                assertThrows(NullPointerException.class,
                        () -> actionChooseView.doMove(unitNums, source, dest, moveActions, secondStage));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void doMoveNotNull() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Map<UnitType, javafx.scene.control.TextField> unitNums = new HashMap<>();
            unitNums.put(UnitType.QUEEN, new TextField());
            TextField source = new TextField();
            source.setText("Utah(5)");
            javafx.scene.control.TextField dest = new TextField("Utah(5)");
            List<Action> moveActions = new ArrayList<>();
            Stage secondStage = new Stage();
            try {
                assertThrows(NullPointerException.class,
                        () -> actionChooseView.doMove(unitNums, source, dest, moveActions, secondStage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void doAttackNotNull() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Map<UnitType, javafx.scene.control.TextField> unitNums = new HashMap<>();
            unitNums.put(UnitType.QUEEN, new TextField());
            TextField source = new TextField();
            source.setText("Utah(0)");
            javafx.scene.control.TextField dest = new TextField("Utah(0)");
            List<Action> moveActions = new ArrayList<>();
            Stage secondStage = new Stage();
            try {
                assertThrows(NullPointerException.class,
                        () -> actionChooseView.doAttack(unitNums, source, dest, moveActions, secondStage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void doAttackNull() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Map<UnitType, javafx.scene.control.TextField> unitNums = new HashMap<>();
            unitNums.put(UnitType.QUEEN, new TextField());
            TextField source = new TextField();
            source.setText("Utah(5)");
            javafx.scene.control.TextField dest = new TextField();
            List<Action> moveActions = new ArrayList<>();
            Stage secondStage = new Stage();
            try {
                assertThrows(NullPointerException.class,
                        () -> actionChooseView.doAttack(unitNums, source, dest, moveActions, secondStage));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void actAttackMissile() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Player player= new Player(1, UserColor.BLUE);
            List<Action> moveActions = new ArrayList<>();
            assertThrows(NullPointerException.class,
                    () -> actionChooseView.actAttackMissile(player, moveActions));

        });
    }

    @Test
    void actCloakResearch() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Player player= new Player(1, UserColor.BLUE);
            List<Action> moveActions = new ArrayList<>();
            try {
                assertThrows(NullPointerException.class,
                        () -> actionChooseView.actCloakResearch(player, moveActions));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void actCloakTerritory() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Player player= new Player(1, UserColor.BLUE);
            List<Action> moveActions = new ArrayList<>();
            actionChooseView.actCloakTerritory(player, moveActions);
        });
    }

    @Test
    void actUpgrade() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Player player= new Player(1, UserColor.BLUE);
            List<Action> moveActions = new ArrayList<>();
            actionChooseView.actUpgrade(player, moveActions);
        });
    }

    @Test
    void doUpgrade() {

    }

    @Test
    void actTech() {

    }

    @Test
    void unitsGroup() {
    }

    @Test
    void showSecondWindow() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            actionChooseView.showSecondWindow("");
        });
    }

    @Test
    void territoryInfoScene() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            GameBoard gameBoard = new GameBoard(2);
            Territory territory = new Territory(5, "Utah");
            actionChooseView.territoryInfoScene(gameBoard, territory);
        });

    }

    @Test
    void startActionTest() throws Exception {
        actionChooseView.init();
        Platform.runLater(()->{
            Stage primaryStage = new Stage();
            Player self = new Player(1, UserColor.BLUE);
            GameBoard gameBoard = new GameBoard(3);
            Map<Integer, Player> players = new HashMap<>();
            Player temp = new Player(2, UserColor.GREEN);
            Player temp1 = new Player(3, UserColor.RED);
            players.put(1, self);
            players.put(2, temp);
            players.put(3, temp1);
            try {
                assertThrows(NullPointerException.class,
                        () -> actionChooseView.startActionChoose(primaryStage, self, gameBoard, players));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}