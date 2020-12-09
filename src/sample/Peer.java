package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Peer{
    String superPeerIP;
    int port = 7777;
    public static int difficulty = 2;
    ArrayList<Block> blockchain = new ArrayList<Block>();
    Socket socket;
    OutputStream outputStream;
    ObjectOutputStream objectOutputStream;
    InputStream inputStream;
    ObjectInputStream objectInputStream;

    public Peer(String superPeerIP) throws IOException, ClassNotFoundException {
        this.superPeerIP = superPeerIP;
        this.socket = connectToSuper();
    }

    public Socket connectToSuper() throws IOException, ClassNotFoundException {
        blockchain = readStorage();
        Socket socket = new Socket(superPeerIP, port);
        System.out.println("Connected!");

        // get the output stream from the socket.
        this.outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        this.objectOutputStream = new ObjectOutputStream(this.outputStream);

        System.out.println("Sending messages to the ServerSocket");
        this.objectOutputStream.writeObject(blockchain.size());
        System.out.println(blockchain.size());
        return socket;
    }

        //find better name
    public void connectToSuper(Block block) throws IOException, ClassNotFoundException {
        blockchain = readStorage();
        Socket socket = new Socket(superPeerIP, port);
        System.out.println("Connected!");

        // get the output stream from the socket.
        this.outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        this.objectOutputStream = new ObjectOutputStream(this.outputStream);

        System.out.println("Sending messages to the ServerSocket");
        this.objectOutputStream.writeObject(block);
        System.out.println(block);
    }


    public void prepareBlock(String userName, String path) throws IOException, ClassNotFoundException {
        //When the blockchain is empty, the previous hash is 0; "initialization" of genesis block
        Block block;
        if (blockchain.isEmpty()) {
            block = (new Block(new Metadata(path), "0", userName));
        } else {
            block = (new Block(new Metadata(path), blockchain.get(blockchain.size() - 1).getHash(), userName));
        }
        sendBlock(block);
    }

    public void sendBlock(Block preparedBlock) throws IOException, ClassNotFoundException {
        connectToSuper(preparedBlock);
        Server server = new Server(blockchain);
        server.serverConnection();
    }

    public void connectToServer() throws IOException, ClassNotFoundException {
        while(true) {
            Server server = new Server(blockchain);
            server.serverConnection();
            Object o = server.receiveInput();
            if (o.equals("done")){
                System.out.println("Entire blockchain received");
                break;
            }
        }
    }

    //method to read the "storage" file where the blockchain is stored
    //returns an arraylist containing the blockchain
    public ArrayList<Block> readStorage() throws IOException, ClassNotFoundException {
        //Accesses the storage file, which is where the arraylist of the blockchain is.
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storage.txt"));
        //Reads the storage file and lets the ArrayList blockchain be equal to this
        return (ArrayList<Block>) ois.readObject();
    }


    //A method which adds blocks to the blockchain
    public void addBlockToBlockchain(Block newBlock) {
        //first need to mine the block and put some effort in
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
        newBlock.setHeight(newBlock.getHeight()+1);
        System.out.println("Block version number: "+newBlock.getHeight());
    }

    public void viewBlockchain() {
        if (blockchain.isEmpty()) {
            System.out.println("No blockchain to view");
        } else {
            String blockchainJson = StringUtil.getJson(blockchain);
            System.out.println("\nThe block chain: ");
            System.out.println(blockchainJson);
        }
    }


}

/*
//check blocks already have been added
        if (blockchain.isEmpty()){
            objectOutputStream.writeObject("No blockchain");
        } else {
            //if not empty send the last block of the blockchain
            objectOutputStream.writeObject(blockchain.get(blockchain.size() - 1));
            System.out.println(blockchain.get(blockchain.size()-1));
        }
 */
