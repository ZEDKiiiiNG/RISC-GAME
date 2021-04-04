package edu.duke.risc.client;

import edu.duke.risc.shared.commons.UserColor;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class NevadaUI extends TerritoryUI {
    public NevadaUI(Color color) {
        setId(1);
        //
        Button newButton = new javafx.scene.control.Button("Nevada(1)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(250,200,100, 250);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(250);
        newPane.setLayoutY(200);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
