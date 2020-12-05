package sample;

import javafx.scene.control.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Controller {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    //private static ArrayList<String> ipAddresses = new ArrayList<String>();
    Client superPeer;

    public static int difficulty = 2;

    public TextField textFieldPath;

    public void initialize() throws IOException, ClassNotFoundException {
        superPeer = new Client("localhost", 7777);
        superPeer.connectToSuper();
        deleteBlockchain();
        blockchain = superPeer.readStorage();
        System.out.println(blockchain.size());
    }

    public void deleteBlockchain(){
        try {
            FileOutputStream out = new FileOutputStream("storage.txt");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            ArrayList<Block> empty = new ArrayList<Block>();
            oos.writeObject(empty);
            oos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
        /*
    public static ArrayList<Block> readStorage() throws IOException, ClassNotFoundException {
        //Accesses the storage file, which is where the arraylist of the blockchain is.
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storage.txt"));

        //Reads the storage file and lets the ArrayList blockchain be equal to this
        blockchain = (ArrayList<Block>) ois.readObject();
        return blockchain;
    }*/

    //A method which adds blocks to the blockchain
    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
        newBlock.setHeight(newBlock.getHeight()+1);
        System.out.println("Block version number: "+newBlock.getHeight());
    }

    //A method for sending the blockchain over a network
    public void sendData() throws IOException, ClassNotFoundException {
        blockchain = Client.readStorage();

        //When the blockchain is empty, the previous hash is 0 -
        // which is different than the rest of the blockchain
        if (blockchain.isEmpty()) {
            addBlock(new Block(new Metadata(textFieldPath.getText()), "0"));
        } else {
            addBlock(new Block(new Metadata(textFieldPath.getText()), blockchain.get(blockchain.size()-1).getHash()));
        }
        viewBlockchain();
        //Clears the textField after the block is added
        textFieldPath.clear();
        superPeer.sendBlock();
    }

    public void viewBlockchain() {
        String blockchainJson = StringUtil.getJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }
}
