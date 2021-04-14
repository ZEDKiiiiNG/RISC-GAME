package edu.duke.risc.client;

import edu.duke.risc.shared.actions.Action;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class placement extends Application implements Initializable {
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
        int max_num = 1;
        for (UnitType i:self.getInitUnitsMap().keySet()) {
            if(self.getInitUnitsMap().get(i)!=0){
                max_num = self.getInitUnitsMap().get(i);
            }
        }
        for(int i = 1;i<=max_num;i++){
            num_choice_box.getItems().add(i);
        }
        num_choice_box.setLayoutX(610);
        num_choice_box.setLayoutY(210);

        g.getChildren().addAll(terr_choice, terr_choice_box, num_choice, num_choice_box);


        //button commit
        javafx.scene.control.Button commit = new javafx.scene.control.Button("commit");
        commit.setLayoutX(750);
        commit.setLayoutY(550);
        commit.setOnAction(e-> {
            try {
                getPlacements(terr_choice_box, num_choice_box, self, actions);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        g.getChildren().add(commit);

        //add territories
        for(TerritoryUI terrUI : App.TerrUIs){
            if(terrUI.isVisible()) {
                int id = terrUI.getId();
                Territory terr = gameBoard.getTerritories().get(id);
                g.getChildren().addAll(terrUI.getPane());
                if(gameBoard.isTerritoryVisible(self.getId(), id)){
                    App.TerrUIs.get(id).getButton().setOnAction(e -> territoryInfoScene(gameBoard, terr));
                }
            }
        }

        primaryStage.setTitle("Place your units (GameID = " + App.cc.getGameId()+")");
        primaryStage.setScene(new Scene(g, 1000, 620));
        primaryStage.show();
    }

    public void getPlacements(ChoiceBox<String> terr, ChoiceBox<Integer> num, Player player, List<Action> actions) throws Exception {

        String action = "";
        if(terr.getItems()==null){
            action = "0,S,0";
        }
        else{
            String terr_name = terr.getValue();
            Integer numOfUnits = num.getValue();
            if(terr_name == null|| numOfUnits == null){showNullPlacementAlert("Please do placement before commit!");this.showWindow();return;}
            int i = terr_name.indexOf("(");
            String terr_id = terr_name.substring(i+1, i+2);
            System.out.println(terr_id+",S,"+num.getValue());//now sys.out, to be continue-------------------------------
            place = terr_id+",S,"+num.getValue();
            action = this.getPlaceInfo();
        }
        App.cc.clientAssignUnits(actions, action);
        if (!player.getInitUnitsMap().isEmpty()) {
            //redisplay this page
            this.showWindow();
        }else{
            showWaitingSence();
            App.cc.assignUnits(actions);
            //close placement window and go to new window
            this.stage.close();
            actionChoosePage = new actionChoose();
            actionChoosePage.showWindow();
            //turn to page action choose

        }


    }
    public void showWaitingSence(){
        Text msg = new Text("You have commit you placement\n please wait for other users finishing their placement...");
        msg.setLayoutX(50);
        msg.setLayoutY(100);
        Group g= new Group();
        g.getChildren().add(msg);
        Scene waitOthers = new Scene(g, 400, 300);
        this.stage.setScene(waitOthers);
    }
    public void showNullPlacementAlert(String prompt){
        Text a = new Text(prompt);
        javafx.scene.control.Button exitButton = new javafx.scene.control.Button("OK");
        exitButton.setLayoutX(50);
        exitButton.setLayoutY(200);
        a.setLayoutX(50);
        a.setLayoutY(100);
        Group g= new Group();
        g.getChildren().add(a);
        g.getChildren().add(exitButton);
        Stage secondStage = new Stage();
        Scene techScene = new Scene(g, 400, 300);
        secondStage.setScene(techScene);
        exitButton.setOnAction(e ->{secondStage.close();});
        secondStage.showAndWait();
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

    public String getPlaceInfo(){
        return this.place;
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
