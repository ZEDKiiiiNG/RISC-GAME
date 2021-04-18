package edu.duke.risc.shared;


import java.io.*;
import java.net.Socket;

/**
 * Used for socket communication
 *
 * @author eason
 * @date 2021/3/10 10:56
 */
public class SocketCommunicator implements Communicable, Serializable {

    private Socket socket;

    private transient OutputStream writer;

    public Socket getSocket() {
        return socket;
    }

    private transient InputStream reader;

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
    @Override
    public void sendUrgentData() throws  IOException {
        this.socket.sendUrgentData(0xFF);
    }

}
