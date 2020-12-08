package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PeerConnection implements Runnable {

    Socket socket;
    private Superpeer superpeer;
    InputStream inputStream;
    OutputStream outputStream;
    ArrayList<Block> blockchain = new ArrayList<Block>();

    @Override
    public void run() {
        try {
            establishStreams();

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public PeerConnection(Socket socket, Superpeer superpeer) {
        this.socket = socket;
        this.superpeer = superpeer;
    }

    public void establishStreams() throws IOException, ClassNotFoundException, InterruptedException {
        //I nt tråd PeerConnection
        //add to active connections
        //add()
        superpeer.add(this.socket);


        //receive blocks
        //Mangler method in this class

        this.blockchain = superpeer.getBlocks();

        //sortering
        sendBlocks(receiveBlocks());
        // send blocks
        //Mangler method in this class


    }
    public void sendBlocks(Object o) throws IOException, InterruptedException {
        System.out.println("Sending blocks");
        if (o instanceof Integer){
            Integer index = (Integer) o;
            for (int i = index; i < blockchain.size(); i++){
                Socket socket1 = new Socket(socket.getInetAddress().toString().substring(1),7778);
                outputStream = socket1.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                System.out.println("blockchain length:" +blockchain.size());
                objectOutputStream.writeObject(blockchain.get(i));
                //TimeUnit.SECONDS.sleep(10);
                System.out.println(blockchain.get(i));
            }
        } else if (o instanceof Block){
            Block block = (Block) o;
            Socket socket1 = new Socket(socket.getInetAddress().toString().substring(1),7778);
            outputStream = socket1.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(block);
        }
    }

    public Object receiveBlocks() throws IOException, ClassNotFoundException {
        System.out.println("Receiving blocks");
        inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        //Object o = objectInputStream.readObject();
        //int sizeOfPeerBlockchain = (int) objectInputStream.readObject();
       // System.out.println(sizeOfPeerBlockchain);
        return objectInputStream.readObject();
    }
}


/*
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