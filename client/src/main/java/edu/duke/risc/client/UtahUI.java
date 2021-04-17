package edu.duke.risc.client;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UtahUI extends TerritoryUI {
    public UtahUI(Color color) {
        setId(0);
        //
        Button newButton = new javafx.scene.control.Button("Utah(0)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(350,200,100, 250);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(350);
        newPane.setLayoutY(200);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
