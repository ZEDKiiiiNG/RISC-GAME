package edu.duke.risc.client;

import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;




public class winUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        Player self = App.cc.getMyself();
        startWinUI(primaryStage, self);

    }

    public void startWinUI(Stage primaryStage, Player self){
        Group g = new Group();//all widgets

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
                exitGame();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        g.getChildren().add(exit);

        primaryStage.setTitle("Win");
        primaryStage.setScene(new Scene(g, 400, 300));
        primaryStage.show();
    }

    public void exitGame() throws IOException {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
