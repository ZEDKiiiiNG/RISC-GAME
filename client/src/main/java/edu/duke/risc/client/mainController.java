package edu.duke.risc.client;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable{

    public mainController() {
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ignore
    }

    public void connectToServer(ActionEvent actionEvent) throws Exception {
        App.cc.tryConnectAndWait();
        placement wait = new placement();
        wait.showWindow();
    }
}
