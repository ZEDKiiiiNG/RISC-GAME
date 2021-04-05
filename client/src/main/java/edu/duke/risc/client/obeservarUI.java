
package edu.duke.risc.client;

        import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class obeservarUI extends Application implements Initializable {
    Stage stage=new Stage();
    private String place ="";
    private actionChoose actionChoosePage;

    private List<Action> actions = new ArrayList<>();
    @FXML
    private Label whoami;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //at the beginning of placement phase
        //territories cache has been initialized(in main controller)
        Group g = new Group();//all widgets
        Player self = App.cc.getMyself();
        GameBoard gameBoard = App.cc.getGameBoard();


        //add territories
        for(TerritoryUI terrUI : App.TerrUIs){
            if(terrUI.isVisible()) {
                int id = terrUI.getId();
                Territory terr = gameBoard.getTerritories().get(id);
                g.getChildren().addAll(terrUI.getPane());
                App.TerrUIs.get(id).getButton().setOnAction(e -> territoryInfoScene(gameBoard, terr));
            }
        }

        primaryStage.setTitle("Place your units");
        primaryStage.setScene(new Scene(g, 1000, 600));
        primaryStage.show();
    }



    public void territoryInfoScene(GameBoard gameBoard, Territory territory){
        String terrInfo = gameBoard.getDisplayer().displaySingleTerritory(gameBoard, territory);
        Text a = new Text(terrInfo);
        a.setLayoutX(100);
        a.setLayoutY(100);
        Group g= new Group();
        g.getChildren().add(a);
        Stage secondStage = new Stage();
        Scene terrInfoScene = new Scene(g, 600, 400);
        secondStage.setScene(terrInfoScene);
        secondStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void  showWindow() throws Exception {
        start(stage);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        whoami = new Label();
        whoami.setText("Your are the \" + myColor + \" player, please place your units");
    }
}
