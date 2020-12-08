package sample;

import javafx.scene.control.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Controller {
        //controller class for a usual JavaFX application

    public static ArrayList<Block> blockchain = new ArrayList<>();
    Peer peer;
        //text field to read the file path
    public TextField textFieldPath;
    //text field to read the user name
    public TextField textFieldUser;


    public void initialize() throws IOException, ClassNotFoundException {
        Peer peer = new Peer("localhost");
        Socket socket = peer.connectToSuper();
        peer.connectToServer(socket);
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

        //A method for sending the blockchain over a network - this is run when you press the button
    public void sendData() throws IOException, ClassNotFoundException {
       //peer.sendBlockToSuper();
        peer.sendBlockToBlockchain(textFieldUser.getText(),textFieldPath.getText());


    }
        //method to print the blockchain
    public void viewBlockchain() {
        //peer.viewBlockchain();
        peer.viewBlockchain();
    }
}
