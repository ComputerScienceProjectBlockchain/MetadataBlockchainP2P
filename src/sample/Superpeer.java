package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Superpeer {
    private final static String ip = "localhost";
    private final static int port = 7777;
    private static Client superPeer;


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        superPeer = new Client(ip, port);
        superPeer.setSuperPeer(true);

        updateIP();
        if(superPeer.readIP().isEmpty()){
            System.out.println("No peers connected.");
        }
    }

    public static void add(Socket socket){
        superPeer.getIpAddresses().add(socket.getInetAddress().toString());
    }

    public static void updateIP(){
        try {
            FileOutputStream out = new FileOutputStream("ip.txt");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(superPeer.getIpAddresses());
            oos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
