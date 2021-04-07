package edu.duke.risc.client;

import edu.duke.risc.shared.users.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;



public class WaitPlayerUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Player self = App.cc.getMyself();
        String text = "You are the current the player: "+ self.toString();
        Text a = new Text(text);
        a.setLayoutX(100);
        a.setLayoutY(300);
        Group g= new Group();
        g.getChildren().add(a);


        Scene scene_action = new Scene(g, 1000, 600);

        //Setting title to the Stage
        stage.setTitle("RISC");

        //Adding scene to the stage
        stage.setScene(scene_action);

        //Displaying the contents of the stage
        stage.show();
    }
    public static void main(String args[]){
        launch(args);
    }

}
