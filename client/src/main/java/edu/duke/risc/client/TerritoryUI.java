package edu.duke.risc.client;

import edu.duke.risc.shared.commons.UserColor;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class TerritoryUI {
    private int id;
    private  Rectangle rect;
    private  StackPane pane;
    private  javafx.scene.control.Button button;
    private Color TerritoryColor;
    private boolean visible = false;

    public void setId(int id) {
        this.id = id;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public void setPane(StackPane pane) {
        this.pane = pane;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void setTerritoryColor(Color territoryColor) {
        TerritoryColor = territoryColor;
        //
        rect.setFill(territoryColor);
        //
    }

    public Rectangle getRect() {
        return rect;
    }

    public StackPane getPane() {
        return pane;
    }

    public Button getButton() {
        return button;
    }

    public Color getTerritoryColor() {
        return TerritoryColor;
    }

    /*
    * if territory belongs to one of our players
    * then set it as visible
    * */
    public void setVisible() {
        visible = true;
    }

    /*
    * returns true if the territory is visible
    * called to determine whether to show territory on scene or not*/
    public boolean isVisible(){
        return visible;
    }

    public int getId() {
        return id;
    }
}
