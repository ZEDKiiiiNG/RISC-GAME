package edu.duke.risc.client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class placement extends Application {
    Stage stage=new Stage();
    @FXML
    private Text whoami;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Enum myColor = App.cc.getMyself().getColor();
        setWhoami("Your are the " + myColor + " player, please place your units");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("placement.fxml")));
        primaryStage.setTitle("Place your units");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void  showWindow() throws Exception {
        start(stage);
    }

    public void setWhoami(String data){
        whoami.setText(data);
    }
}
