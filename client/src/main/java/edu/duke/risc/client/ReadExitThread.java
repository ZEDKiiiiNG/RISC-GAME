package edu.duke.risc.client;

import edu.duke.risc.shared.Communicable;
import edu.duke.risc.shared.Configurations;
import edu.duke.risc.shared.PayloadObject;
import edu.duke.risc.shared.commons.PayloadType;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author eason
 * @date 2021/3/14 22:49
 */
public class ReadExitThread extends Thread {

    public static final String EXIT = "exit";

    private BufferedReader consoleReader;
    private Communicable communicator;
    private Integer playerId;

    public ReadExitThread(BufferedReader consoleReader, Communicable communicator, Integer playerId) {
        this.consoleReader = consoleReader;
        this.communicator = communicator;
        this.playerId = playerId;
    }

    @Override
    public void run() {
        try {
            while(true){
                String input = consoleReader.readLine();
                input = input.toLowerCase();
                if (EXIT.equals(input)) {
                    PayloadObject request = new PayloadObject(playerId, Configurations.MASTER_ID, PayloadType.QUIT);
                    this.communicator.writeMessage(request);
                    break;
                }else{
                    System.out.println("Invalid input, should be exit");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
