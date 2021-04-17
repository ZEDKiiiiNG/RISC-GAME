package edu.duke.risc.client;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WashingtonUI extends TerritoryUI {
    public WashingtonUI(Color color) {
        setId(9);
        //
        Button newButton = new javafx.scene.control.Button("Washington(9)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(25,150,160, 140);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(25);
        newPane.setLayoutY(150);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
