package edu.duke.risc.client;

import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.MissileType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.exceptions.InvalidInputException;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class actionChoose extends Application  {
    Stage stage=new Stage();
    String scrollText = "Please choose the action you want to take in this roll.\n" +
            "Do not commit until you finish all actions.";
//    private observerUI obeserver;
//    private winUI win;
    private loseUI lose;
    private List<Action> attackActions = new ArrayList<>();
    private List<Action> missileAttackActions = new ArrayList<>();
    private List<Action> nonAffectActions = new ArrayList<>();
    private Map<Integer, String> visibleTerritories = new HashMap<>();
    private Map<Integer, String> oldVisibleTerritories = new HashMap<>();


    @Override
    public void start(Stage primaryStage) throws Exception{


        Map<Integer, Color> territoryIds = new HashMap<>();
        Player self = App.cc.getMyself();
        GameBoard gameBoard = App.cc.getGameBoard();
        Map<Integer, Player> players = gameBoard.getPlayers();
        startActionChoose(primaryStage, self, gameBoard, players);
    }

    public void startActionChoose(Stage primaryStage, Player self, GameBoard gameBoard, Map<Integer, Player> players) throws Exception {
        Map<Integer, String> newVisibleTerritories = new HashMap<>();
        Map<Integer, Color> territoryIds = new HashMap<>();
        for (Map.Entry<Integer, Player> entry : players.entrySet()) {
            Player player = entry.getValue();
            for (Integer territoryId : player.getOwnedTerritories()) {
                //printing units
                Territory terr = gameBoard.findTerritory(territoryId);
                String terrInfo = gameBoard.getDisplayer().displaySingleTerritory(gameBoard, terr);
                //
                territoryIds.put(territoryId, Color.web(player.getColor().name()));
                //
                if(gameBoard.isTerritoryVisible(self.getId(), territoryId)){
                    newVisibleTerritories.put(territoryId, "used to owned by player "+player.getColor().toString()+"\n"+terrInfo);
                }
            }
        }

        for(Integer i:visibleTerritories.keySet()){
            if(newVisibleTerritories.get(i)==null){
                oldVisibleTerritories.put(i, visibleTerritories.get(i));
            }
            else{
                oldVisibleTerritories.remove(i);
            }
        }

        visibleTerritories = newVisibleTerritories;

        Group g = new Group();

        //add territories
        for(TerritoryUI terrUI : App.TerrUIs){
            if(terrUI.isVisible()) {
                int id = terrUI.getId();
                Territory terr = gameBoard.getTerritories().get(id);
                g.getChildren().addAll(terrUI.getPane());
                if(gameBoard.isTerritoryVisible(self.getId(), id)){
                    App.TerrUIs.get(id).getButton().setOnAction(e -> territoryInfoScene(gameBoard, terr));
                }
                if(oldVisibleTerritories.get(id)!=null){
                    App.TerrUIs.get(id).getButton().setOnAction(e->showSecondWindow(oldVisibleTerritories.get(id)));
                }
            }
        }

        //scrollPane
        g.getChildren().add(getScrollPane());

        //text
        ScrollPane scroll = new ScrollPane();
        scroll.setPrefSize(300, 130);
        scroll.setLayoutX(550);
        scroll.setLayoutY(160);
        Text a = new Text(self.getPlayerInfo());
        a.setLayoutX(550);
        a.setLayoutY(160);
        scroll.setContent(a);
        g.getChildren().add(scroll);

        //move button
        javafx.scene.control.Button move = new javafx.scene.control.Button("move");
        move.setLayoutX(550);
        move.setLayoutY(310);
        move.setOnAction(e->actMove(self, nonAffectActions));

        //attack button
        javafx.scene.control.Button attack = new javafx.scene.control.Button("units attack");
        attack.setLayoutX(550);
        attack.setLayoutY(360);
        attack.setOnAction(e->actAttack(self, attackActions));

        //upgrade button
        javafx.scene.control.Button upgrade = new javafx.scene.control.Button("upgrade");
        upgrade.setLayoutX(550);
        upgrade.setLayoutY(410);
        upgrade.setOnAction(e->actUpgrade(self, nonAffectActions));

        //tech button
        javafx.scene.control.Button tech = new javafx.scene.control.Button("tech");
        tech.setLayoutX(700);
        tech.setLayoutY(410);
        tech.setOnAction(e-> {
            try {
                actTech(self, nonAffectActions);
            } catch (Exception exception) {
                showSecondWindow("upgrade tech failed");
            }
        });

        //missile button
        javafx.scene.control.Button attackMissile = new javafx.scene.control.Button("missile attack");
        attackMissile.setLayoutX(700);
        attackMissile.setLayoutY(360);
        attackMissile.setOnAction(e->actAttackMissile(self, missileAttackActions));

        //train spy button
        Button trainSpy = new Button("train spy");
        trainSpy.setLayoutX(550);
        trainSpy.setLayoutY(460);
        trainSpy.setOnAction(e-> actTrainSpy(self, nonAffectActions));

        //move spy button
        Button moveSpy = new Button("move spy");
        moveSpy.setLayoutX(700);
        moveSpy.setLayoutY(460);
        moveSpy.setOnAction(e->actMoveSpy(self, nonAffectActions));

        //cloak research button
        javafx.scene.control.Button cloakRearch = new Button("cloak research");
        cloakRearch.setLayoutX(550);
        cloakRearch.setLayoutY(510);
        cloakRearch.setOnAction(e-> {
            try {
                actCloakResearch(self, nonAffectActions);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        //cloak territory button
        Button cloakTerritory = new Button("cloak territory");
        cloakTerritory.setLayoutX(550);
        cloakTerritory.setLayoutY(510);
        cloakTerritory.setOnAction(e->actCloakTerritory(self,  nonAffectActions));

        //commit button
        javafx.scene.control.Button commit = new javafx.scene.control.Button("commit");
        commit.setLayoutX(760);
        commit.setLayoutY(560);
        commit.setOnAction(e-> {
            try {
                finishThisRoll(attackActions, missileAttackActions, nonAffectActions);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        g.getChildren().addAll(commit, move, attack, upgrade, tech, attackMissile, trainSpy, moveSpy);

        if(self.getTechnology()>=3){
            if(!self.isCloakingResearched()){
                g.getChildren().add(cloakRearch);
            }else{
                g.getChildren().add(cloakTerritory);
            }
        }

        primaryStage.setTitle("choose action (GameID = "+ App.cc.getGameId() + ")");
        primaryStage.setScene(new Scene(g, 900, 590));
        primaryStage.show();
        if(self.isLost()){

            this.stage.close();
            lose = new loseUI();
            lose.showWindow();
        }
        if (self.isWin()) {
            showSecondWindow("You win");
        }
    }

    public ScrollPane getScrollPane(){
        ScrollPane scroll = new ScrollPane();
        Text t = new Text(scrollText);
        t.setStyle("-fx-font: 14 arial;");
        t.setLayoutX(50);
        t.setLayoutY(25);
        scroll.setPrefSize(850, 120);
        scroll.setContent(t);
        scroll.setLayoutX(25);
        scroll.setLayoutY(10);
        return scroll;
    }

    private void actMoveSpy(Player self, List<Action> nonAffectActions) {
        Group g = new Group();
        Map<Integer, Integer> spyMap = self.getSpiesMap();

        Text t = new Text("source territory");
        t.setLayoutX(30);
        t.setLayoutY(20);
        ChoiceBox<Integer> source = new ChoiceBox<>();
        source.setLayoutX(170);
        source.setLayoutY(10);
        for(Integer i:spyMap.keySet()){
            source.getItems().add(i);
        }

        Text t1 = new Text("destination territory");
        t1.setLayoutX(30);
        t1.setLayoutY(90);
        ChoiceBox<Integer> destinate = new ChoiceBox<>();
        destinate.setLayoutX(170);
        destinate.setLayoutY(80);
        Set<Integer> territoryIds = new HashSet<>();
        Map<Integer, Player> players = App.cc.getGameBoard().getPlayers();
        for (Map.Entry<Integer, Player> entry : players.entrySet()) {
            Player p = entry.getValue();
            for (Integer territoryId : p.getOwnedTerritories()) {
                territoryIds.add(territoryId);
            }
        }
        for(Integer i:territoryIds){
            destinate.getItems().add(i);
        }


        Text t2 = new Text("number");
        t2.setLayoutX(30);
        t2.setLayoutY(160);
        TextField spy_num = new TextField();
        spy_num.setLayoutX(170);
        spy_num.setLayoutY(150);

        Button b = new Button("submit");
        b.setLayoutX(370);
        b.setLayoutY(210);
        Stage secondStage = new Stage();
        b.setOnAction(e-> {
            try {
                doMoveSpy(source, destinate, spy_num, nonAffectActions, secondStage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        g.getChildren().addAll(t,source, t1, destinate, b, t2, spy_num);
        Scene moveScene = new Scene(g, 360, 250);

        secondStage.setScene(moveScene);
        secondStage.show();
    }

    private void doMoveSpy(ChoiceBox<Integer> source, ChoiceBox<Integer> destinate, TextField spy_num,
                           List<Action> nonAffectActions, Stage secondStage) throws Exception {
        if(source.getValue()==null||destinate.getValue()==null){
            showSecondWindow("Invalid input");
            this.showWindow();
            return;
        }
        String output = "";
        output += source.getValue()+","+destinate.getValue()+","+spy_num.getText();
        System.out.println(output);
        secondStage.close();

        try {
            App.cc.conductMoveSpy(nonAffectActions, output);
            scrollText = "move spy success";
//            showSecondWindow("Instruction: "+output+"\n"+"move spy success");
        } catch (InvalidInputException e) {
            showSecondWindow(e.getMessage());
        } catch (InvalidActionException e) {
            showSecondWindow(e.getMessage());
        }
        this.showWindow();
    }

    private void actTrainSpy(Player self, List<Action> nonAffectActions) {
        Group g = new Group();

        Text t = new Text("target territory");
        t.setLayoutX(30);
        t.setLayoutY(30);
        ChoiceBox<Integer> terr_id= new ChoiceBox<>();
        terr_id.setLayoutX(150);
        terr_id.setLayoutY(20);

        Text t2 = new Text("number");
        t2.setLayoutX(30);
        t2.setLayoutY(120);
        TextField unit_num = new TextField();
        unit_num.setLayoutX(150);
        unit_num.setLayoutY(110);

        Stage secondStage = new Stage();

        Button b = new Button("submit");
        b.setLayoutX(300);
        b.setLayoutY(210);
        b.setOnAction(e-> {
            try {
                if (!App.cc.checkUserStatus()) {
                    doTrainSpy(terr_id, unit_num, secondStage, nonAffectActions);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        for(Integer i: self.getOwnedTerritories()){
            System.out.println(i);
            terr_id.getItems().add(i);
        }

        g.getChildren().addAll(t, t2, terr_id, unit_num, b);
        Scene upgradeScene = new Scene(g, 380, 250);

        secondStage.setScene(upgradeScene);
        secondStage.show();

    }

    private void doTrainSpy(ChoiceBox<Integer> terr_id, TextField unit_num, Stage secondStage, List<Action> nonAffectActions) throws Exception {
        if(terr_id.getValue()==null){
            showSecondWindow("Invalid input");
            this.showWindow();
            return;
        }
        String output = "";
        output += terr_id.getValue()+","+unit_num.getText();
        System.out.println(output);
        secondStage.close();
        try {
            App.cc.conductTrainSpy(nonAffectActions, output);
            scrollText = "train spy successfully";
//            showSecondWindow("Instruction: "+output+"\n"+"train spy successfully");
        } catch (InvalidInputException | InvalidActionException e) {
            showSecondWindow(e.getMessage());
        }
        this.showWindow();
    }


    public void finishThisRoll(List<Action> attackActions, List<Action> missileAttackActions,
                               List<Action> nonAffectActions) throws Exception {
        if (App.cc.checkUserStatus()) {
            return;
        }
        showSecondWindow("Please wait for other users finishing their commit...");
        String log  = App.cc.moveAndAttack(attackActions, missileAttackActions, nonAffectActions);
        scrollText = log;
        this.showWindow();

        attackActions.clear();
        missileAttackActions.clear();
        nonAffectActions.clear();
        App.updateTerritories(oldVisibleTerritories);
    }

    public void actMove(Player player, List<Action> moveActions){
        Group g = new Group();
        Map<UnitType, Integer> unitMap = player.getTotalUnitsMap();
        Map<UnitType, TextField> nums = unitsGroup(unitMap, g);
        Text t = new Text("source territory");
        t.setLayoutX(30);
        t.setLayoutY(30);
        TextField num = new TextField();
        num.setLayoutX(150);
        num.setLayoutY(20);

        Text t1 = new Text("destination territory");
        t1.setLayoutX(30);
        t1.setLayoutY(70);
        TextField num1 = new TextField();
        num1.setLayoutX(150);
        num1.setLayoutY(60);

        Button b = new Button("submit");
        b.setLayoutX(370);
        b.setLayoutY(570);
        Stage secondStage = new Stage();
        b.setOnAction(e-> {
            try {
                if (!App.cc.checkUserStatus()) {
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
        t.setLayoutY(30);
        TextField num = new TextField();
        num.setLayoutX(150);
        num.setLayoutY(20);

        Text t1 = new Text("destination territory");
        t1.setLayoutX(30);
        t1.setLayoutY(70);
        TextField num1 = new TextField();
        num1.setLayoutX(150);
        num1.setLayoutY(60);

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
        secondStage.close();
        try{
            App.cc.conductMoveOrAttack(moveActions, 0, output);
            scrollText = "move action success";
//            showSecondWindow("Instruction: "+output+"\n"+"move action success");
        }catch (NumberFormatException e){
            showSecondWindow(e.getMessage());
        } catch (InvalidInputException e) {
            showSecondWindow(e.getMessage());
        } catch (InvalidActionException e) {
            showSecondWindow(e.getMessage());
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
        secondStage.close();
        try{
            App.cc.conductMoveOrAttack(attackActions, 1, output);
            scrollText = "units attack action success";
        }catch (NumberFormatException e){
            showSecondWindow(e.getMessage());
        } catch (InvalidInputException e) {
            showSecondWindow(e.getMessage());
        } catch (InvalidActionException e) {
            showSecondWindow(e.getMessage());
        }
        this.showWindow();
    }


    public void actAttackMissile(Player player, List<Action> missileAttackActions){
        Set<Integer> territoryIds = new HashSet<>();
        Map<Integer, Player> players = App.cc.getGameBoard().getPlayers();
        for (Map.Entry<Integer, Player> entry : players.entrySet()) {
            Player p = entry.getValue();
            for (Integer territoryId : p.getOwnedTerritories()) {
                territoryIds.add(territoryId);
            }
        }

        for(int i: player.getOwnedTerritories()){
            if(territoryIds.contains(i)){
                territoryIds.remove(i);
            }
        }
        Group g = new Group();
        Text t = new Text("Destination territory");
        t.setLayoutX(20);
        t.setLayoutY(20);
        ChoiceBox<Integer> enemy = new ChoiceBox<>();
        enemy.setLayoutX(150);
        enemy.setLayoutY(10);
        for(Integer i : territoryIds){
            enemy.getItems().add(i);
        }

        Text t1 = new Text("missileType");
        t1.setLayoutX(20);
        t1.setLayoutY(120);
        ChoiceBox<String> missile = new ChoiceBox<>();
        missile.setLayoutX(150);
        missile.setLayoutY(110);
        for(MissileType i:player.getMissiles().keySet()){
            missile.getItems().add(i.toString());
        }
        Stage secondStage = new Stage();

        Button b = new Button("submit");
        b.setLayoutX(190);
        b.setLayoutY(200);
        b.setOnAction(e-> {
            try {
                doMissileAttack(enemy, missile, missileAttackActions, secondStage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        g.getChildren().addAll(t, t1, enemy, missile, b);
        Scene missileScene = new Scene(g, 280, 260);

        secondStage.setScene(missileScene);
        secondStage.show();

    }


    public void actCloakResearch(Player player, List<Action> cloakResearch) throws Exception {
        try {
            App.cc.conductCloakRearch(player, cloakResearch);
            scrollText = "cloak research success";
//            showSecondWindow("cloak research success");
        } catch (InvalidActionException e) {
            showSecondWindow(e.getMessage());
        }
        this.showWindow();
    }

    public void actCloakTerritory(Player self, List<Action> cloakTerritoryActions){
        Group g = new Group();
        Set<Integer> territoryIds = self.getOwnedTerritories();
        Text t = new Text("territory id");
        t.setLayoutX(20);
        t.setLayoutY(20);
        ChoiceBox<Integer> terr_id = new ChoiceBox<>();
        terr_id.setLayoutX(130);
        terr_id.setLayoutY(10);
        for(int i: territoryIds){
            terr_id.getItems().add(i);
        }
        Stage secondStage = new Stage();
        Button b = new Button("submit");
        b.setLayoutX(170);
        b.setLayoutY(80);
        b.setOnAction(e-> {
            try {
                doCloakTerritory(self, terr_id, cloakTerritoryActions, secondStage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        g.getChildren().addAll(t, terr_id, b);
        Scene cloakScene = new Scene(g, 250, 140);
        secondStage.setScene(cloakScene);
        secondStage.show();

    }

    private void doCloakTerritory(Player self, ChoiceBox<Integer> terr_id,
                                  List<Action> cloakTerritoryActions, Stage secondStage) throws Exception {
        if(terr_id.getValue()==null){
            showSecondWindow("Invalid input");
            this.showWindow();
            return;
        }
        int output = terr_id.getValue();
        System.out.println(output);
        secondStage.close();
        try {
            App.cc.conductCloakTerritory(self, cloakTerritoryActions, output);
            scrollText = "cloak territory successfully";
//            showSecondWindow("cloak territory successfully");
        } catch (InvalidActionException e) {
            showSecondWindow(e.getMessage());
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
        terr_id.setLayoutY(20);

        Text t1 = new Text("unit type");
        t1.setLayoutX(30);
        t1.setLayoutY(120);
        ChoiceBox<String> unit_id= new ChoiceBox<>();
        unit_id.setLayoutX(150);
        unit_id.setLayoutY(110);

        Text t2 = new Text("unit number");
        t2.setLayoutX(30);
        t2.setLayoutY(220);
        TextField unit_num = new TextField();
        unit_num.setLayoutX(150);
        unit_num.setLayoutY(210);

        Stage secondStage = new Stage();

        Button b = new Button("submit");
        b.setLayoutX(300);
        b.setLayoutY(310);
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
            terr_id.getItems().add(i);
        }
        for(UnitType i : unitMap.keySet()){
            unit_id.getItems().add(i.toString());
        }
        g.getChildren().addAll(t, t1, t2, terr_id, unit_id, unit_num, b);
        Scene upgradeScene = new Scene(g, 380, 350);

        secondStage.setScene(upgradeScene);
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
        try {
            App.cc.conductUpgradeUnits(upgradeUnitsActions, output);
            scrollText = "upgrade successfully";
//            showSecondWindow("Instruction: "+output+"\n"+"upgrade successfully");
        } catch (InvalidInputException | InvalidActionException e) {
            showSecondWindow(e.getMessage());
        }
        this.showWindow();
    }

    private void doMissileAttack(ChoiceBox<Integer> enemy, ChoiceBox<String> missile,
                                 List<Action> missileAttackActions, Stage secondStage) throws Exception {
        if(enemy.getValue()==null||missile.getValue()==null){
            showSecondWindow("Invalid input");
            this.showWindow();
            return;
        }
        String output = enemy.getValue()+","+missile.getValue().substring(10);
        System.out.println(output);
        secondStage.close();
        try{
            App.cc.conductMissileAttack(missileAttackActions, output);
            scrollText = "missile attack successfully";
//            showSecondWindow("Instruction: "+output+"\n"+"missile attack successfully");
        }catch (InvalidActionException | InvalidInputException e){
            showSecondWindow(e.getMessage());
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
                scrollText = "Upgrade tech success";
//                showSecondWindow("Upgrade tech success");
            } catch (InvalidActionException e) {
                showSecondWindow(e.getMessage());
            }

        } else {
            scrollText = "Already upgraded in this turn";
//            showSecondWindow("Already upgraded in this turn");
        }
        this.showWindow();
    }

    public Map<UnitType, TextField> unitsGroup(Map<UnitType, Integer> units, Group g){
        int location =0;
        Map<UnitType, TextField> nums = new HashMap<>();
        for(UnitType i : units.keySet()){
            Text t = new Text(i.toString());
            t.setLayoutX(30);
            t.setLayoutY(30+40*location+80);
            TextField num = new TextField();
            num.setLayoutX(150);
            num.setLayoutY(20+40*location+80);
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


    public static void main(String[] args) {
        launch(args);
    }

    public void  showWindow() throws Exception {
        start(stage);
    }



}
