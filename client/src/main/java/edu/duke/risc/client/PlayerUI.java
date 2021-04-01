package edu.duke.risc.client;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.board.GameBoard;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/*
finish the UI seeting of client
 */
public class PlayerUI extends Application implements EventHandler<ActionEvent> {
    /**
     * The game board
     */

    private GameBoard gameBoard;

    /**
     * Current player id
     */
    private Integer playerId = Configurations.DEFAULT_PLAYER_ID;

    Button attack;


    public PlayerUI() {

    }

    public void start(Stage stage) throws Exception {
        //Player player = this.gameBoard.getPlayers().get(playerId);
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        //Label l = new Label("Hello, Xunyu " + javafxVersion + ", running on Java " + javaVersion + ".");
        GridPane gp = new GridPane();
        String[] labels = new String[]{"+", "-", "*", "/",
                "7", "8", "9",
                "4", "5", "6",
                "1", "2", "3",
                "."};
        int rows[] = new int[]{0, 0, 0, 0,
                1, 1, 1,
                2, 2, 2,
                3, 3, 3,
                4};
        int cols[] = new int[]{0, 1, 2, 3,
                0, 1, 2,
                0, 1, 2,
                0, 1, 2,
                2};
        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            gp.getColumnConstraints().add(cc);
        }
        for (int i = 0; i < 5; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(20);
            gp.getRowConstraints().add(rc);
        }

        for (int i = 0; i < labels.length; i++) {
            Button b = new Button(labels[i]);
            gp.add(b, cols[i], rows[i]);
            b.setMaxWidth(Double.MAX_VALUE); //Note this change
            b.setMaxHeight(Double.MAX_VALUE); //Note this change
        }
        Button b = new Button("0");
        b.setMaxWidth(Double.MAX_VALUE); //Note this change
        b.setMaxHeight(Double.MAX_VALUE); //Note this change
        gp.add(b, 0, 4, 2, 1);
        b = new Button("E\nn\nt\ne\nr");
        b.setMaxWidth(Double.MAX_VALUE); //Note this change
        b.setMaxHeight(Double.MAX_VALUE); //Note this change
        gp.add(b, 3, 1, 1, 3);
//        URL xmlResource = getClass().getResource("/ui/calcbuttons.xml");
//        GridPane gp = FXMLLoader.load(xmlResource);


        attack = new Button();
        attack.setText("Attack");
        attack.setOnAction(this);
        StackPane layout = new StackPane();
        layout.getChildren().add(attack);
        Scene scene = new Scene(layout, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == attack) {
            System.out.println("OK");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}




