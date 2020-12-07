package sample;

import javafx.scene.control.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Controller {
        //controller class for a usual JavaFX application

    public static ArrayList<Block> blockchain = new ArrayList<>();

    Client superPeer; //WHY A SUPERPEER HERE ?
    public static int difficulty = 2;
        //text field to read the file path
    public TextField textFieldPath;
    //text field to read the user name
    public TextField textFieldUser;


    public void initialize() throws IOException, ClassNotFoundException {
            //MISLEADING ??
        superPeer = new Client("localhost", 7777);
        superPeer.connectToSuper();
        //deleteBlockchain();
        blockchain = superPeer.readStorage();
        System.out.println(blockchain.size());
    }

        //method to delete blockchain for testing purposes
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

        //A method which adds blocks to the blockchain
    public static void addBlock(Block newBlock) {
            //first need to mine the block and put some effort in
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
        newBlock.setHeight(newBlock.getHeight()+1);
        System.out.println("Block version number: "+newBlock.getHeight());
    }

        //A method for sending the blockchain over a network - this is run when you press the button
    public void sendData() throws IOException, ClassNotFoundException {
            //Probs not supposed to be Client. here
        blockchain = Client.readStorage();

            //When the blockchain is empty, the previous hash is 0; "initialization" of genesis block
        if (!textFieldUser.getText().isBlank() && !textFieldPath.getText().isBlank()) {
            if (blockchain.isEmpty()) {
                addBlock(new Block(new Metadata(textFieldPath.getText()), "0", textFieldUser.getText()));
            } else {
                addBlock(new Block(new Metadata(textFieldPath.getText()), blockchain.get(blockchain.size() - 1).getHash(), textFieldUser.getText()));
            }
        } else {
            System.out.println("Both username and path is needed");
        }
            //method to print the blockchain
        viewBlockchain();
            //Clears the textField after the block is added
        textFieldPath.clear();
            //initialize a new socket
        Socket socket = new Socket(superPeer.getIp(),superPeer.getPort());
            //and send the new blockchain
        superPeer.sendBlock(socket, blockchain);


    }
        //method to print the blockchain
    public void viewBlockchain() {
            //Json stand for JavaScript Object Notation
            //library to print out our blocks in a certain way
        String blockchainJson = StringUtil.getJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }
}
