package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

        //class to simulate a super peer in a P2P network

public class Superpeer {
    //port for the server of the superpeer
    private final int port = 7777;

    //arraylist to save ip addresses of connected peers
    private ArrayList<String> peerIP = new ArrayList<String>();
    //arraylist to save the blockchain
    private ArrayList<Block> blockchain = new ArrayList<Block>();


    public static void main(String[] args) throws IOException {
        //new superpeer object
        Superpeer superpeer = new Superpeer();
        //server of the super peer has to keep listening for incoming peer connections
        //therefore this while loop is necessary
        while (true) {
            //this method listens for incoming peers
            Socket socket = superpeer.connectPeer();
            //if there is a new connection we create a new PeerConnection object
            //this uses the superpeer and the socket for the connected peer as input
            PeerConnection peerConnection = new PeerConnection(socket, superpeer);
            //then a new thread for each peer that is incoming is started
            //this gives the possibility to connect to several peers
            new Thread(peerConnection).start();
        }
    }

    //method where the server socket listens for incoming connections
    private Socket connectPeer() {
        Socket socket = null;
        //do while loop, so that the try block will be executed until we have a socket we can return
        do {
            try {
                ServerSocket ss = new ServerSocket(port);
                System.out.println("ServerSocket awaiting connections...");
                // blocking call, this will wait until a connection is attempted on this port
                socket = ss.accept();
                //after a succesful connection, the server socket will be closed
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Not able to initialize a server socket. \n Trying again...");
            }
        }
        while(socket == null);
        //return the socket the server socket has connected to
        return socket;
    }

    //takes a socket as input and adds the sockets IP in the arraylist
    public void addIP(Socket socket) {
        if (!peerIP.contains(socket.getInetAddress().getHostAddress())) {
            //getInetAddress returns an InetAddress object
            //getHostAddress will then return the IP as a string
            peerIP.add(socket.getInetAddress().getHostAddress());
        } else {
            System.out.println("Peer already in arrayList");
        }
    }


    //this methods reads the storage file where the blockchain is saved
    //and returns an arraylist of blocks - the blockchain
    public ArrayList<Block> readStorage() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storage.txt"));
            blockchain = (ArrayList<Block>) ois.readObject();
        } catch (FileNotFoundException f) {
            f.printStackTrace();
            System.out.println("There is no such file available.");
        } catch (IOException | ClassNotFoundException i){
            i.printStackTrace();
            System.out.println("No such Output stream available, therefore there is no object to read.");
        }
        return blockchain;
    }
}   