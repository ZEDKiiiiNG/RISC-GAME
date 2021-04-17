package edu.duke.risc.client;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NevadaUI extends TerritoryUI {
    public NevadaUI(Color color) {
        setId(1);
        //
        Button newButton = new javafx.scene.control.Button("Nevada(1)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(225,290,90, 170);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(225);
        newPane.setLayoutY(290);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
