package sample;

import javafx.scene.control.*;
import java.io.*;
import java.util.ArrayList;


public class Controller {
    //controller class for a usual JavaFX application

    private Peer peer;
    //text field to read the file path
    public TextField textFieldPath;
    //text field to read the user name
    public TextField textFieldUser;

    //initialize is the first method that will be called from the FX application
    //so before opening the GUI, we uses the initialize method to update a peers blockchain when connecting to superpeer
    public void initialize() {
        this.peer = new Peer("localhost");
        peer.connectToServer();
    }

    //method to prepare a new block that is supposed to be sent
    public void sendData(){
        peer.connectToServer();
        //we need to be sure that a file path and a user name was added
        if (!textFieldPath.getText().isBlank() && !textFieldUser.getText().isBlank()) {
          //prepare a new block based on the input in the text fields of the GUI
            try {
                Block block = peer.prepareBlock(textFieldUser.getText(), textFieldPath.getText());
                //send the prepared block to the super peer
                System.out.println(block.getFileTitle());
                peer.sendBlock(block);
            } catch (IOException e) {
                System.out.println("No such file available. Please try again..");
            }
        }else {
            System.out.println("Both username and path is needed");
        }
        //clear the text field for the file path
        textFieldPath.clear();
    }

    //method to view the blockchain in the run terminal
    public void viewBlockchain(){
        peer.connectToServer();
        peer.viewBlockchain();
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
}
