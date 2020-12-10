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


    public Peer(String superPeerIP) throws IOException, ClassNotFoundException {
        this.superPeerIP = superPeerIP;
        this.socket = connectToSuper();
    }
            //method to connect the peer to the super peer
    public Socket connectToSuper() throws IOException, ClassNotFoundException {
            //read the storage txt file and save the blockchain it contains
        blockchain = readStorage();
            //make a new socket with the ip of the super peer and a port
        Socket socket = new Socket(this.superPeerIP, port);
        System.out.println("Connected!");

            // get the output stream from the socket
        this.outputStream = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
        this.objectOutputStream = new ObjectOutputStream(this.outputStream);

        System.out.println("Sending messages to the ServerSocket");
            //send the size of the blockchain to the super peer
            //then the super peer can see which blocks the peer is missing
            //and then the super peer can send the missing blocks back
        this.objectOutputStream.writeObject(blockchain.size());
        System.out.println(blockchain.size());
        return socket;
    }

            //connects to the super peer and sends a block the peer wants to add to the blockchain
            //does the same as the other connectToSuper method
            //the only thing that differs is the this method send a block instead of the size of the blockchain
    public void connectToSuper(Block block) throws IOException, ClassNotFoundException {
        blockchain = readStorage();
        Socket socket = new Socket(superPeerIP, port);
        System.out.println("Connected!");

            // get the output stream from the socket.
        this.outputStream = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
        this.objectOutputStream = new ObjectOutputStream(this.outputStream);
        System.out.println("Sending messages to the ServerSocket");
            //here we send a block to the super peer
        this.objectOutputStream.writeObject(block);
        System.out.println(block);
    }

            //returns a block the either is the genesis block
            //or another block that is supposed to be added to the blockchain
    public Block prepareBlock(String userName, String path) throws IOException, ClassNotFoundException {
            //When the blockchain is empty, the previous hash is 0; "initialization" of genesis block
        Block block;
        if (blockchain.isEmpty()) {
                //generation of a genesis block
                //genesis block is the first the block in a blockchain; it has no previous hash to refer to
            block = (new Block(new Metadata(path), "0", userName));
        } else {
            block = (new Block(new Metadata(path), blockchain.get(blockchain.size() - 1).getHash(), userName));
        }
        return block;
    }

        //sends a prepared block to the super peer
    public void sendBlock(Block preparedBlock) throws IOException, ClassNotFoundException {
        connectToSuper(preparedBlock);
        Server server = new Server(blockchain);
        server.serverConnection();
    }
        //while loop will run until the server receives the message  "done"
        //this message came from the super peer and means that the peer now has the latest version of the blockchain
    public void connectToServer() throws IOException, ClassNotFoundException {
        while(true) {
            Server server = new Server(blockchain);
            server.serverConnection();
            Object o = server.receiveInput();
                //if method to get out of the while loop
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

        //method to view the blockchain in the terminal
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
