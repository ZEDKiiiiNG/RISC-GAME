/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.risc.client;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

/**
 *
 */
public class App extends Application {
    public String getGreeting() {
        return "Hello world from client.";
    }
    Button button;
    public static void main(String[] args) throws IOException {
        //ClientController clientController = new ClientController();
        //clientController.startGame();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Feng Wang");
        button = new Button("test UI- Feng Wang");
        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
