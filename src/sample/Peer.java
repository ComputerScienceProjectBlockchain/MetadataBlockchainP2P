package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Peer{
    private final String superPeerIP;
    private final int port = 7777;
    private ArrayList<Block> blockchain = new ArrayList<Block>();
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;


    public Peer(String superPeerIP) {
        this.superPeerIP = superPeerIP;
        connectToSuper();
    }

    //while loop will run until the server receives the message "done"
    //this message came from the super peer and means that the peer now has the latest version of the blockchain
    public void connectToServer(){
        while(true) {
            System.out.println("Connecting to server");
            Server server = new Server(blockchain);
            boolean bool = server.serverConnection();
            //if method to get out of the while loop
            if (bool){
                System.out.println("Entire blockchain received");
                break;
            }
        }
    }
            //method to connect the peer to the super peer
            public void connectToSuper(){
        Socket socket =null;
            //read the storage txt file and save the blockchain it contains
            //make a new socket with the ip of the super peer and a port
        //a do-while loop was added, so that if we catch an exception, the program will try again
        //until the socket is not null anymore
        do{
            try {
                blockchain = readStorage();
                socket = new Socket(this.superPeerIP, port);
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
            }catch(IOException i){
                System.out.println("No connection to a socket established. \n Trying again...");
            }catch( ClassNotFoundException c){
                c.printStackTrace();
                System.out.println("The method readStorage() had no text file to read from.");
            }
        }while(socket == null);
    }

            //connects to the super peer and sends a block the peer wants to add to the blockchain
            //does the same as the other connectToSuper method
            //the only thing that differs is the this method send a block instead of the size of the blockchain
    private void connectToSuper(Block block) {
        Socket socket = null;
        //here we also added a do-while loop in order to not exit the program when we catch an exception
        do {
            try {
                blockchain = readStorage();
                socket = new Socket(superPeerIP, port);
                System.out.println("Connected!");

                // get the output stream from the socket.
                this.outputStream = socket.getOutputStream();
                // create an object output stream from the output stream so we can send an object through it
                this.objectOutputStream = new ObjectOutputStream(this.outputStream);
                System.out.println("Sending messages to the ServerSocket");
                //here we send a block to the super peer
                this.objectOutputStream.writeObject(block);
                System.out.println(block);
            } catch (IOException i) {
                i.printStackTrace();
                System.out.println("Not connected to any socket.");
            }catch( ClassNotFoundException c){
                c.printStackTrace();
                System.out.println("The method readStorage() had no text file to read from.");
            }
        }while(socket == null);
    }

            //returns a block the either is the genesis block
            //or another block that is supposed to be added to the blockchain
    public Block prepareBlock(String userName, String path) throws IOException {
            //When the blockchain is empty, the previous hash is 0; "initialization" of genesis block
        Block block;
        //do-while loop that first stops when we successfully retrieved metadata
                   if (blockchain.isEmpty()) {
                       //generation of a genesis block
                       //genesis block is the first the block in a blockchain; it has no previous hash to refer to
                       block = (new Block(new Metadata(path), "0", userName));
                   } else {
                       block = (new Block(new Metadata(path), blockchain.get(blockchain.size() - 1).getHash(), userName));
                   }
               // catch (IOException i) {
                 //  System.out.println("No such file available..");
                   //System.out.println("Check your path.");
        return block;
    }

        //sends a prepared block to the super peer
    public void sendBlock(Block preparedBlock) {
        connectToSuper(preparedBlock);
        Server server = new Server(blockchain);
        server.serverConnection();
    }

        //method to read the "storage" file where the blockchain is stored
        //returns an arraylist containing the blockchain
        //both exceptions need to be passed on because of what we return
    private ArrayList<Block> readStorage() throws IOException, ClassNotFoundException {
                //Accesses the storage file, which is where the arraylist of the blockchain is.
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storage.txt"));
            //Reads the storage file and lets the ArrayList blockchain be equal to this
        return (ArrayList<Block>) ois.readObject();
    }

        //method to view the blockchain in the terminal
    public void viewBlockchain() {
        try {
            //read out the latest version of the txt-file
            blockchain = readStorage();
        }catch(IOException i){
            i.printStackTrace();
            System.out.println("Not connected to any socket.");
        }catch (ClassNotFoundException c){
            c.printStackTrace();
            System.out.println("The method readStorage() had no text file to read from.");
        }
        if (blockchain.isEmpty()) {
            System.out.println("No blockchain to view");
        } else {
            //StringUtil.getJson is a method that uses "PrettyPrinting"
            //can be used to write our blocks in nice form
            String blockchainJson = StringUtil.getJson(blockchain);
            System.out.println("\nThe block chain: ");
            System.out.println(blockchainJson);
        }
    }
}