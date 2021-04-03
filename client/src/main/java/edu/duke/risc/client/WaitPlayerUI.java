package edu.duke.risc.client;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;



public class WaitPlayerUI extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        Text a = new Text("Please wait other players");
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
