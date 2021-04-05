package edu.duke.risc.client;

import edu.duke.risc.shared.commons.UserColor;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class CaliforniaUI extends TerritoryUI {
    public CaliforniaUI(Color color) {
        setId(7);
        //
        Button newButton = new javafx.scene.control.Button("California(7)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(0,400,250, 200);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(0);
        newPane.setLayoutY(400);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
