package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Superpeer {
    //private final static String ip = "localhost";
    private final static int port = 7777;
    private static Client superPeer;
    private static ArrayList<String> ipAddresses = new ArrayList<String>();


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //superPeer = new Client(ip, port);
        //superPeer.setSuperPeer(true);
        superServer();
        updateIP();
        if(readIP().isEmpty()){
            System.out.println("No peers connected.");
        }
    }

    public static void add(Socket socket){
        ipAddresses.add(socket.getInetAddress().toString());
    }

    public static void updateIP(){
        try {
            FileOutputStream out = new FileOutputStream("ip.txt");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(ipAddresses);
            oos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readIP() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ip.txt"));

        //Reads the storage file and lets the ArrayList blockchain be equal to this
        return (ArrayList<String>) ois.readObject();
    }

    public static ArrayList<Block> readStorage() throws IOException, ClassNotFoundException {
        //Accesses the storage file, which is where the arraylist of the blockchain is.
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storage.txt"));

        //Reads the storage file and lets the ArrayList blockchain be equal to this
        return (ArrayList<Block>) ois.readObject();
    }

    public static void superServer() throws IOException, ClassNotFoundException {
        ServerSocket ss = new ServerSocket(port);
        System.out.println("ServerSocket awaiting connections...");
        Socket socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
        System.out.println("Connection from " + socket + "!");

        // get the input stream from the connected socket
        InputStream inputStream = socket.getInputStream();
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Object o = objectInputStream.readObject();
        if (o instanceof String){
        String input = (String) objectInputStream.readObject();
        if (input.equals("Connect to Super")) {
            readStorage();
            Client client = new Client(socket.getInetAddress().toString(), port);
            add(socket);
            client.sendEntireBlockchain();
        }
        } else{
            Server.serverConnection();
        }
        System.out.println("Closing sockets.");
        System.out.println("----------------------");
        ss.close();
        socket.close();
        superServer();
        superServer();
    }
}
