package sample;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

//class the sets up a connection for the peers and the super peer

//Based on program from https://gist.github.com/chatton/14110d2550126b12c0254501dde73616
public class Server {
    private final ArrayList<Block> blockchain;

    private ObjectInputStream objectInputStream;

    //constructor for a server
    public Server(ArrayList<Block> blockchain) {
        this.blockchain = blockchain;
    }

    //method that opens a server socket and listens for incoming peers and messages
    public boolean serverConnection(){
        try {
            ServerSocket ss = new ServerSocket(7778);
            System.out.println("Waiting for connection...");
            // blocking call, this will wait until a connection is attempted on this port
            Socket socket = ss.accept();
            //after successful connection we close the server socket
            ss.close();
            // get the input stream from the connected socket
            InputStream inputStream = socket.getInputStream();
            // create a DataInputStream so we can read data from it.
            this.objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException i) {
            i.printStackTrace();
            System.out.println("No server socket available, or no connection with incoming requests." +
                        " \nTrying again..");
        }
        return receiveInput();
    }

    //method to check what kind of object has been received
    //if it is one of the two strings then we break the while loop in the connectToServer method in peer
    //else a block was received
    private boolean receiveInput() {
        //we need to initialize the object outside the try-catch statement
        //so the do-while loop makes sure that we don't return a "null" object
        //which would give us a nullpointerexception
        Object o = "null";
        do {
            try {
                o = this.objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (o.equals("Entire blockchain sent") || o.equals("Is Empty")) {
                return true;
            } else {
                Block block = (Block) o;
                //will only add block to blockchain if the blockchain is either
                //empty or the last hash of the blockchain is the same as the previous hash of the new block
                blockchain.add(block);
                saveBlockchain();
                System.out.println();
                return false;
            }
        }while (o.equals("null")) ;
    }

    //method to update the storage file
    private void saveBlockchain(){
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
