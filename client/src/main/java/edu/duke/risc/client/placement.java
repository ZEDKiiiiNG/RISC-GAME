package edu.duke.risc.client;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class placement extends Application{
    Stage stage=new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Enum myColor = App.cc.getMyself().getColor();
        Map<Integer, Color> territoryIds = new HashMap<>();
        Player self = App.cc.getMyself();
        GameBoard gameBoard = App.cc.getGameBoard();
        Map<Integer, Player> players = gameBoard.getPlayers();
        for (Map.Entry<Integer, Player> entry : players.entrySet()) {
            Player player = entry.getValue();
            for (Integer territoryId : player.getOwnedTerritories()) {
                //printing units
                territoryIds.put(territoryId, Color.web(player.getColor().name()));
            }
        }

        Group g = new Group();


        Rectangle[] rects =  new Rectangle[10];
        StackPane[] panes = new StackPane[10];
        javafx.scene.control.Button[] buttons = new javafx.scene.control.Button[10];
        //Washington(9)

        rects[9] = new Rectangle(0,0,200, 200);
        buttons[9] = new javafx.scene.control.Button("Washington(9)");
        panes[9] = new StackPane();
        panes[9].setLayoutX(0);
        panes[9].setLayoutY(0);

        //Idaho(2)
        rects[2] = new Rectangle(200,0,200, 200);
        buttons[2] = new javafx.scene.control.Button("Idaho(2)");
        panes[2] = new StackPane();
        panes[2].setLayoutX(200);
        panes[2].setLayoutY(0);

        //Wyoming(3)
        rects[3] = new Rectangle(400,0,200, 200);
        buttons[3] = new javafx.scene.control.Button("Wyoming(3)");
        panes[3] = new StackPane();
        panes[3].setLayoutX(400);
        panes[3].setLayoutY(0);

        //Oregon(8)
        rects[8] = new Rectangle(0,200,250, 200);
        buttons[8] = new javafx.scene.control.Button("Oregon(8)");
        panes[8] = new StackPane();
        panes[8].setLayoutX(0);
        panes[8].setLayoutY(200);

        //California(7)
        rects[7] = new Rectangle(0,400,250, 200);
        buttons[7] = new javafx.scene.control.Button("California(7)");
        panes[7] = new StackPane();
        panes[7].setLayoutX(0);
        panes[7].setLayoutY(400);

        //Washington(9)
        rects[1] = new Rectangle(250,200,100, 250);
        buttons[1] = new javafx.scene.control.Button("Washington(9)");
        panes[1] = new StackPane();
        panes[1].setLayoutX(250);
        panes[1].setLayoutY(200);

        //Utah(0)
        rects[0] = new Rectangle(350,200,100, 250);
        buttons[0] = new javafx.scene.control.Button("Utah(0)");
        panes[0] = new StackPane();
        panes[0].setLayoutX(350);
        panes[0].setLayoutY(200);

        //Colorado(4)
        rects[4] = new Rectangle(450,200,150, 250);
        buttons[4] = new javafx.scene.control.Button("Colorado(4)");
        panes[4] = new StackPane();
        panes[4].setLayoutX(450);
        panes[4].setLayoutY(200);

        //Arizona(6)
        rects[6] = new Rectangle(250,450,150, 150);
        buttons[6] = new javafx.scene.control.Button("Arizona(6)");
        panes[6] = new StackPane();
        panes[6].setLayoutX(250);
        panes[6].setLayoutY(450);

        //New Mexico(5)
        rects[5] = new Rectangle(400,450,200, 150);
        buttons[5] = new javafx.scene.control.Button("New Mexico(5)");
        panes[5] = new StackPane();
        panes[5].setLayoutX(400);
        panes[5].setLayoutY(450);

        for(int i =0;i<10;i++){
            if(territoryIds.get(i)!=null){
                displayTerritory(rects[i], panes[i], buttons[i], g, territoryIds.get(i), gameBoard, gameBoard.findTerritory(i));
            }

        }

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

        primaryStage.setTitle("Place your units");
        primaryStage.setScene(new Scene(g, 1000, 600));
        primaryStage.show();
    }

    public void getPlacements(ChoiceBox<String> terr, ChoiceBox<Integer> num){
        String terr_name = terr.getValue();
        int i = terr_name.indexOf("(");
        String terr_id = terr_name.substring(i+1, i+2);
        System.out.println(terr_id+",S,"+num.getValue());
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

    public void displayTerritory(Rectangle rect, StackPane pane, javafx.scene.control.Button b, Group g, Color color, GameBoard gameBoard, Territory territory){
        rect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        pane.getChildren().addAll(rect, b);
        rect.setFill(color);
        b.setOnAction(e->territoryInfoScene(gameBoard,territory));
        g.getChildren().add(pane);

    }
    public static void main(String[] args) {
        launch(args);
    }

    public void  showWindow() throws Exception {
        start(stage);
    }

}
