package edu.duke.risc.client;

import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.exceptions.InvalidInputException;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class actionChoose extends Application  {
    Stage stage=new Stage();
    private observerUI obeserver;
    private List<Action> moveActions = new ArrayList<>();
    private List<Action> attackActions = new ArrayList<>();
    private List<Action> upgradeUnitsActions = new ArrayList<>();
    private List<Action> upgradeTechActions = new ArrayList<>();

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

        //add territories
        for(TerritoryUI terrUI : App.TerrUIs){
            if(terrUI.isVisible()) {
                int id = terrUI.getId();
                Territory terr = gameBoard.getTerritories().get(id);
                g.getChildren().addAll(terrUI.getPane());
                App.TerrUIs.get(id).getButton().setOnAction(e -> territoryInfoScene(gameBoard, terr));
            }
        }




        //text

        Text a = new Text(self.getPlayerInfo());
        a.setLayoutX(650);
        a.setLayoutY(10);
        g.getChildren().add(a);


        //move button
        javafx.scene.control.Button move = new javafx.scene.control.Button("move");
        move.setLayoutX(650);
        move.setLayoutY(150);
        move.setOnAction(e->actMove(self, moveActions));


        //attack button
        javafx.scene.control.Button attack = new javafx.scene.control.Button("attack");
        attack.setLayoutX(650);
        attack.setLayoutY(250);
        attack.setOnAction(e->actAttack(self, attackActions));


        //
        //upgrade button
        javafx.scene.control.Button upgrade = new javafx.scene.control.Button("upgrade");
        upgrade.setLayoutX(650);
        upgrade.setLayoutY(350);
        upgrade.setOnAction(e->actUpgrade(self, upgradeUnitsActions));

        //tech button
        javafx.scene.control.Button tech = new javafx.scene.control.Button("tech");
        tech.setLayoutX(650);
        tech.setLayoutY(450);
        tech.setOnAction(e-> {
            try {
                actTech(self, upgradeTechActions);
            } catch (Exception exception) {
                showSecondWindow("upgrade tech failed");
            }
        });


        //commit button
        javafx.scene.control.Button commit = new javafx.scene.control.Button("commit");
        commit.setLayoutX(650);
        commit.setLayoutY(550);
        commit.setOnAction(e-> {
            try {
                finishThisRoll(moveActions, attackActions, upgradeTechActions, upgradeUnitsActions);
            } catch (Exception ioException) {
//                if (App.cc.checkUserStatus()) {
//                    this.stage.close();
//                    obeserver = new observerUI();
//                    try {
//                        obeserver.showWindow();
//                    } catch (Exception exception) {
//                        exception.printStackTrace();
//                    }
//                }
                showSecondWindow("send actions to server failed");
//            } catch (Exception exception) {
//                if (App.cc.checkUserStatus()) {
//                    this.stage.close();
//                    obeserver = new observerUI();
//                    try {
//                        obeserver.showWindow();
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
                showSecondWindow("send actions to server failed");
            }
        });


        g.getChildren().addAll(commit, move, attack, upgrade, tech);

        primaryStage.setTitle("choose action");
        primaryStage.setScene(new Scene(g, 1100, 600));
        primaryStage.show();
        if (App.cc.checkUserStatus()) {
            this.stage.close();
            obeserver = new observerUI();
            obeserver.showWindow();
        }
    }


    public void finishThisRoll(List<Action> moveActions, List<Action> attackActions,
                               List<Action> upgradeTechActions, List<Action> upgradeUnitsActions) throws Exception {
        if (App.cc.checkUserStatus()) {
            return;
        }
        String log = App.cc.moveAndAttack(moveActions, attackActions, upgradeTechActions, upgradeUnitsActions);

        moveActions.clear();
        attackActions.clear();
        upgradeUnitsActions.clear();
        upgradeTechActions.clear();
        App.updateTerritories();

        showSecondWindow(log);
        this.showWindow();
    }

    public void actMove(Player player, List<Action> moveActions){
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
        Stage secondStage = new Stage();
        b.setOnAction(e-> {
            try {
                if (!App.cc.checkUserStatus()) {//?
                    doMove(nums, num, num1, moveActions, secondStage);
                }

            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        });
        g.getChildren().addAll(t, num, t1, num1, b);
        Scene moveScene = new Scene(g, 450, 600);

        secondStage.setScene(moveScene);
        secondStage.show();
    }

    public void actAttack(Player player, List<Action> attackActions){
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
        Stage secondStage = new Stage();
        b.setOnAction(e-> {
            try {
                if (!App.cc.checkUserStatus()) {
                    doAttack(nums, num, num1, attackActions, secondStage);
                }
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        });
        g.getChildren().addAll(t, num, t1, num1, b);
        Scene moveScene = new Scene(g, 450, 600);

        secondStage.setScene(moveScene);
        secondStage.show();
    }


    //need to consider two cases, success and fail
    public void doMove(Map<UnitType, TextField> unitNums, TextField source, TextField dest,
                       List<Action> moveActions, Stage secondStage) throws Exception {
        if(source.getText().trim().equals("")||dest.getText().trim().equals("")){
            showSecondWindow("Invalid input");
            this.showWindow();
            return;
        }
        String output = "";
        output += source.getText()+","+dest.getText();
        for(UnitType i:unitNums.keySet()){
            if(!unitNums.get(i).getText().trim().equals("")){
                output += ";";
                output += i.toString().substring(1, 2)+","+unitNums.get(i).getText();
            }

        }
        System.out.println(output);
        secondStage.close();

        try{
            App.cc.conductMoveOrAttack(moveActions, 0, output);
            showSecondWindow("Instruction: "+output+"\n"+"move action success");
        }catch (NumberFormatException e){
            showSecondWindow("Instruction: "+output+"\n"+"move action fail");
        } catch (InvalidInputException e) {
            showSecondWindow("Instruction: "+output+"\n"+"move action fail");
        } catch (InvalidActionException e) {
            showSecondWindow("Instruction: "+output+"\n"+"move action fail");
        }
        this.showWindow();
    }

    public void doAttack(Map<UnitType, TextField> unitNums, TextField source, TextField dest,
                         List<Action> attackActions, Stage secondStage) throws Exception {
        if(source.getText().trim().equals("")||dest.getText().trim().equals("")){
            showSecondWindow("Invalid input");
            this.showWindow();
            return;
        }
        String output = "";
        output += source.getText()+","+dest.getText();
        for(UnitType i:unitNums.keySet()){
            if(!unitNums.get(i).getText().trim().equals("")){
                output += ";";
                output += i.toString().substring(1, 2)+","+unitNums.get(i).getText();
            }

        }
        System.out.println(output);
        secondStage.close();

        try{
            App.cc.conductMoveOrAttack(attackActions, 1, output);
            showSecondWindow("Instruction: "+output+"\n"+"attack action success");
        }catch (NumberFormatException e){
            showSecondWindow("Instruction: "+output+"\n"+"attack action fail");
        } catch (InvalidInputException e) {
            showSecondWindow("Instruction: "+output+"\n"+"attack action fail");
        } catch (InvalidActionException e) {
            showSecondWindow("Instruction: "+output+"\n"+"attack action fail");
        }
        this.showWindow();
    }


    public void actUpgrade(Player player, List<Action> upgradeUnitsActions){
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
        t2.setLayoutX(30);
        t2.setLayoutY(220);
        //ChoiceBox<Integer> unit_num= new ChoiceBox<>();
        TextField unit_num = new TextField();
        unit_num.setLayoutX(150);
        unit_num.setLayoutY(220);

        Stage secondStage = new Stage();

        Button b = new Button("submit");
        b.setLayoutX(170);
        b.setLayoutY(320);
        b.setOnAction(e-> {
            try {
                if (!App.cc.checkUserStatus()) {
                    doUpgrade(terr_id, unit_id, unit_num, secondStage, upgradeUnitsActions);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        for(Integer i: player.getOwnedTerritories()){
            System.out.println(i);
            terr_id.getItems().add(i);
        }
        for(UnitType i : unitMap.keySet()){
            unit_id.getItems().add(i.toString());
        }
        g.getChildren().addAll(t, t1, t2, terr_id, unit_id, unit_num, b);
        Scene upgardeScene = new Scene(g, 300, 350);

        secondStage.setScene(upgardeScene);
        secondStage.show();
    }

    public void doUpgrade(ChoiceBox<Integer> terr_id, ChoiceBox<String> unit_id, TextField unit_num,
                          Stage secondStage, List<Action> upgradeUnitsActions) throws Exception {

        if(terr_id.getValue()==null||unit_id.getValue()==null){
            showSecondWindow("Invalid input");
            this.showWindow();
            return;
        }
        String output = "";
        String unitType = unit_id.getValue().substring(1, 2);
        output += terr_id.getValue()+","+unitType+","+unit_num.getText();
        System.out.println(output);
        secondStage.close();
        //this.showWindow();
        try {
            App.cc.conductUpgradeUnits(upgradeUnitsActions, output);
            showSecondWindow("Instruction: "+output+"\n"+"upgrade successfully");
        } catch (InvalidInputException | InvalidActionException e) {
            showSecondWindow("Instruction: "+output+"\n"+"upgrade failed");
        }
        this.showWindow();
    }

    public void actTech(Player self, List<Action> upgradeTechActions) throws Exception {
        if (App.cc.checkUserStatus()) {
            return;
        }
        if (!self.isAlreadyUpgradeTechInTurn()) {
            try {
                App.cc.conductUpgradeTechLevel(upgradeTechActions);
                showSecondWindow("Upgrade tech success");
            } catch (InvalidActionException e) {
                showSecondWindow("Upgrade tech fail");
            }

        } else {
            showSecondWindow("Already upgraded in this turn");
        }
        this.showWindow();
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
            num.setLayoutY(30+40*location);
            g.getChildren().add(t);
            g.getChildren().add(num);
            nums.put(i, num);
            location++;
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
        secondStage.showAndWait();//用户必须首先处理新的弹窗
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



}
