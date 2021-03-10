package edu.duke.risc.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author eason
 * @date 2021/3/10 10:56
 */
public class SocketCommunicator implements Communicable {

    private Socket socket;

    private ObjectOutputStream writer;

    private ObjectInputStream reader;

    public SocketCommunicator(Socket socket) {
        this.socket = socket;
        try {
            writer = new ObjectOutputStream(this.socket.getOutputStream());
            reader = new ObjectInputStream(this.socket.getInputStream());
            System.out.println("Successfully connect to socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean writeMessage(PayloadObject message) throws IOException {
        writer.writeObject(message);
        return true;
    }

    @Override
    public PayloadObject receiveMessage() throws IOException, ClassNotFoundException {
        return (PayloadObject) reader.readObject();

    }

}
