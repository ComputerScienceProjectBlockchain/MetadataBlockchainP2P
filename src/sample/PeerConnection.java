package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

    //This class is basically the connection between the super peer and all incoming peers
    //for each incoming peer we will have a new thread
public class PeerConnection implements Runnable {

    Socket socket;
    private Superpeer superpeer;
    InputStream inputStream;
    OutputStream outputStream;
    public static int difficulty = 2;
    ArrayList<Block> blockchain = new ArrayList<>();

    @Override
        //exceptions passed on from establishStreams
    public void run() {
        try {
            establishStreams();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

        //new constructor which takes the socket of the peer
        //and the super peer as input
    public PeerConnection(Socket socket, Superpeer superpeer) {
        this.socket = socket;
        this.superpeer = superpeer;
    }

        //Exceptions are passed on from receiveBlocks
    public void establishStreams() throws IOException, ClassNotFoundException {
            //add the socket IP to the super peers arraylist
        superpeer.addIP(this.socket);
            //get the latest version of the blockchain
        this.blockchain = superpeer.readStorage();
            //method to send blocks to the peer
            //how many blocks the method sends is based on what the input is
            //further explanation in the method
        sendBlocks(receiveBlocks());
    }
        //method to prepare an ObjectOutputStream
        //exception needs to be passed on, because we return an ObjectOutputStream
    public ObjectOutputStream prepareObjectOutputStream() throws IOException {
        this.socket = new Socket(socket.getInetAddress().getHostAddress(), 7778);
        outputStream = this.socket.getOutputStream();
        return new ObjectOutputStream(outputStream);
    }

        //we should probably split this method in several methods
    public void sendBlocks(Object o) {
        try {
            if (o instanceof Integer) {
                Integer index = (Integer) o;
                if (blockchain.isEmpty()) {
                    ObjectOutputStream objectOutputStream = prepareObjectOutputStream();
                    System.out.println("Blockchain is empty");
                    objectOutputStream.writeObject("Is Empty");
                } else {
                    for (int i = index; i < blockchain.size(); i++) {
                        ObjectOutputStream objectOutputStream = prepareObjectOutputStream();
                        System.out.println("blockchain length:" + blockchain.size());
                        objectOutputStream.writeObject(blockchain.get(i));
                        System.out.println(blockchain.get(i));
                    }
                    ObjectOutputStream objectOutputStream = prepareObjectOutputStream();
                    objectOutputStream.writeObject("Entire blockchain sent");
                    System.out.println("Entire blockchain sent");
                    this.socket.close();
                }
            } else if (o instanceof Block) {
                Block block = (Block) o;
                if (block.getPreviousHash().equals(blockchain.get(blockchain.size() - 1).getHash())) {
                    addBlockToBlockchain(block);
                    //for(String peer : superpeer.getPeerIP()) {
                    System.out.println("Sending blocks to Peer: " + socket.getInetAddress().getHostAddress());
                    Socket peerSocket = new Socket(socket.getInetAddress().getHostAddress(), 7778);
                    System.out.println("Bob");
                    outputStream = peerSocket.getOutputStream();
                    System.out.println("Unicorn");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    System.out.println("Bla");
                    objectOutputStream.writeObject(block);
                    System.out.println("Sending back block");
                    peerSocket.close();
                }
            } else {
                System.out.println("Invalid blockchain");
            }
        }catch(IOException i){
            i.printStackTrace();
        }
    }

        //method to receive the number of blocks a peer has
        //with that the super peer can check if the peer missed to receive some blocks
        //exception need to be passed on because of what we we return
    public Object receiveBlocks() throws IOException, ClassNotFoundException {
        System.out.println("Receiving blocks");
        inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return objectInputStream.readObject();
    }

        //A method which adds blocks to the blockchain
    public void addBlockToBlockchain(Block newBlock) {
            //first need to mine the block and put some effort in
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
        newBlock.setHeight(newBlock.getHeight() + 1);
        System.out.println("Block version number: " + newBlock.getHeight());
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
}
