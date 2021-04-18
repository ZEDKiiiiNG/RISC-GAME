package edu.duke.risc.client;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WyomingUI extends TerritoryUI {
    public WyomingUI(Color color) {
        setId(3);
        //
        Button newButton = new javafx.scene.control.Button("Wyoming(3)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(355,150,170, 140);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(355);
        newPane.setLayoutY(150);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
