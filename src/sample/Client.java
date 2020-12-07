package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
        //class to make peers for our p2p network

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

        //not sure if following two methods need to be used
    public void setSuperPeer(boolean superPeer){
        this.superPeer = superPeer;
    }

    public ArrayList<Block> getBlockchain(){
        return blockchain;
    }

            //method name seems little misleading
    public void connectToSuper() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connected!");

            // get the output stream from the socket
        OutputStream outputStream = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        System.out.println("Sending messages to the ServerSocket");
        objectOutputStream.writeObject("Connect to Super");

        System.out.println("Closing socket, another connection is now available");
        System.out.println("--------------------------------------------");
        socket.close();
    }

    public void sendBlock(ArrayList<Block> blockchain) throws IOException, ClassNotFoundException {
        //Client.blockchain = readStorage();
        Socket socket1 = new Socket(ip, port);
        System.out.println("Connected!");

            // get the output stream from the socket.
        OutputStream outputStream = socket1.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        System.out.println("Sending messages to the ServerSocket");
            //check blocks already have been added
        if (blockchain.isEmpty()){
            objectOutputStream.writeObject("No blockchain");
        } else {
                //if not empty send the last block of the blockchain
            objectOutputStream.writeObject(blockchain.get(blockchain.size() - 1));
            System.out.println(blockchain.get(blockchain.size()-1));
        }
            //update the old blockchain
        updateBlockchain(blockchain);
        }

        public void sendEntireBlockchain(/*Socket socket*/) throws IOException, ClassNotFoundException {
                //save the current blockchain in the arraylist
            blockchain = readStorage();
            Socket socket1 = new Socket(ip, port);
            //System.out.println("Connected!");
                // get the output stream from the socket.
            OutputStream outputStream = socket1.getOutputStream();
                // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("Sending messages to the ServerSocket");
                //send the whole blockchain
            objectOutputStream.writeObject(blockchain);
            System.out.println(blockchain);
            System.out.println("Closing socket, another connection is now available");
            System.out.println("--------------------------------------------");
            socket1.close();
                //why update here ?
            updateBlockchain(blockchain);
        }

        //method to update the blockchain
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
        //method to read the "storage" file where the blockchain is stored
        //returns an arraylist containing the blockchain
    public static ArrayList<Block> readStorage() throws IOException, ClassNotFoundException {
            //Accesses the storage file, which is where the arraylist of the blockchain is.
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storage.txt"));
            //Reads the storage file and lets the ArrayList blockchain be equal to this
        return (ArrayList<Block>) ois.readObject();
    }


    public Socket getSocket(){
        return this.client;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
