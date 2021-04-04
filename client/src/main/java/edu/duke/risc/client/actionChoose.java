package edu.duke.risc.client;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class actionChoose extends Application  {
    Stage stage=new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{

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

        //commit button
        javafx.scene.control.Button commit = new javafx.scene.control.Button("commit");
        commit.setLayoutX(650);
        commit.setLayoutY(50);
        commit.setOnAction(e->finishThisRoll(commit));


        //move button
        javafx.scene.control.Button move = new javafx.scene.control.Button("move");
        move.setLayoutX(650);
        move.setLayoutY(150);
        move.setOnAction(e->actMoveAndAttack(move, self));


        //attack button
        javafx.scene.control.Button attack = new javafx.scene.control.Button("attack");
        attack.setLayoutX(650);
        attack.setLayoutY(250);
        attack.setOnAction(e->actMoveAndAttack(attack, self));


        //
        //upgrade button
        javafx.scene.control.Button upgrade = new javafx.scene.control.Button("upgrade");
        upgrade.setLayoutX(650);
        upgrade.setLayoutY(350);
        upgrade.setOnAction(e->actUpgrade(upgrade, self));

        //tech button
        javafx.scene.control.Button tech = new javafx.scene.control.Button("tech");
        tech.setLayoutX(650);
        tech.setLayoutY(450);
        tech.setOnAction(e->actTech(tech, self));

        g.getChildren().addAll(commit, move, attack, upgrade, tech);

        primaryStage.setTitle("choose action");
        primaryStage.setScene(new Scene(g, 1000, 600));
        primaryStage.show();
    }

    public void finishThisRoll(javafx.scene.control.Button commit){
    }

    public void actMoveAndAttack(javafx.scene.control.Button move, Player player){
        Group g = new Group();
        Map<UnitType, Integer> unitMap = player.getTotalUnitsMap();
        Map<UnitType, TextField> nums = unitsGroup(unitMap, g);
        Text t = new Text("source territory");
        t.setLayoutX(30);
        t.setLayoutY(520);
        TextField num = new TextField();
        num.setLayoutX(150);
        num.setLayoutY(520);

        Text t1 = new Text("destination territory");
        t1.setLayoutX(30);
        t1.setLayoutY(570);
        TextField num1 = new TextField();
        num1.setLayoutX(150);
        num1.setLayoutY(570);

        Button b = new Button("submit");
        b.setLayoutX(370);
        b.setLayoutY(570);
        b.setOnAction(e->doMoveAndAttack(nums, num, num1));
        g.getChildren().addAll(t, num, t1, num1, b);
        Scene moveScene = new Scene(g, 450, 600);
        Stage secondStage = new Stage();
        secondStage.setScene(moveScene);
        secondStage.show();
    }

    //need to consider two cases, success and fail
    public void doMoveAndAttack(Map<UnitType, TextField> unitNums, TextField source, TextField dest){
        String output = "";
        output += source.getText()+","+dest.getText();
        for(UnitType i:unitNums.keySet()){
            output += ";";
            output += i.toString().substring(0, 1)+","+unitNums.get(i).getText();
        }
        System.out.println(output);
    }


    public void actUpgrade(javafx.scene.control.Button upgrade, Player player){
        Group g = new Group();
        Map<UnitType, Integer> unitMap = player.getTotalUnitsMap();

        Text t = new Text("target territory");
        t.setLayoutX(30);
        t.setLayoutY(30);
        ChoiceBox<Integer> terr_id= new ChoiceBox<>();
        terr_id.setLayoutX(150);
        terr_id.setLayoutY(30);

        Text t1 = new Text("unit type");
        t1.setLayoutX(30);
        t1.setLayoutY(120);
        ChoiceBox<String> unit_id= new ChoiceBox<>();
        unit_id.setLayoutX(150);
        unit_id.setLayoutY(120);

        Text t2 = new Text("unit number");
        t.setLayoutX(30);
        t.setLayoutY(220);
        ChoiceBox<Integer> unit_num= new ChoiceBox<>();
        unit_num.setLayoutX(150);
        unit_num.setLayoutY(220);

        Button b = new Button("submit");
        b.setLayoutX(170);
        b.setLayoutY(320);
        b.setOnAction(e->doUpgrade(terr_id, unit_id, unit_num));

        for(Integer i: player.getOwnedTerritories()){
            terr_id.getItems().add(i);
        }
        for(UnitType i : unitMap.keySet()){
            unit_id.getItems().add(i.toString());
            unit_num.getItems().add(unitMap.get(i));
        }
        g.getChildren().addAll(t, t1, t2, terr_id, unit_id, unit_num, b);
        Scene upgardeScene = new Scene(g, 300, 350);
        Stage secondStage = new Stage();
        secondStage.setScene(upgardeScene);
        secondStage.show();
    }

    public void doUpgrade(ChoiceBox<Integer> terr_id, ChoiceBox<String> unit_id, ChoiceBox<Integer> unit_num){
        String output = "";
        output += terr_id.getValue()+","+unit_id.getValue()+","+unit_num.getValue();
        System.out.println(output);
    }

    public Map<UnitType, TextField> unitsGroup(Map<UnitType, Integer> units, Group g){
        int location =0;
        Map<UnitType, TextField> nums = new HashMap<>();
        for(UnitType i : units.keySet()){
            Text t = new Text(i.toString());
            t.setLayoutX(30);
            t.setLayoutY(30+40*location);
            TextField num = new TextField();
            num.setLayoutX(150);
            num.setLayoutY(150+40*location);
            g.getChildren().add(t);
            g.getChildren().add(num);
            nums.put(i, num);
        }
        return nums;
    }

    public void showSecondWindow(String introduce){
        Text a = new Text(introduce);
        a.setLayoutX(50);
        a.setLayoutY(100);
        Group g= new Group();
        g.getChildren().add(a);
        Stage secondStage = new Stage();
        Scene techScene = new Scene(g, 400, 300);
        secondStage.setScene(techScene);
        secondStage.show();
    }

    public void actTech(javafx.scene.control.Button tech, Player self){
        if (self.isAlreadyUpgradeTechInTurn()) {
            showSecondWindow("Already upgraded in this turn");
        } else {
            showSecondWindow("Upgrade tech successfully");
        }
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
