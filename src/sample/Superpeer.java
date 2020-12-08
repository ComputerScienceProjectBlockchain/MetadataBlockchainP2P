package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

        //class to simulate a super peer in a P2P network

public class Superpeer {
        //private final static String ip = "localhost";
    private final  int port = 7777;
        //since a super peer shares some methods with the usual peers
        //we create a super peer as a client and add additional methods in this class
//    private static Client superPeer;
        //arraylist to save ip addresses of connected peers
    private  ArrayList<String> peers = new ArrayList<String>();
        //arraylist to save the blockchain
    private  ArrayList<Block> blockchain = new ArrayList<Block>();


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //superPeer = new Client(ip, port);
        //superPeer.setSuperPeer(true);
        Superpeer superpeer = new Superpeer();
        while (true) // forever
        {
            //listen for peer
            Socket socket = superpeer.connectPeer();
            PeerConnection peerConnection = new PeerConnection(socket, superpeer);
            new Thread(peerConnection).start(); // new thread begins by executing run method

        }
    }

    private Socket connectPeer() throws IOException {
        ServerSocket ss = new ServerSocket(port);
        System.out.println("ServerSocket awaiting connections...");
        // blocking call, this will wait until a connection is attempted on this port
        Socket socket = ss.accept();
        ss.close();
        return socket;
    }

    //adds the ip of a peer to the arraylist
    public void add(Socket socket) {
        peers.add(socket.getInetAddress().toString().substring(1)); // kan blive mere elegant
    }


        //does the same as the readIP() method just for the blockchain
        //returns an arraylist of blocks
    public  ArrayList<Block> readStorage() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storage.txt"));
        return (ArrayList<Block>) ois.readObject();
    }



    public ArrayList<Block> getBlocks() throws IOException, ClassNotFoundException {
        blockchain = readStorage();
        return blockchain;
    }
}