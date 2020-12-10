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

    public void establishStreams() throws IOException, ClassNotFoundException{
            //add the socket IP to the super peers arraylist
        superpeer.addIP(this.socket);
            //get the latest version of the blockchain
        this.blockchain = superpeer.readStorage();
            //method to send blocks to the peer
            //how many blocks the method sends is based on what the input is
            //further explanation in the method
        sendBlocks(receiveBlocks());
    }

        //we should probably split this method in several methods
    public void sendBlocks(Object o) throws IOException{
        if (o instanceof Integer) {
            Integer index = (Integer) o;
            if (blockchain.isEmpty()) {
                this.socket = new Socket(socket.getInetAddress().getHostAddress(), 7778);
                outputStream = this.socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                System.out.println("Blockchain is empty");
                objectOutputStream.writeObject("Is Empty");
            } else {
                for (int i = index; i < blockchain.size(); i++) {
                    this.socket = new Socket(socket.getInetAddress().getHostAddress(), 7778);
                    outputStream = this.socket.getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    System.out.println("blockchain length:" + blockchain.size());
                    objectOutputStream.writeObject(blockchain.get(i));
                    System.out.println(blockchain.get(i));
                }
                this.socket = new Socket(socket.getInetAddress().getHostAddress(), 7778);
                outputStream = this.socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject("Entire blockchain sent");
                System.out.println("Entire blockchain sent");
                this.socket.close();
            }
        } else if (o instanceof Block) {
            Block block = (Block) o;
            if (block.getPreviousHash().equals(blockchain.get(blockchain.size() - 1).getHash())) {
                addBlockToBlockchain(block);
            for(String peer : superpeer.getPeerIP()) {
                System.out.println("Sending blocks to Peer: " + peer);
                Socket peerSocket = new Socket(peer, 7778);
                System.out.println("Bob");
                outputStream = peerSocket.getOutputStream();
                System.out.println("Unicorn");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                System.out.println("Bla");
                objectOutputStream.writeObject(block);
                System.out.println("Sending back block");
                //peerSocket.close();
                }
            } else {
                    System.out.println("Invalid blockchain");
                }
            }
        }

        //method to receive the number of blocks a peer has
        //with that the super peer can check if the peer missed to receive some blocks
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


/*
    Guess we don't need that anymore ?!

        //opening a server for the super peer
    public  void superServer() throws IOException, ClassNotFoundException {
        Object o = Object
        if (o.equals("Connect to Super")) {
            readStorage();
            Client client = new Client(getIp(socket.getInetAddress().toString()), 7777);
            if (!ipAddresses.contains(getIp(socket.getInetAddress().toString()))) {
                add(socket);
                System.out.println(getIp(socket.getInetAddress().toString()));
            }
            client.sendEntireBlockchain();
        } else if (o == null) {
            System.out.println("bla");
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
                    client.sendBlock(blockchain);
                }
            }
             }

        public static String getIp(String ip){
        return ip.substring(1);
        }

                System.out.println("Connection from " + socket + "!");
        // get the input stream from the connected socket
        System.out.println("Where is the break?");
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        System.out.println("Bob");
        // create a DataInputStream so we can read data from it
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);


        if (blockchain.isEmpty()){
            sendBlocks(0);
        } else {
            for (int i = 0; i < blockchain.size(); i++) {
                if (blockchain.get(i).getPreviousHash().equals(block.getPreviousHash())) {
                    System.out.println("Ko");
                    sendBlocks(i);
                } else if (!blockchain.contains(block)) {
                    blockchain.add(block);
                    System.out.println("Bo");
                    sendBlocks(blockchain.size() - 1);
                }
            }
        }
    }

    if (blockchain.isEmpty()) {
                blockchain.add(newBlock);
                System.out.println(blockchain);
                //System.out.println(newBlock.getPreviousHash());
                //check if hash of last block equals previous hash of new block
            } else if ((blockchain.get(blockchain.size() - 1).getHash()).equals(newBlock.getPreviousHash())) {
                blockchain.add(newBlock);
                System.out.println("Received [" + blockchain.size() + "] messages from: " + socket);
                // print out the text of every message
                System.out.println("All messages:");
                System.out.println(blockchain);
                //blockchain.forEach((msg)-> System.out.println(msg));
                System.out.println("Length of blockchain: " + blockchain.size());
            } else {
                System.out.println("Invalid blockchain - missing link");
            }
 */