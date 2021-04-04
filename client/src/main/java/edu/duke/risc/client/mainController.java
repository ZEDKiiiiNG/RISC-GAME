package edu.duke.risc.client;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class mainController{


    public void changeWindow() throws Exception {
        UI_Second second = new UI_Second();
        second.showWindow();
    }


}
