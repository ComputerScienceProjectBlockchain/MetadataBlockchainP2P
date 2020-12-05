package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Superpeer {
    private final static String ip = "localhost";
    private final static int port = 7777;
    private static Client superPeer;
    private static ArrayList<String> ipAddresses = new ArrayList<String>();


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        superPeer = new Client(ip, port);
        superPeer.setSuperPeer(true);

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


}
