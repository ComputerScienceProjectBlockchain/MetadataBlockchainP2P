package sample;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

//class the sets up a connection for the peers and the super peer

//Based on program from https://gist.github.com/chatton/14110d2550126b12c0254501dde73616
public class Server {
    ArrayList<Block> blockchain;
    InputStream inputStream;
    ObjectInputStream objectInputStream;

        //constructor for a server
    public Server(ArrayList<Block> blockchain) {
        this.blockchain = blockchain;
    }

        //method that opens a server socket and listens for incoming peers and messages
    public boolean serverConnection(){
        ServerSocket ss = null;
        Socket socket = null;
        do {
            try {
                ss = new ServerSocket(7778);
                System.out.println("Waiting for connection...");
                // blocking call, this will wait until a connection is attempted on this port
                socket = ss.accept();
                //after successful connection we close the server socket
                ss.close();
                // get the input stream from the connected socket
                this.inputStream = socket.getInputStream();
                // create a DataInputStream so we can read data from it.
                this.objectInputStream = new ObjectInputStream(inputStream);
            } catch (IOException i) {
                i.printStackTrace();
                System.out.println("No server socket available, or no connection with incoming requests. \nTrying again..");
            }
        }while(ss != null && socket != null);
        return receiveInput();
    }

            //method to check what kind of object has been received
            //if it is one of the two strings then we break the while loop in the connectToServer method in peer
            //else a block was received
    public boolean receiveInput() {
        Object o = null;
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
                //compare method to check if the file name already appears in the blockchain
                compareBlocks(blockchain, block);
                //will only add block to blockchain if the blockchain is either
                //empty or the last hash of the blockchain is the same as the previous hash of the new block
                blockchain.add(block);
                saveBlockchain();
                return false;
            }
        }while(o == null);
    }

        //method to update the storage file
    public void saveBlockchain(){
        try {
            FileOutputStream out = new FileOutputStream("storage.txt");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(blockchain);
            oos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

        //method to check if a file has been added before
        //and if so compare last accessed time and the modified time
        //maybe move to super server ?
    public static void compareBlocks(ArrayList<Block> blockchain, Block newBlock) {
        if (blockchain.size() > 0) {
            for (int i = 0; i < blockchain.size(); i++)
            {
                String fileTitle = blockchain.get(i).getFileTitle();
                String accessedTime = blockchain.get(i).getFileAccessedTime();
                String modifiedTime = blockchain.get(i).getFileModifiedTime();
                if (fileTitle.equals(newBlock.getFileTitle()))
                {
                    System.out.println("Block number " + i + " has the same name as the new file");
                    if (!accessedTime.equals(newBlock.getFileAccessedTime()))
                    {
                        System.out.println("The file: " + newBlock.getFileTitle() + " was last accessed " + newBlock.getFileAccessedTime() + " by " + newBlock.getUserName());
                    } if (!modifiedTime.equals(newBlock.getFileModifiedTime()))
                    {
                        System.out.println("The file: " + newBlock.getFileTitle() + " was last modified " + newBlock.getFileModifiedTime() + " by " + newBlock.getUserName());
                    }
                }
            }
        }
    }
}
