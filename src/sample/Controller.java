package sample;

import javafx.scene.control.*;
import java.io.*;
import java.util.ArrayList;


public class Controller {
        //controller class for a usual JavaFX application

    Peer peer;
        //text field to read the file path
    public TextField textFieldPath;
    //text field to read the user name
    public TextField textFieldUser;


  public void initialize() throws IOException, ClassNotFoundException {
        //we need to remember to mention why we just make a new peer here, not a super peer
      this.peer = new Peer("localhost");
      peer.connectToServer();
    }

        //method to prepare a new block that is supposed to be sent
    public void sendData() throws IOException, ClassNotFoundException {
        if (!textFieldPath.getText().isBlank() && !textFieldUser.getText().isBlank()) {
                //prepare a new block based on the input in the text fields of the GUI
            Block block = peer.prepareBlock(textFieldUser.getText(),textFieldPath.getText());
                //send the prepared block to the super peer
            peer.sendBlock(block);
        }else {
            System.out.println("Both username and path is needed");
        }
            //clear the text field for the file path
        textFieldPath.clear();
    }

        //method to view the blockchain in the run terminal
        //is that really necessary ?
    public void viewBlockchain() {
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
