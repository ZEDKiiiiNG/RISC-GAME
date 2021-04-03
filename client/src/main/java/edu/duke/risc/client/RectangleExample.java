package edu.duke.risc.client;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class RectangleExample extends Application {

    private ClientController clientControler;
    public RectangleExample() throws IOException {
        this.clientControler = new ClientController();
    }


    public void displayTerritory(Rectangle rect, StackPane pane, Button b, Group g, Color color){
        rect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        pane.getChildren().addAll(rect, b);
        rect.setFill(color);
        g.getChildren().add(pane);

    }

    public StackPane actionButton(Stage stage, Scene commitScene, Scene moveScene, Scene attackScene, Scene upgradeScene, Scene techScene)throws Exception{
        StackPane pane = new StackPane();
        Button commit  = new Button("commit");
        commit.setLayoutX(650);
        commit.setLayoutY(0);
        commit.setOnAction(e->stage.setScene(commitScene));
        Button move = new Button("move");
        move.setLayoutX(650);
        move.setLayoutY(200);
        move.setOnAction(e->stage.setScene(moveScene));
        Button attack = new Button("attack");
        attack.setLayoutX(650);
        attack.setLayoutY(300);
        attack.setOnAction(e->stage.setScene(attackScene));
        Button upgrade = new Button("upgrade");
        upgrade.setLayoutX(650);
        upgrade.setLayoutY(400);
        upgrade.setOnAction(e->stage.setScene(upgradeScene));
        Button tech = new Button("tech");
        tech.setLayoutX(650);
        tech.setLayoutY(500);
        tech.setOnAction(e->stage.setScene(techScene));
        pane.getChildren().addAll(commit, move, attack, upgrade, tech);
        return pane;
    }



    public void actionChooseUI(Stage stage, GameBoard gameBoard, Scene commitScene, Scene moveScene, Scene attackScene, Scene upgradeScene, Scene techScene)throws Exception{
        Group g = new Group();


        Rectangle[] rects =  new Rectangle[10];
        StackPane[] panes = new StackPane[10];
        Button[] buttons = new Button[10];
        //Washington(9)

        rects[9] = new Rectangle(0,0,200, 200);
        buttons[9] = new Button("Washington(9)");
        Territory territory = gameBoard.findTerritory(9);
        buttons[9].setOnAction(e->stage.setScene(territoryInfoScene(gameBoard,territory)));
        panes[9] = new StackPane();
        panes[9].setLayoutX(0);
        panes[9].setLayoutY(0);

        //Idaho(2)
        rects[2] = new Rectangle(200,0,200, 200);
        buttons[2] = new Button("Idaho(2)");
        panes[2] = new StackPane();
        panes[2].setLayoutX(200);
        panes[2].setLayoutY(0);

        //Wyoming(3)
        rects[3] = new Rectangle(400,0,200, 200);
        buttons[3] = new Button("Wyoming(3)");
        panes[3] = new StackPane();
        panes[3].setLayoutX(400);
        panes[3].setLayoutY(0);

        //Oregon(8)
        rects[8] = new Rectangle(0,200,250, 200);
        buttons[8] = new Button("Oregon(8)");
        panes[8] = new StackPane();
        panes[8].setLayoutX(0);
        panes[8].setLayoutY(200);

        //California(7)
        rects[7] = new Rectangle(0,400,250, 200);
        buttons[7] = new Button("California(7)");
        panes[7] = new StackPane();
        panes[7].setLayoutX(0);
        panes[7].setLayoutY(400);

        //Washington(9)
        rects[1] = new Rectangle(250,200,100, 250);
        buttons[1] = new Button("Washington(9)");
        panes[1] = new StackPane();
        panes[1].setLayoutX(250);
        panes[1].setLayoutY(200);

        //Utah(0)
        rects[0] = new Rectangle(350,200,100, 250);
        buttons[0] = new Button("Utah(0)");
        panes[0] = new StackPane();
        panes[0].setLayoutX(350);
        panes[0].setLayoutY(200);

        //Colorado(4)
        rects[4] = new Rectangle(450,200,150, 250);
        buttons[4] = new Button("Colorado(4)");
        panes[4] = new StackPane();
        panes[4].setLayoutX(450);
        panes[4].setLayoutY(200);

        //Arizona(6)
        rects[6] = new Rectangle(250,450,150, 150);
        buttons[6] = new Button("Arizona(6)");
        panes[6] = new StackPane();
        panes[6].setLayoutX(250);
        panes[6].setLayoutY(450);

        //New Mexico(5)
        rects[5] = new Rectangle(400,450,200, 150);
        buttons[5] = new Button("New Mexico(5)");
        panes[5] = new StackPane();
        panes[5].setLayoutX(400);
        panes[5].setLayoutY(450);


        displayTerritory(rects[0], panes[0], buttons[0], g, Color.YELLOW);
        displayTerritory(rects[1], panes[1], buttons[1], g, Color.RED);
        displayTerritory(rects[2], panes[2], buttons[2], g, Color.RED);
        displayTerritory(rects[3], panes[3], buttons[3], g, Color.BLUE);
        displayTerritory(rects[4], panes[4], buttons[4], g, Color.RED);
        displayTerritory(rects[5], panes[5], buttons[5], g, Color.GREEN);
        displayTerritory(rects[6], panes[6], buttons[6], g, Color.RED);
        displayTerritory(rects[7], panes[7], buttons[7], g, Color.RED);
        displayTerritory(rects[8], panes[8], buttons[8], g, Color.RED);
        displayTerritory(rects[9], panes[9], buttons[9], g, Color.RED);

        //Setting title to the Stage
        stage.setTitle("RISC");
        StackPane p = actionButton(stage, commitScene, moveScene, attackScene, upgradeScene, techScene);
        g.getChildren().add(p);
        Scene scene_action = new Scene(g, 1000, 600);
        //Adding scene to the stage
        stage.setScene(scene_action);

        //Displaying the contents of the stage
        stage.show();
    }


    public Scene territoryInfoScene(GameBoard gameBoard, Territory territory){
        String terrInfo = gameBoard.getDisplayer().displaySingleTerritory(gameBoard, territory);
        Text a = new Text(terrInfo);
        a.setLayoutX(100);
        a.setLayoutY(300);
        Group g= new Group();
        g.getChildren().add(a);
        Scene terrInfoScene = new Scene(g, 600, 400);
        return terrInfoScene;
    }

    @Override
    public void start(Stage stage) {

        //Creating a scene object
        //actionChooseUI(stage, gameBoard, commitScene, moveScene, attackScene, upgradeScene, techScene);//


    }
    public static void main(String args[]){
        launch(args);
    }
}