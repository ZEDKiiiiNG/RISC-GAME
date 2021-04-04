package edu.duke.risc.client;

import edu.duke.risc.shared.commons.UserColor;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class ArizonaUI extends TerritoryUI {
    public ArizonaUI(Color color) {
        setId(6);
        //
        Button newButton = new javafx.scene.control.Button("Arizona(6)");
        setButton(newButton);
        //
        Rectangle newRect = new Rectangle(250,450,150, 150);
        newRect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        newRect.setFill(color);
        setRect(newRect);
        //
        StackPane newPane = new StackPane();
        newPane.setLayoutX(250);
        newPane.setLayoutY(450);
        newPane.getChildren().addAll(newRect, newButton);
        setPane(newPane);
        //
        setTerritoryColor(color);
    }
}
