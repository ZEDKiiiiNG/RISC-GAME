package edu.duke.risc.client;

import edu.duke.risc.shared.commons.UserColor;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class WyomingUI extends TerritoryUI {
    public WyomingUI(Color color) {
        setId(3);
        //
        Button newButton = new javafx.scene.control.Button("Wyoming(3)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(400,0,200, 200);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(400);
        newPane.setLayoutY(0);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
