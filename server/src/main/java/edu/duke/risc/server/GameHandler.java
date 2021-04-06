package edu.duke.risc.server;

import java.io.IOException;

public class GameHandler extends Thread{
    private GameController game;

    public GameHandler(GameController game){
        this.game = game;
    }

    @Override
    public void run(){
        game.run();
    }

}
