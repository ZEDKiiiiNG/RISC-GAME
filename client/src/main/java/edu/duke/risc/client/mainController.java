package edu.duke.risc.client;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.exceptions.InvalidPayloadContent;
import edu.duke.risc.shared.exceptions.UnmatchedReceiverException;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable{
    public Stage mainStage;//to be closed
    private placement placementPage;
    private actionChoose actionChoosePage;
    private observerUI observer;

    public mainController() {
        //auto generated
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ignore
    }

    public void connectToServerAndEnterPlacement() throws Exception {
        //showConnectingScene();
        App.cc.tryConnectAndWait();
        App.initializeTerritories();
        placementPage = new placement();
        placementPage.showWindow();
        //mainStage.close();//close the first window after connect
    }

    /*
    * just try connect and wait initialize territories*/
    public void connectToServer() throws Exception {
        System.out.println("c1?");
        showConnectingScene();
        System.out.println("c2?");
        App.cc.tryConnectAndWait();
        System.out.println("c3?");
        App.initializeTerritories();
    }

    private void showConnectingScene() {
        Text msg = new Text("Successfully connected to server, now wait for other players...");
        msg.setLayoutX(50);
        msg.setLayoutY(100);
        Group g= new Group();
        g.getChildren().add(msg);
        Scene waitOthers = new Scene(g, 400, 300);
        this.mainStage.setScene(waitOthers);
    }
    
    private void showUserPageScene(){
        Text msg = new Text("Register a new account or login.");
        msg.setLayoutX(50);
        msg.setLayoutY(50);
        //
        Button login = new Button("Login");
        login.setLayoutX(30);
        login.setLayoutY(150);
        //
        Button register = new Button("Register");
        register.setLayoutX(100);
        register.setLayoutY(150);
        //
        Label userID = new Label("User ID");
        Label userPassword = new Label("Password");
        userID.setLayoutX(40);
        userID.setLayoutY(100);
        userPassword.setLayoutX(40);
        userPassword.setLayoutY(120);
        //
        TextField ID = new TextField();
        TextField pass = new TextField();
        ID.setLayoutX(100);
        ID.setLayoutY(100);
        pass.setLayoutX(100);
        pass.setLayoutY(120);
        //
        Group g= new Group();
        g.getChildren().addAll(msg, login, register, userID, userPassword, ID, pass);
        Scene userLogin = new Scene(g, 400, 300);
        //set actions, get user Id and password from text field
        register.setOnAction(e -> showRegisterScene(ID, pass));
        login.setOnAction(e -> showLoginScene(ID.getText(), pass.getText()));
        this.mainStage.setScene(userLogin);
    }

    private void showRegisterScene(TextField ID, TextField pass){
        if(ID.getText()==""||pass.getText()==""){
            showFailedScene("User Id and password cannot be null, try again!");
            this.showUserPageScene();
            return;
        }
        String id = ID.getText();
        String passW= pass.getText();
        String choose = "R";
        String errorMsg = null;
        try {
            errorMsg = App.cc.logInValidate(choose, id, passW);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(errorMsg == null){//successfully registered
            //redirect
            showGameChooseScene();
        }
        else{//register failed
            showFailedScene(errorMsg);
        }
    }

    private void showFailedScene(String errorMsg) {
        Text msg = new Text(errorMsg);
        msg.setLayoutX(50);
        msg.setLayoutY(100);
        Group g= new Group();
        g.getChildren().add(msg);
        Scene waitOthers = new Scene(g, 400, 300);
        Stage newStage = new Stage();
        newStage.setScene(waitOthers);
        newStage.showAndWait();
    }

    private void showLoginScene(String id, String passW) {
        String choose = "L";
        String errorMsg = null;
        try {
            errorMsg = App.cc.logInValidate(choose, id, passW);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(errorMsg == null){//successfully registered
            //redirect to game choose page
            showGameChooseScene();

        }
        else{//register failed
            showFailedScene(errorMsg);
        }
    }

    private void showGameChooseScene() {
        Text msg = new Text("Create a new game or join an existing game.");
        msg.setLayoutX(50);
        msg.setLayoutY(50);
        //
        Button new_game = new Button("New Game");
        new_game.setLayoutX(50);
        new_game.setLayoutY(150);
        //
        Button join_game = new Button("Join Game");
        join_game.setLayoutX(50);
        join_game.setLayoutY(200);
        //
        Group g= new Group();
        g.getChildren().addAll(msg, new_game, join_game);
        Scene chooseGame = new Scene(g, 400, 300);
        //set actions,
        join_game.setOnAction(e -> showJoinGameScene());
        new_game.setOnAction(e -> showNewgameScene());
        this.mainStage.setScene(chooseGame);
    }

    private void showJoinGameScene() {
        String choose = "E";
        Text msg = new Text("Please specify the ID of a Game you want to join.");
        msg.setLayoutX(50);
        msg.setLayoutY(50);
        //
        TextField GID = new TextField();
        GID.setLayoutX(50);
        GID.setLayoutY(100);
        //
        Button confirm = new Button("Join Game");
        confirm.setLayoutX(50);
        confirm.setLayoutY(150);
        //
        Group g= new Group();
        g.getChildren().addAll(msg, GID, confirm);
        Scene joinGame = new Scene(g, 400, 300);
        //set actions,
        confirm.setOnAction(e -> {
            try {
                String errorMsg = App.cc.gameChoose(choose, Integer.parseInt(GID.getText()));
                if (errorMsg == null){
                    //redirect upon success
                    System.out.println("Successfully joined a game");
                    try {
                        //connectToServer();//initialize territories and receive payload
                        //already update local gameboard in gameChoose, no need reconnect
                        mainStage.setScene(new Scene(new Group(new TextField("Now waiting other players..."))));
                        App.cc.tryConnectAndWait();
                        App.initializeTerritories();//initialize territory UI cache

                        //after update(possibly no update since stage is beyond) and territories initialization
                        switch (App.cc.getStage()){

                            case Configurations.STAGE_CREATE:
                            case Configurations.STAGE_ASSIGN: {
                                placementPage = new placement();
                                placementPage.showWindow();
                                mainStage.close();
                                return;
                            }
                            case Configurations.STAGE_MOVE: {
                                actionChoosePage = new actionChoose();
                                actionChoosePage.showWindow();
                                mainStage.close();
                                return;
                            }
                            case Configurations.STAGE_OBSERVE :{
                                observer = new observerUI();
                                observer.showWindow();
                                mainStage.close();
                                return;
                            }
                            default:return;
                        }
                    } catch (Exception exception) {
                        showFailedScene("Exception occur");
                    }
                }
                else{
                    showFailedScene(errorMsg);
                }
            } catch (IOException ioException) {
                showFailedScene("IOException detected");
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
                showFailedScene("Exception detected");
            } catch (UnmatchedReceiverException unmatchedReceiverException) {
                unmatchedReceiverException.printStackTrace();
                showFailedScene("Exception detected");
            } catch (InvalidPayloadContent invalidPayloadContent) {
                invalidPayloadContent.printStackTrace();
                showFailedScene("Exception detected");
            }

        });
        this.mainStage.setScene(joinGame);
    }

    private void showNewgameScene() {
        String choose = "N";
        Text msg = new Text("Please specify the number of players in this Game.");
        msg.setLayoutX(50);
        msg.setLayoutY(50);
        //2 - 5 players
        ChoiceBox<Integer> num_choice_box = new ChoiceBox<>();
        for(int i = 2;i<=5;i++){
            num_choice_box.getItems().add(i);
        }
        num_choice_box.setLayoutX(50);
        num_choice_box.setLayoutY(70);
        //
        Button confirm = new Button("Create Game");
        confirm.setLayoutX(50);
        confirm.setLayoutY(150);
        //
        Group g= new Group();
        g.getChildren().addAll(msg, num_choice_box, confirm);
        Scene createGame = new Scene(g, 400, 300);
        //set actions,
        confirm.setOnAction(e -> {
            try {
                String errorMsg = App.cc.gameChoose(choose, num_choice_box.getValue());
                if (errorMsg == null){
                    //redirect upon success
                    //new game, so placement phase after(try connect and wait)
                    try {
                        mainStage.setScene(new Scene(new Group(new TextField("Now waiting other players..."))));
                        connectToServerAndEnterPlacement();//initialize territories and receive payload and enter placement phase
                        mainStage.close();
                    } catch (Exception exception) {
                        showFailedScene("Exception occur");
                    }
                }
                else{
                    showFailedScene(errorMsg);
                }
            } catch (IOException ioException) {
                showFailedScene("IOException detected");
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
                showFailedScene("Exception detected");
            } catch (UnmatchedReceiverException unmatchedReceiverException) {
                unmatchedReceiverException.printStackTrace();
                showFailedScene("Exception detected");
            } catch (InvalidPayloadContent invalidPayloadContent) {
                invalidPayloadContent.printStackTrace();
                showFailedScene("Exception detected");
            }

        });
        this.mainStage.setScene(createGame);
    }


    public void startGame() throws Exception {

        showUserPageScene();

        //placementPage.close();
    }



    public void setStage(Stage primaryStage) {
        this.mainStage = primaryStage;
    }
}
