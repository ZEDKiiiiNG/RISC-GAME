package edu.duke.risc.client;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class placement extends Application implements Initializable {
    Stage stage=new Stage();
    @FXML
    private Label whoami;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //at the beginning of placement phase
        //territories cache has been initialized(in main controller)
        Group g = new Group();//all widgets
        Player self = App.cc.getMyself();
        GameBoard gameBoard = App.cc.getGameBoard();

        //introduce text
        String introduce = "player "+self.getColor().toString()+"\n"+
                "You are assigned " + gameBoard.getPlayerAssignedTerritoryInfo(self.getId())+"\n"
                + "You still have " + self.getUnitsInfo(self.getInitUnitsMap()) + " available";
        Text intro = new Text(introduce);
        intro.setLayoutX(610);
        intro.setLayoutY(10);
        g.getChildren().add(intro);

        //territory choice box
        Text terr_choice = new Text("choose the territory you want to put units in");
        terr_choice.setLayoutX(610);
        terr_choice.setLayoutY(80);
        ChoiceBox<String> terr_choice_box = new ChoiceBox<>();
        for (Integer territoryId : self.getOwnedTerritories()) {
            terr_choice_box.getItems().add(gameBoard.getTerritories().get(territoryId).toString());
        }
        terr_choice_box.setLayoutX(610);
        terr_choice_box.setLayoutY(110);

        //soldier number choice box
        Text num_choice = new Text("choose the number of soldiers you want to put");
        num_choice.setLayoutX(610);
        num_choice.setLayoutY(180);
        ChoiceBox<Integer> num_choice_box = new ChoiceBox<>();
        int max_num = 0;
        for (UnitType i:self.getInitUnitsMap().keySet()) {
            if(self.getInitUnitsMap().get(i)!=0){
                max_num = self.getInitUnitsMap().get(i);
            }

        }
        for(int i = 0;i<=max_num;i++){
            num_choice_box.getItems().add(i);
        }
        num_choice_box.setLayoutX(610);
        num_choice_box.setLayoutY(210);

        g.getChildren().addAll(terr_choice, terr_choice_box, num_choice, num_choice_box);

        //button commit
        javafx.scene.control.Button commit = new javafx.scene.control.Button("commit");
        commit.setLayoutX(750);
        commit.setLayoutY(550);
        commit.setOnAction(e->getPlacements(terr_choice_box, num_choice_box));
        g.getChildren().add(commit);

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

    public void getPlacements(ChoiceBox<String> terr, ChoiceBox<Integer> num){
        String terr_name = terr.getValue();
        int i = terr_name.indexOf("(");
        String terr_id = terr_name.substring(i, i+1);
        System.out.println(terr_id+",S,"+num.getValue());//now sys.out, to be continue-------------------------------
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
