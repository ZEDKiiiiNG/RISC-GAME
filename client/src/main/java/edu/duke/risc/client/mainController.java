package edu.duke.risc.client;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable{
    public Stage mainStage;//to be closed

    public mainController() {
        //auto generated
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ignore
    }

    public void connectToServer(ActionEvent actionEvent) throws Exception {
        App.cc.tryConnectAndWait();
        App.initializeTerritories();
        placement placementPage = new placement();
        placementPage.showWindow();
        mainStage.close();//close the first window after connect
    }


    public void setStage(Stage primaryStage) {
        this.mainStage = primaryStage;
    }
}
