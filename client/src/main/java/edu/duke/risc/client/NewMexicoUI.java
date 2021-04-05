package edu.duke.risc.client;

import edu.duke.risc.shared.commons.UserColor;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class NewMexicoUI extends TerritoryUI {
    public NewMexicoUI(Color color) {
        setId(5);
        //
        Button newButton = new javafx.scene.control.Button("New Mexico(5)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(400,450,200, 150);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(400);
        newPane.setLayoutY(450);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
