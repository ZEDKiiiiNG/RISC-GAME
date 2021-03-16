package edu.duke.risc.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Used for socket communication
 *
 * @author eason
 * @date 2021/3/10 10:56
 */
public class SocketCommunicator implements Communicable {

    private Socket socket;

    private OutputStream writer;

    private InputStream reader;

    public SocketCommunicator(Socket socket) {
        this.socket = socket;
        try {
            writer = this.socket.getOutputStream();
            reader = this.socket.getInputStream();
            System.out.println("Successfully connect to socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean writeMessage(PayloadObject message) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.writer);
        objectOutputStream.writeObject(message);
        return true;
    }

    @Override
    public PayloadObject receiveMessage() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(this.reader);
        return (PayloadObject) objectInputStream.readObject();
    }

    @Override
    public void terminate() {
        try {
            this.writer.close();
            this.reader.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
