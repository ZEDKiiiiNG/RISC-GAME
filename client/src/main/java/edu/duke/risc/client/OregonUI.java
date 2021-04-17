package edu.duke.risc.client;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class OregonUI extends TerritoryUI {
    public OregonUI(Color color) {
        setId(8);
        //
        Button newButton = new javafx.scene.control.Button("Oregon(8)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(25,290,200, 140);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(25);
        newPane.setLayoutY(290);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
