package sample;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    private static ArrayList<Block> blockchain = new ArrayList<>();


    private String ip;
    private int port;
    private Socket client;
    private boolean superPeer;

    public Client(String ip, int port) throws IOException{
        this.ip = ip;
        this.port = port;
        //client = new Socket(ip,port);
        superPeer = false;
    }

    public void setSuperPeer(boolean superPeer){
        this.superPeer = superPeer;
    }

    public ArrayList<Block> getBlockchain(){
        return blockchain;
    }

    public void connectToSuper() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connected!");

        // get the output stream from the socket.
        OutputStream outputStream = this.client.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        System.out.println("Sending messages to the ServerSocket");
        objectOutputStream.writeObject("Connect to Super");

        System.out.println("Closing socket, another connection is now available");
        System.out.println("--------------------------------------------");
        socket.close();
    }

    public void sendBlock() throws IOException {
            Socket socket = new Socket(ip, port);
            System.out.println("Connected!");

            // get the output stream from the socket.
            OutputStream outputStream = this.client.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("Sending messages to the ServerSocket");
            objectOutputStream.writeObject(blockchain.get(blockchain.size()-1));

            System.out.println("Closing socket, another connection is now available");
            System.out.println("--------------------------------------------");
            socket.close();
            updateBlockchain(blockchain);
        }

        public void sendEntireBlockchain() throws IOException {
            Socket socket = new Socket(ip, port);
            System.out.println("Connected!");

            // get the output stream from the socket.
            OutputStream outputStream = this.client.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("Sending messages to the ServerSocket");
            objectOutputStream.writeObject(blockchain);

            System.out.println("Closing socket, another connection is now available");
            System.out.println("--------------------------------------------");
            socket.close();
            updateBlockchain(blockchain);
        }

    public void updateBlockchain(ArrayList<Block> blockchain){
        try {
            FileOutputStream out = new FileOutputStream("storage.txt");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(blockchain);
            oos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Block> readStorage() throws IOException, ClassNotFoundException {
        //Accesses the storage file, which is where the arraylist of the blockchain is.
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storage.txt"));

        //Reads the storage file and lets the ArrayList blockchain be equal to this
        return (ArrayList<Block>) ois.readObject();
    }


    public Socket getSocket(){
        return this.client;
    }
}
