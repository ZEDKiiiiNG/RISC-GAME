package edu.duke.risc.client;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CaliforniaUI extends TerritoryUI {
    public CaliforniaUI(Color color) {
        setId(7);
        //
        Button newButton = new javafx.scene.control.Button("California(7)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(25,430,200, 120);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(25);
        newPane.setLayoutY(430);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
