package sample;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
//Based on program from https://gist.github.com/chatton/14110d2550126b12c0254501dde73616
public class Server {
    public static Client client;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        serverConnection();
    }
    public static void serverConnection() throws IOException, ClassNotFoundException {
        // don't need to specify a hostname, it will be the current machine
        ServerSocket ss = new ServerSocket(7777);
        System.out.println("ServerSocket awaiting connections...");
        Socket socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
        client = new Client(socket.getInetAddress().toString(), socket.getPort());
        Superpeer.add(socket);
        System.out.println("Connection from " + client.getSocket() + "!");

        // get the input stream from the connected socket
        InputStream inputStream = client.getSocket().getInputStream();
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        ArrayList<Block> blockchain = client.readStorage();
        //ArrayList<Block> blockchain = (ArrayList<Block>) objectInputStream.readObject();
        Block newBlock = (Block) objectInputStream.readObject();
        //Will only add block to blockchain - if the blockchain is either;
        // empty or the last hash of the blockchain is the same as the previous hash of the new block
        if(blockchain.isEmpty()) {
            blockchain.add(newBlock);
            System.out.println(blockchain);
        } else if (blockchain.size()==1){
            blockchain.add(newBlock);
            System.out.println(blockchain);
            //System.out.println(newBlock.getPreviousHash());
        } else if((blockchain.get(blockchain.size()-1).getHash()).equals(newBlock.getPreviousHash())){
            blockchain.add(newBlock);
            System.out.println("Received [" + blockchain.size() + "] messages from: " + client.getSocket());
            // print out the text of every message
            System.out.println("All messages:");
            System.out.println(blockchain);
            //blockchain.forEach((msg)-> System.out.println(msg));
            System.out.println("Length of blockchain: " + blockchain.size());
        }else {
            System.out.println("Invalid blockchain - missing link");
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
        serverConnection();
    }
}
