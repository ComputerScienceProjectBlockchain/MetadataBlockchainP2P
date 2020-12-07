package sample;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

//class the sets up a connection for the peers and the super peer

//Based on program from https://gist.github.com/chatton/14110d2550126b12c0254501dde73616
public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        serverConnection();
    }

    public static void serverConnection() throws IOException, ClassNotFoundException {
            // don't need to specify a hostname, it will be the current machine
        ServerSocket ss = new ServerSocket(7777);
        System.out.println("ServerSocket awaiting connections...");
            // blocking call, this will wait until a connection is attempted on this port
        Socket socket = ss.accept();
        System.out.println("Connection from " + socket + "!");

            // get the input stream from the connected socket
        InputStream inputStream = socket.getInputStream();
            // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            //get the version of the blockchain the client has at the moment
        ArrayList<Block> blockchain = Client.readStorage();
        //ArrayList<Block> blockchain = (ArrayList<Block>) objectInputStream.readObject();
            //save the input as an object
        Object o = objectInputStream.readObject();
            //check if the block is an arraylist or a block
        if (o instanceof Block) {
            Block newBlock = (Block) objectInputStream.readObject();
                //compare method to check if the file name already appears in the blockchain
            compareBlocks(blockchain,newBlock);
                //will only add block to blockchain if the blockchain is either
                //empty or the last hash of the blockchain is the same as the previous hash of the new block
            if (blockchain.isEmpty()) {
                blockchain.add(newBlock);
                System.out.println(blockchain);
                    //DO WE NEED THIS ELSE IF  ???
            } else if (blockchain.size() == 1) {
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
            //if it is an arraylist, then this is an updated version of the blockchain
        } else if (o instanceof ArrayList){
            //blockchain = (ArrayList<Block>) objectInputStream.readObject();
            blockchain = (ArrayList<Block>) o;
        }

            //Store the current blockchain in the storage file
            //FileOutputStream is an output stream for adding data to a file
            //Everytime this code is run the txt file is overwritten into the new txt file
        try {
            FileOutputStream out = new FileOutputStream("storage.txt");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(blockchain);
            oos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("Closing sockets.");
        System.out.println("----------------------");
        ss.close();
        socket.close();
            //runs repetitive so that we always can "listen" to connections
        serverConnection();
    }

        //method to check if a file has been added before
        //and if so compare last accessed time and the modified time
    public static void compareBlocks(ArrayList<Block> blockchain, Block newBlock) {
        if (blockchain.size() > 0) {
            for (int i = 0; i < blockchain.size(); i++) {
                if (blockchain.get(i).getFileTitle().equals(newBlock.getFileTitle())){
                    System.out.println("Block number " + i + " has the same name as the new file");
                    if (!blockchain.get(i).getFileAccessedTime().equals(newBlock.getFileAccessedTime())){
                        System.out.println("The file: " + newBlock.getFileTitle() + " was last accessed " + newBlock.getFileAccessedTime() + " by " + newBlock.getUserName());
                    } if (!blockchain.get(i).getFileModifiedTime().equals(newBlock.getFileModifiedTime())){
                        System.out.println("The file: " + newBlock.getFileTitle() + " was last modified " + newBlock.getFileModifiedTime() + " by " + newBlock.getUserName());
                }
            }
        }
        }
    }
}
