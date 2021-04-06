package edu.duke.risc.client;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.actions.Action;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.PayloadType;
import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




public class winUI extends Application {
    Stage stage=new Stage();
    private String place ="";
    private actionChoose actionChoosePage;

    private List<Action> actions = new ArrayList<>();
    @FXML
    private Label whoami;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Player self = App.cc.getMyself();
        Group g = new Group();//all widgets
        GameBoard gameBoard = App.cc.getGameBoard();

        //introduce text
        String introduce = "player "+self.getColor().toString()+"\n"+
                "You win ";
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

        primaryStage.setTitle("Win");
        primaryStage.setScene(new Scene(g, 400, 300));
        primaryStage.show();

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


    public static void main(String[] args) {
        launch(args);
    }

    public void  showWindow() throws Exception {
        start(stage);
    }


}
