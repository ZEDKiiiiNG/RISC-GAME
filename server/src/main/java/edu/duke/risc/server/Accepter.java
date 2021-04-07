package edu.duke.risc.server;

import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.SocketCommunicator;
import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.PayloadType;
import edu.duke.risc.shared.users.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static edu.duke.risc.shared.Configurations.*;

public class Accepter extends Thread{
    /**
     * Socket connection with the server
     */
    private ServerSocket serverSocket;

    /**
     * id with corresponding password
     */
    private Map<String, String> idPwdMap;
    /**
     * Games with corresponding No.
     */
    private Map<Integer, GameController> Games;




    public Accepter(){
        try {
            serverSocket = new ServerSocket(Configurations.DEFAULT_SERVER_PORT);
            idPwdMap = new HashMap<>();
            Games = new HashMap<>();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String validateLogin(SocketCommunicator communicator) throws IOException, ClassNotFoundException {
        while(true) {
            PayloadObject readObject = null;
            readObject = communicator.receiveMessage();
            String id = (String) readObject.getContents().get("ID");
            String pwd = (String) readObject.getContents().get("PWD");
            Map<String, Object> content = new HashMap<>(10);
            // first time to log in
            if(readObject.getMessageType() == PayloadType.LOGIN){
                if (idPwdMap.containsKey(id) && idPwdMap.get(id).equals(pwd)) {
                    content.put(SUCCESS_LOG, null);
                    PayloadObject payloadObject = new PayloadObject(MASTER_ID, DEFAULT_PLAYER_ID, PayloadType.LOGIN, content);
                    communicator.writeMessage(payloadObject);
                    return id;
                }
                else{
                    content.put(CANNOTFIND_LOG, null);
                    PayloadObject payloadObject = new PayloadObject(MASTER_ID, DEFAULT_PLAYER_ID, PayloadType.LOGIN, content);
                    communicator.writeMessage(payloadObject);
                }
            }
            else if (readObject.getMessageType() == PayloadType.SIGNUP ) {
                if (idPwdMap.containsKey(id)) {
                    content.put(OCCUPIED_LOG, null);
                    PayloadObject payloadObject = new PayloadObject(MASTER_ID, DEFAULT_PLAYER_ID, PayloadType.SIGNUP, content);
                    communicator.writeMessage(payloadObject);
                }
                else{
                    idPwdMap.put(id,pwd);
                    content.put(SUCCESS_LOG, null);
                    PayloadObject payloadObject = new PayloadObject(MASTER_ID, DEFAULT_PLAYER_ID, PayloadType.SIGNUP, content);
                    communicator.writeMessage(payloadObject);
                    return id;
                }
            }
        }
    }

    private void gameOptions(SocketCommunicator communicator, String id) throws IOException, ClassNotFoundException, InterruptedException {
        while(true) {
            PayloadObject readObject = null;
            readObject = communicator.receiveMessage();
            String choose = (String) readObject.getContents().get("CHOOSE");
            int gameid = (int) readObject.getContents().get("ID");
            Map<String, Object> content = new HashMap<>(10);
            // join exist game
            if(choose.equals("E")){
                //cannot find game
                if(!Games.containsKey(gameid)){
                    content.put(GAMENOTFOUND, null);
                    PayloadObject payloadObject = new PayloadObject(MASTER_ID, DEFAULT_PLAYER_ID, PayloadType.LOGIN, content);
                    communicator.writeMessage(payloadObject);
                    continue;
                }

                //find game in start stage
                if(Games.get(gameid).getStage().equals(STAGE_CREATE) ){
                    content.put(SUCCESSFOUND, null);
                    content.put("STAGE",STAGE_CREATE);
                    content.put("GAMEID",gameid);
                    PayloadObject payloadObject = new PayloadObject(MASTER_ID, DEFAULT_PLAYER_ID, PayloadType.LOGIN, content);
                    Games.get(gameid).addUserConnections(id, communicator);
                    communicator.writeMessage(payloadObject);
                    gameStarter();
                    return;
                }
                if(Games.get(gameid).getIdMap().containsKey(id)) {
                    //find game in other stage
                    content.put(SUCCESSFOUND, null);
                    content.put("STAGE", Games.get(gameid).getStage());
                    PayloadObject payloadObject = new PayloadObject(MASTER_ID, Games.get(gameid).getIdMap().get(id).getId(), PayloadType.LOGIN, content);
                    Games.get(gameid).addUserConnections(id, communicator);
                    Games.get(gameid).updateConnections(id);

                    content.put(GAME_BOARD_STRING, Games.get(gameid).getBoard());
                    content.put(LOGGER_STRING, "");
                    content.put(PLAYER_STRING, Games.get(gameid).getIdMap().get(id).getId());

                    communicator.writeMessage(payloadObject);
                    return;
                }
                else{
                    content.put(USERNOTFOUND, null);
                    PayloadObject payloadObject = new PayloadObject(MASTER_ID, DEFAULT_PLAYER_ID, PayloadType.LOGIN, content);
                    communicator.writeMessage(payloadObject);
                }
            }
            // choose to create new game
            else{
                int gameNo = Games.size()+1;
                content.put(SUCCESSFOUND, null);
                content.put("STAGE",STAGE_CREATE);
                content.put("GAMEID",gameNo);
                GameController newGame = new GameController(gameNo, gameid);
                Games.put(gameNo, newGame);
                PayloadObject payloadObject = new PayloadObject(MASTER_ID, DEFAULT_PLAYER_ID, PayloadType.LOGIN, content);
                Games.get(gameNo).addUserConnections(id, communicator);
                Games.get(gameNo).addUserConnections(id, communicator);
                communicator.writeMessage(payloadObject);
                gameStarter();
                return;
            }
        }
    }

    /**
     * check all games ended or not
     */
    public boolean checkFinish(){
        if(Games.size() == 0){
            return false;
        }
        for(Map.Entry<Integer, GameController> entry : Games.entrySet()){
            if(!entry.getValue().getStage().equals(GAMEFINISHED))
                return  false;
        }
        return  true;
    }

    /**
     * check all games ended or not
     */
    public void gameStarter(){
        if(Games.size() == 0){
            return;
        }
        for(Map.Entry<Integer, GameController> entry : Games.entrySet()){
            if(entry.getValue().getUserConnections().size() == entry.getValue().getMaxPlayer()) {
                GameHandler gameHandler = new GameHandler(entry.getValue());
                gameHandler.start();
                System.out.println("game"+ entry.getKey()+"start");
            }
            else{
                System.out.println("game"+ entry.getKey()+"waiting for"
                        +(entry.getValue().getMaxPlayer()- entry.getValue().getUserConnections().size())+"to join the game");
            }
        }
    }



    @Override
    public void run() {
        try {
            while (true) {
                if(checkFinish()){
                    this.serverSocket.close();
                    System.out.println("All Games are over");
                    System.exit(0);
                }
                System.out.println("Server starts");
                Socket clientSocket = serverSocket.accept();
                SocketCommunicator communicator = new SocketCommunicator(clientSocket);
                String id = validateLogin(communicator);
                gameOptions(communicator,id);

            }
        }
        catch (IOException | ClassNotFoundException | InterruptedException e) {
            //when socket disconnects
            System.out.println(e.getMessage());
        }
    }
}
