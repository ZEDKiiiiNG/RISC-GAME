
package edu.duke.risc.client;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.PayloadType;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;



public class observerUI extends Application implements Initializable {
    Stage stage=new Stage();
    private String place ="";
    private actionChoose actionChoosePage;

    private List<Action> actions = new ArrayList<>();
    @FXML
    private Label whoami;

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

        
        Text a1 = new Text("You lost the game, entering Observer Mode, you can hit exit button to quit...");
        a1.setLayoutX(650);
        a1.setLayoutY(0);
        Group g1= new Group();
        g1.getChildren().add(a1);
        //exit button
        javafx.scene.control.Button exit = new javafx.scene.control.Button("exit");
        exit.setLayoutX(950);
        exit.setLayoutY(0);
        exit.setOnAction(e-> {
            try {
                exitGame(self);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
//        //add territories
//        for(TerritoryUI terrUI : App.TerrUIs){
//            if(terrUI.isVisible()) {
//                int id = terrUI.getId();
//                Territory terr = gameBoard.getTerritories().get(id);
//                g1.getChildren().addAll(terrUI.getPane());
//                App.TerrUIs.get(id).getButton().setOnAction(e -> territoryInfoScene(gameBoard, terr));
//            }
//        }

        Map<Integer,Territory> territories = new HashMap<>();
        for(int i =0;i<10;i++){
            territories.put(i, App.cc.getGameBoard().getTerritories().get(i));
        }
        Map<Integer, String> terrInfos = new HashMap<>();
        for(int i = 0;i< 10;i++){
            terrInfos.put(i, gameBoard.getDisplayer().displaySingleTerritory(gameBoard, territories.get(i)));
        }
        for(int i = 0;i<10;i++){
            Text terr_info = new Text(terrInfos.get(i));
            terr_info.setLayoutX(610);
            terr_info.setLayoutY(20+75*i);
            g1.getChildren().add(terr_info);
        }
        Rectangle[] rects =  new Rectangle[10];
        TextField[] texts = new TextField[10];
        //Washington(9)

        rects[9] = new Rectangle(0,0,200, 200);
        texts[9] = new TextField(territories.get(9).toString());
        texts[9].setLayoutX(0);
        texts[9].setLayoutY(0);

        //Idaho(2)
        rects[2] = new Rectangle(200,0,200, 200);
        texts[2] = new TextField(territories.get(2).toString());
        texts[2].setLayoutX(200);
        texts[2].setLayoutY(0);

        //Wyoming(3)
        rects[3] = new Rectangle(400,0,200, 200);
        texts[3] = new TextField(territories.get(3).toString());
        texts[3].setLayoutX(400);
        texts[3].setLayoutY(0);

        //Oregon(8)
        rects[8] = new Rectangle(0,200,250, 200);
        texts[8] = new TextField(territories.get(8).toString());
        texts[8].setLayoutX(0);
        texts[8].setLayoutY(200);

        //
        rects[7] = new Rectangle(0,400,250, 200);
        texts[7] = new TextField(territories.get(7).toString());
        texts[7].setLayoutX(0);
        texts[7].setLayoutY(400);

        //Washington(9)
        rects[1] = new Rectangle(250,200,100, 250);
        texts[1] = new TextField(territories.get(1).toString());
        texts[1].setLayoutX(250);
        texts[1].setLayoutY(200);

        //Utah(0)
        rects[0] = new Rectangle(350,200,100, 250);
        texts[0] = new TextField(territories.get(0).toString());
        texts[0].setLayoutX(350);
        texts[0].setLayoutY(200);

        //Colorado(4)
        rects[4] = new Rectangle(450,200,150, 250);
        texts[4] = new TextField(territories.get(4).toString());
        texts[4].setLayoutX(450);
        texts[4].setLayoutY(200);

        //Arizona(6)
        rects[6] = new Rectangle(250,450,150, 150);
        texts[6] = new TextField(territories.get(6).toString());
        texts[6].setLayoutX(250);
        texts[6].setLayoutY(450);

        //New Mexico(5)
        rects[5] = new Rectangle(400,450,200, 150);
        texts[5] = new TextField(territories.get(5).toString());
        texts[5].setLayoutX(400);
        texts[5].setLayoutY(450);

        for(int i =0;i<10;i++){
            if(territoryIds.get(i)!=null){
                displayTerritory(rects[i], texts[i], g1, territoryIds.get(i));
            }
        }


        g1.getChildren().add(exit);
        Scene techScene = new Scene(g1, 1000, 800);
        primaryStage.setScene(techScene);
        primaryStage.setTitle("Now in observer mode, you can exit anytime. (GameID = " + App.cc.getGameId()+")");
        primaryStage.show();
//        this.showWindow();
        while (true) {
            App.cc.waitAndReadServerResponse();
            showSecondWindow(App.cc.getLoggerInfo());
            this.showWindow();
        }
    }

    public void displayTerritory(Rectangle rect, TextField b, Group g, Color color){
        rect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        rect.setFill(color);
        g.getChildren().addAll(rect, b);

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

    private void exit() {
        this.stage.close();
        System.exit(0);
    }

    private void exitGame(Player self) throws IOException {
//        App.cc.setReadExitThread(new ReadExitThread(null, App.cc.getCommunicator(), self.getId()));
        PayloadObject request = new PayloadObject(self.getId(), Configurations.MASTER_ID, PayloadType.QUIT);
        App.cc.getCommunicator().writeMessage(request);
//        App.cc.getReadExitThread().exit();
        System.exit(0);
    }


    public void displayLog(String log){
        Text a = new Text(log);
        a.setLayoutX(50);
        a.setLayoutY(100);
        Group g= new Group();
        g.getChildren().add(a);
        Stage secondStage = new Stage();
        Scene techScene = new Scene(g, 400, 300);
        secondStage.setScene(techScene);
        secondStage.showAndWait();//用户必须首先处理新的弹窗
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
