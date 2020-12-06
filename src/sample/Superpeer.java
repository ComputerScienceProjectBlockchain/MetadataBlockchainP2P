package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

        //class to simulate a super peer in a P2P network

public class Superpeer {
        //private final static String ip = "localhost";
    private final static int port = 7777;
        //since a super peer shares some methods with the usual peers
        //we create a super peer as a client and add additional methods in this class
    private static Client superPeer;
        //arraylist to save ip addresses of connected peers
    private static ArrayList<String> ipAddresses = new ArrayList<String>();
        //arraylist to save the blockchain
    private static ArrayList<Block> blockchain = new ArrayList<Block>();


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //superPeer = new Client(ip, port);
        //superPeer.setSuperPeer(true);
        blockchain = readStorage();
        superServer();
        updateIP();
        if (readIP().isEmpty()) {
            System.out.println("No peers connected.");
        }
    }

        //adds the ip of a peer to the arraylist
    public static void add(Socket socket) {
        ipAddresses.add(socket.getInetAddress().toString());
    }

        //the arraylist of ip's is saved in a txt file and this method update the file
    public static void updateIP() {
        try {
            FileOutputStream out = new FileOutputStream("ip.txt");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(ipAddresses);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

            //reads the storage file of ips and returns the content as an arraylist
    public static ArrayList<String> readIP() throws IOException, ClassNotFoundException {
            //Accesses the storage file, which is where the arraylist of the ip addresses is
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ip.txt"));
            //Reads the storage file and lets the ArrayList ipAddresses be equal to this
        return (ArrayList<String>) ois.readObject();
    }

        //does the same as the readIP() method just for the blockchain
        //returns an arraylist of blocks
    public static ArrayList<Block> readStorage() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storage.txt"));
        return (ArrayList<Block>) ois.readObject();
    }

            //opening a server for the super peer
    public static void superServer() throws IOException, ClassNotFoundException {
        ServerSocket ss = new ServerSocket(port);
        System.out.println("ServerSocket awaiting connections...");
            // blocking call, this will wait until a connection is attempted on this port
        Socket socket = ss.accept();
        System.out.println("Connection from " + socket + "!");
            // get the input stream from the connected socket
        InputStream inputStream = socket.getInputStream();
            // create a DataInputStream so we can read data from it
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            //returns the object which has been send to the serverSocket
        Object o = objectInputStream.readObject();
            //println for testing purposes
        System.out.println(o);
            //String input = (String) o;
                //check what type of object has been sent
            if (o.equals("Connect to Super")) {
                readStorage();
                Client client = new Client(socket.getInetAddress().toString(), port);
                if (!ipAddresses.contains(socket.getInetAddress().toString())) {
                    add(socket);
                }
                client.sendEntireBlockchain(socket);
            } else {
                    //if a new block has been sent, we save it in a variable
                Block newBlock = (Block) o;
                    //check if the blockchain already contains a file with the same name as in the new block
                Server.compareBlocks(blockchain,newBlock);
                    //add the new block to the blockchain
                blockchain.add(newBlock);
                    //for loop that sends the new updated blockchain to all other peers
                for (int i = 0; i < ipAddresses.size(); i++) {
                    Client client = new Client(ipAddresses.get(i), port);
                    System.out.println("Sending block to " + ipAddresses.get(i));
                    client.sendBlock(socket, blockchain);
                }
            }
            System.out.println("Closing sockets.");
            System.out.println("----------------------");
            ss.close();
            socket.close();
                //repetitive calling so that the super peer always listens to new connecting peers
            superServer();
        }
    }