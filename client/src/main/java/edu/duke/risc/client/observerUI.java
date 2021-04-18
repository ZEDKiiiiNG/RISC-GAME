
package edu.duke.risc.client;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.PayloadType;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class observerUI extends Application{
    Stage stage=new Stage();
    String scrollText = "You are in the observer mode" ;

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

        String territoryInfo = "";

        Text a1 = new Text("You lost the game, entering Observer Mode, you can hit exit button to quit...");
        a1.setLayoutX(0);
        a1.setLayoutY(10);
        Group g1= new Group();
        g1.getChildren().add(a1);

        //scrollPane
        g1.getChildren().add(getScrollPane());

        //exit button
        javafx.scene.control.Button exit = new javafx.scene.control.Button("exit");
        exit.setLayoutX(950);
        exit.setLayoutY(590);
        exit.setOnAction(e-> {
            try {
                exitGame(self);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        //add territories
        for(TerritoryUI terrUI : App.TerrUIs){
            if(terrUI.isVisible()) {
                g1.getChildren().addAll(terrUI.getPane());

                int id = terrUI.getId();
                Territory terr = gameBoard.getTerritories().get(id);
                territoryInfo += gameBoard.getDisplayer().displaySingleTerritory(gameBoard, terr)+"\n";
            }
        }
        Text intro = new Text(territoryInfo);
        intro.setLayoutX(550);
        intro.setLayoutY(160);
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(intro);
        scroll.setPrefSize(330, 400);
        scroll.setLayoutX(550);
        scroll.setLayoutY(160);
        g1.getChildren().add(intro);


        g1.getChildren().add(exit);
        Scene techScene = new Scene(g1, 1000, 620);
        primaryStage.setScene(techScene);
        primaryStage.setTitle("Now in observer mode, you can exit anytime. (GameID = " + App.cc.getGameId()+")");
        primaryStage.show();
        updateObserver();
    }

    public void updateObserver() throws Exception {
        while (true) {
            App.cc.waitAndReadServerResponse();
            scrollText = App.cc.getLoggerInfo();
            this.showWindow();
        }
    }

    public ScrollPane getScrollPane(){
        ScrollPane scroll = new ScrollPane();
        Text t = new Text(scrollText);
        t.setStyle("-fx-font: 14 arial;");
        t.setLayoutX(50);
        t.setLayoutY(45);
        scroll.setPrefSize(950, 100);
        scroll.setContent(t);
        scroll.setLayoutX(25);
        scroll.setLayoutY(10);
        return scroll;
    }

    private void exitGame(Player self) throws IOException {
        PayloadObject request = new PayloadObject(self.getId(), Configurations.MASTER_ID, PayloadType.QUIT);
        App.cc.getCommunicator().writeMessage(request);
        System.exit(0);
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void  showWindow() throws Exception {
        start(stage);
    }

}
