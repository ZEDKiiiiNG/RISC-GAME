/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.risc.client;

import java.io.IOException;

/**
 *
 */
public class App {
    public String getGreeting() {
        return "Hello world from client.";
    }

    public static void main(String[] args) throws IOException {
        ClientController clientController = new ClientController();
    }
}
