package edu.duke.risc.client;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class TerritoryUITest {
    private static TerritoryUI territoryUI;
    public static ArrayList<TerritoryUI> TerrUIs = new ArrayList<TerritoryUI>();

    public static Map<Integer, String> OldVisibleTerritories = new HashMap<>();


    @Test
    public void territoryUITest(FxRobot robot){
        territoryUI = new TerritoryUI() {
            @Override
            public void setId(int id) {
                super.setId(id);
            }

            @Override
            public void setRect(Rectangle rect) {
                super.setRect(rect);
            }

            @Override
            public void setPane(StackPane pane) {
                super.setPane(pane);
            }

            @Override
            public void setButton(Button button) {
                super.setButton(button);
            }

            @Override
            public void setTerritoryColor(Color territoryColor) {
                super.setTerritoryColor(territoryColor);
            }

            @Override
            public Rectangle getRect() {
                return super.getRect();
            }

            @Override
            public StackPane getPane() {
                return super.getPane();
            }

            @Override
            public Button getButton() {
                return super.getButton();
            }

            @Override
            public Color getTerritoryColor() {
                return super.getTerritoryColor();
            }

            @Override
            public void setVisible() {
                super.setVisible();
            }

            @Override
            public boolean isVisible() {
                return super.isVisible();
            }

            @Override
            public int getId() {
                return super.getId();
            }
        };
        territoryUI = new UtahUI(Color.RED);
        territoryUI.setTerritoryColor(Color.WHITE);
        assertEquals(Color.WHITE, territoryUI.getTerritoryColor());
        territoryUI.setId(1);
        assertEquals(1, territoryUI.getId());
        territoryUI.setVisible();
        assertEquals(true, territoryUI.isVisible());
        Rectangle rect = new Rectangle();
        territoryUI.setRect(rect);
        assertEquals(rect, territoryUI.getRect());
        StackPane pane = new StackPane();
        territoryUI.setPane(pane);
        territoryUI.getId();
        assertEquals(pane, territoryUI.getPane());
        TerrUIs.add(new NevadaUI(Color.RED));
        TerrUIs.add(new IdahoUI(Color.RED));
        TerrUIs.add(new WyomingUI(Color.RED));
        TerrUIs.add(new ColoradoUI(Color.RED));
        TerrUIs.add(new NewMexicoUI(Color.RED));
        TerrUIs.add(new ArizonaUI(Color.RED));
        TerrUIs.add(new CaliforniaUI(Color.RED));
        TerrUIs.add(new OregonUI(Color.RED));
        TerrUIs.add(new WashingtonUI(Color.RED));
    }

}