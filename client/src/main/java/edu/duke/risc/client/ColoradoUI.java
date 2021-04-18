package edu.duke.risc.client;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ColoradoUI extends TerritoryUI {
    public ColoradoUI(Color color) {
        setId(4);
        //
        Button newButton = new javafx.scene.control.Button("Colorado(4)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(415,290,110, 170);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(415);
        newPane.setLayoutY(290);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
