package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

    //This class is basically the connection between the super peer and all incoming peers
    //for each incoming peer we will have a new thread
public class PeerConnection implements Runnable {

    private Socket socket;
    private final Superpeer superpeer;
    private OutputStream outputStream;
    private static int difficulty = 2;
    private ArrayList<Block> blockchain = new ArrayList<>();

    //exceptions passed on from establishStreams
    @Override
    public void run() {
        establishStreams();
    }

    //new constructor which takes the socket of the peer
    //and the super peer as input
    public PeerConnection(Socket socket, Superpeer superpeer) {
        this.socket = socket;
        this.superpeer = superpeer;
    }

    //Exceptions are passed on from receiveBlocks
    private void establishStreams() {
        //add the socket IP to the super peers arraylist
        superpeer.addIP(this.socket);
        //get the latest version of the blockchain
        this.blockchain = superpeer.readStorage();
        //method to send blocks to the peer
        //how many blocks the method sends is based on what the input is
        //further explanation in the method
        sendToPeer(receiveBlocks());
    }

    //method to prepare an ObjectOutputStream
    //exception needs to be passed on, because we return an ObjectOutputStream
    private ObjectOutputStream prepareObjectOutputStream() throws IOException {
        this.socket = new Socket(socket.getInetAddress().getHostAddress(), 7778);
        outputStream = this.socket.getOutputStream();
        return new ObjectOutputStream(outputStream);
    }

    //method to check if a peer has the same blockchain as the super peer
    //if the received integer is smaller than the blockchain length
    //the superpeer will send the missing blocks to the peer
    private void sendMissingBlocks(int index) throws IOException{
        //first we need to check if the blockchain is empty
        if (blockchain.isEmpty()) {
            ObjectOutputStream objectOutputStream = prepareObjectOutputStream();
            System.out.println("Blockchain is empty");
            //if it is empty, we send a message to the server of the peer
            objectOutputStream.writeObject("Is Empty");
        } else {
            //if the blockchain is not empty, we compare the size of the blockchain of the peer
            //with the size of the blockchain of the superpeer
            //if the peer is missing blocks, they will be send to the peer
            for (int i = index; i < blockchain.size(); i++) {
                ObjectOutputStream objectOutputStream = prepareObjectOutputStream();
                System.out.println("blockchain length:" + blockchain.size());
                objectOutputStream.writeObject(blockchain.get(i));
                System.out.println(blockchain.get(i));
            }
            //after sending all blocks we send a message to the server of the peer
            ObjectOutputStream objectOutputStream = prepareObjectOutputStream();
            objectOutputStream.writeObject("Entire blockchain sent");
            System.out.println("Entire blockchain sent");
            //the last we do is closing the socket
            this.socket.close();
        }
    }

    //method to send a new block to a peer
    private void sendNewBlock(Block block) throws IOException{
        //first we need to check if the block the super peer received
        //has the correct previous hash. The previous hash must be equal to the hash
        //of the previous block or the blockchain is empty -
        // if it is empty then there is no previous hash to compare with
        if (blockchain.isEmpty()||block.getPreviousHash().equals(blockchain.get(blockchain.size() - 1).getHash())){
            //block gets add to the blockchain
            addBlockToBlockchain(block);
            //for(String peer : superpeer.getPeerIP()) {
            System.out.println("Sending blocks to Peer: " + socket.getInetAddress().getHostAddress());
            //initializing a new socket, with the peers IP and the port the peer is listening on
            Socket peerSocket = new Socket(socket.getInetAddress().getHostAddress(), 7778);
            outputStream = peerSocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            //send the block to the peer
            objectOutputStream.writeObject(block);
            System.out.println("Sending back block");
            //close the socket
            peerSocket.close();
        }
    }

        //this method uses either sendMissingBlocks or sendNewBlock based of what instance the object is
    private void sendToPeer(Object o) {
        try {
            if (o instanceof Integer) {
                //if the object is an Integer, then we know that the input is the length of a peers blockchain
                //therefore we need to use sendMissingBlocks to update a peers blockchain
                Integer index = (Integer) o;
                sendMissingBlocks(index);
            } else if (o instanceof Block) {
                //if the input is a Block, then we know that a peer would like to add a new block to the blockchain
                //therefore we need to use sendNewBlock, where it will be checked if the block can be added
                Block block = (Block) o;
                sendNewBlock(block);
            } else {
                System.out.println("Invalid blockchain");
            }
        }catch(IOException i){
            i.printStackTrace();
            System.out.println("No socket available");
        }
    }

        //method to receive length of a peers blockchain, so the number of blocks a peer has
        //with that the super peer can check if the peer missed to receive some blocks
    private Object receiveBlocks() {
        //we need to initialize the object outside try-catch statement
        //therefore we had to add a do while loop, so that we first return the object when it is not "null" anymore
        Object o = "null";
        do {
            try {
                System.out.println("Receiving blocks");
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                o = objectInputStream.readObject();
                //we can catch two different exceptions
                //we chose to catch them separately so that we can print different messages for each
            } catch (IOException i) {
               i.printStackTrace();
                System.out.println("No input stream available. \n Trying again...");
            } catch(ClassNotFoundException e){
                e.printStackTrace();
                System.out.println("Not able to read the object from the input stream. \n Trying again...");
            }
        }while(o.equals("null"));
        return o;
    }

        //A method which adds blocks to the blockchain
    private void addBlockToBlockchain(Block newBlock) {
            //first need to mine the block and put some effort in
        newBlock.mineBlock(difficulty);
            //add the block to the blockchain
        blockchain.add(newBlock);
        newBlock.setHeight(newBlock.getHeight() + 1);
        System.out.println("Block version number: " + newBlock.getHeight());
        //the txt-file where the blockchain is saved
        updateBlockchain(blockchain);
    }

        //method to update the txt-file where the blockchain is saved
    private void updateBlockchain(ArrayList<Block> blockchain){
        try {
            FileOutputStream out = new FileOutputStream("storage.txt");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(blockchain);
            oos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
