package edu.duke.risc.client;

import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable{
    public Stage mainStage;//to be closed
    private placement placementPage;
    private actionChoose actionChoosePage;

    public mainController() {
        //auto generated
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ignore
    }

    public void connectToServer() throws Exception {
        showConnectingScene();
        App.cc.tryConnectAndWait();
        App.initializeTerritories();
        placementPage = new placement();
        placementPage.showWindow();
        mainStage.close();//close the first window after connect

    }

    private void showConnectingScene() {
        Text msg = new Text("Successfully connected to server, now wait for other players...");
        msg.setLayoutX(50);
        msg.setLayoutY(100);
        Group g= new Group();
        g.getChildren().add(msg);
        Scene waitOthers = new Scene(g, 400, 300);
        this.mainStage.setScene(waitOthers);
    }


    public void startGame() throws Exception {
        connectToServer();

        //placementPage.close();
    }



    public void setStage(Stage primaryStage) {
        this.mainStage = primaryStage;
    }
}
