/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.risc.server;

import java.io.IOException;

/**
 * @author Yichen
 */
public class App {

    public String getGreeting() {
        return "Hello world from server.";
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Accepter accepter = new Accepter();
        accepter.start();
    }

}
