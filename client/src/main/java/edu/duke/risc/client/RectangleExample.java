package edu.duke.risc.client;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RectangleExample extends Application {


    public void displayTerritory(Rectangle rect, StackPane pane, Button b, Group g, Color color){
        rect.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        pane.getChildren().addAll(rect, b);
        rect.setFill(color);
        g.getChildren().add(pane);

    }

    @Override
    public void start(Stage stage) {
        Group g = new Group();


        Rectangle[] rects =  new Rectangle[10];
        StackPane[] panes = new StackPane[10];
        Button[] buttons = new Button[10];
        //Washington(9)

        rects[9] = new Rectangle(0,0,200, 200);
        buttons[9] = new Button("Washington(9)");
        panes[9] = new StackPane();
        displayTerritory(rects[9], panes[9], buttons[9], g, Color.RED);
        panes[9].setLayoutX(0);
        panes[9].setLayoutY(0);

        //Idaho(2)
        rects[2] = new Rectangle(200,0,200, 200);
        rects[2].setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        Text t2 = new Text("Idaho(2)");
        buttons[2] = new Button("Idaho(2)");
        panes[2] = new StackPane();
        displayTerritory(rects[2], panes[2], buttons[2], g, Color.RED);
        panes[2].setLayoutX(200);
        panes[2].setLayoutY(0);
        //Wyoming(3)
        rects[3] = new Rectangle(400,0,200, 200);
        rects[3].setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        Text t3 = new Text("Wyoming(3)");
        StackPane pane3 = new StackPane();
        pane3.getChildren().addAll(rects[3], t3);
        pane3.setLayoutX(400);
        pane3.setLayoutY(0);
        rects[3].setFill(Color.RED);
        g.getChildren().add(pane3);
        //Oregon(8)
        rects[8] = new Rectangle(0,200,250, 200);
        rects[8].setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        Text t8 = new Text("Oregon(8)");
        StackPane pane8 = new StackPane();
        pane8.getChildren().addAll(rects[8], t8);
        pane8.setLayoutX(0);
        pane8.setLayoutY(200);
        rects[8].setFill(Color.RED);
        g.getChildren().add(pane8);

        rects[7] = new Rectangle(0,400,250, 200);
        rects[7].setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        Text t7 = new Text("California(7)");
        StackPane pane7 = new StackPane();
        pane7.getChildren().addAll(rects[7],t7);
        pane7.setLayoutX(0);
        pane7.setLayoutY(400);
        rects[7].setFill(Color.RED);
        g.getChildren().add(pane7);

        rects[1] = new Rectangle(250,200,100, 250);
        rects[1].setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        Text t1 = new Text("Washington(9)");
        StackPane pane1 = new StackPane();
        pane1.getChildren().addAll(rects[1], t1);
        pane1.setLayoutX(250);
        pane1.setLayoutY(200);
        rects[1].setFill(Color.RED);
        g.getChildren().add(pane1);

        rects[0] = new Rectangle(350,200,100, 250);
        rects[0].setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        Text t0 = new Text("Utah(0)");
        StackPane pane0 = new StackPane();
        pane0.getChildren().addAll(rects[0], t0);
        pane0.setLayoutX(350);
        pane0.setLayoutY(200);
        rects[0].setFill(Color.GREEN);
        g.getChildren().add(pane0);

        rects[4] = new Rectangle(450,200,150, 250);
        rects[4].setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        Text t4 = new Text("Colorado(4)");
        StackPane pane4 = new StackPane();
        pane4.getChildren().addAll(rects[4], t4);
        pane4.setLayoutX(450);
        pane4.setLayoutY(200);
        rects[4].setFill(Color.YELLOW);
        g.getChildren().add(pane4);

        rects[6] = new Rectangle(250,450,150, 150);
        rects[6].setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        Text t6 = new Text("Arizona(6)");
        StackPane pane6 = new StackPane();
        pane6.getChildren().addAll(rects[6], t6);
        pane6.setLayoutX(250);
        pane6.setLayoutY(450);
        rects[6].setFill(Color.PURPLE);
        g.getChildren().add(pane6);

        rects[5] = new Rectangle(400,450,200, 150);
        rects[5].setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        Text t5 = new Text("New Mexico(5)");
        StackPane pane5 = new StackPane();
        pane5.getChildren().addAll(rects[5], t5);
        pane5.setLayoutX(400);
        pane5.setLayoutY(450);
        rects[5].setFill(Color.BLUE);
        g.getChildren().add(pane5);

        //Creating a scene object
        Scene scene = new Scene(g, 1000, 600);

        //Setting title to the Stage
        stage.setTitle("Drawing a Rectangle");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}