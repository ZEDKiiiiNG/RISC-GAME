/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.risc.client;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        primaryStage.setTitle("The very first stage");

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

