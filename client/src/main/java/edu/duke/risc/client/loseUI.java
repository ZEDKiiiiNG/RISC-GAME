package edu.duke.risc.client;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.commons.PayloadType;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class loseUI extends Application {
    Stage stage=new Stage();
    private observerUI observer;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Player self = App.cc.getMyself();
        int gameId = App.cc.getGameId();
        startLoseUI(primaryStage, self, gameId);

    }

    public void startLoseUI(Stage primaryStage, Player self, int gameId){
        Group g = new Group();//all widgets

        //introduce text
        String introduce = "player "+self.getColor().toString()+"\n"+
                "You lose ";
        Text intro = new Text(introduce);
        intro.setLayoutX(20);
        intro.setLayoutY(50);
        g.getChildren().add(intro);
        //button commit
        javafx.scene.control.Button exit = new javafx.scene.control.Button("exit");
        exit.setLayoutX(50);
        exit.setLayoutY(100);
        exit.setOnAction(e-> {
            try {
                exitGame(self);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        g.getChildren().add(exit);

        //button observe
        javafx.scene.control.Button observe = new javafx.scene.control.Button("observe");
        observe.setLayoutX(50);
        observe.setLayoutY(150);
        observe.setOnAction(e-> {
            try {
                turnToObserve();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        if(!App.cc.getCommunicator().getSocket().isClosed()){
            g.getChildren().add(observe);
        }

        primaryStage.setTitle("Lose(GameID = " + gameId +")");
        primaryStage.setScene(new Scene(g, 400, 300));
        primaryStage.show();
    }

    public  void turnToObserve() throws Exception {
        this.stage.close();
        observer = new observerUI();
        observer.showWindow();
    }

    public void exitGame(Player self) throws IOException {
        if(!App.cc.getCommunicator().getSocket().isClosed()){
            PayloadObject request = new PayloadObject(self.getId(), Configurations.MASTER_ID, PayloadType.QUIT);
            App.cc.getCommunicator().writeMessage(request);
        }
        System.exit(0);
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void  showWindow() throws Exception {
        start(stage);
    }

}
