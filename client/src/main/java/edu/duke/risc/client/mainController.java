package edu.duke.risc.client;

import javafx.fxml.Initializable;
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
        App.cc.tryConnectAndWait();
        App.initializeTerritories();
        placementPage = new placement();
        placementPage.showWindow();
        mainStage.close();//close the first window after connect

    }


    public void startGame() throws Exception {
        connectToServer();

        //placementPage.close();
    }



    public void setStage(Stage primaryStage) {
        this.mainStage = primaryStage;
    }
}
