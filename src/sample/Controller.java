package sample;

import javafx.scene.control.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Controller {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public TextField textFieldUser;
    ArrayList<String> ipAddresses = new ArrayList<String>(){{
        //add("192.168.0.12");
        //add("192.168.0.17");
        add("localhost");
        //add("192.168.43.63");
        //add("192.168.43.156");
        //add("192.168.43.213");
    }};

    public static int difficulty = 2;

    public TextField textFieldPath;

    public void initialize() throws IOException, ClassNotFoundException {
        deleteBlockchain();
        blockchain = readStorage();
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

    public static ArrayList<Block> readStorage() throws IOException, ClassNotFoundException {
        //Accesses the storage file, which is where the arraylist of the blockchain is.
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storage.txt"));

        //Reads the storage file and lets the ArrayList blockchain be equal to this
        blockchain = (ArrayList<Block>) ois.readObject();
        return blockchain;
    }

    //A method which adds blocks to the blockchain
    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
        newBlock.setHeight(newBlock.getHeight()+1);
        System.out.println("Block version number: "+newBlock.getHeight());
    }

    //A method for sending the blockchain over a network
    public void sendData() throws IOException, ClassNotFoundException {
        blockchain = readStorage();

        //When the blockchain is empty, the previous hash is 0 -
        // which is different than the rest of the blockchain
        if (!textFieldUser.getText().isBlank() && !textFieldPath.getText().isBlank()) {
            if (blockchain.isEmpty()) {
                addBlock(new Block(new Metadata(textFieldPath.getText()), "0", textFieldUser.getText()));
            } else {
                addBlock(new Block(new Metadata(textFieldPath.getText()), blockchain.get(blockchain.size() - 1).getHash(), textFieldUser.getText()));
            }
        String blockchainJson = StringUtil.getJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
        //Clears the textField after the block is added
        textFieldUser.clear();
        textFieldPath.clear();
        //When working with one computer, you use localhost as the host
        // - otherwise you use the receivers ip-address
        for (int i = 0; i < ipAddresses.size(); i++) {
            Socket socket = new Socket(ipAddresses.get(i), 7777);
            System.out.println("Connected!");

            // get the output stream from the socket.
            OutputStream outputStream = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("Sending messages to the ServerSocket");
            objectOutputStream.writeObject(blockchain.get(blockchain.size()-1));

            System.out.println("Closing socket, another connection is now available");
            System.out.println("--------------------------------------------");
            socket.close();
            updateBlockchain(blockchain);
            }
        } else {
            System.out.println("Both username and path is needed");
        }
    }

    public void viewBlockchain() {
        String blockchainJson = StringUtil.getJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }
}
