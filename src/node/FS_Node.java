package node;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import utils.*;

public class FS_Node {
    public static void main(String[] args) {
        try{
            Socket socket = new Socket("localhost", Utils.DEFAULT_PORT);
            DatagramSocket nodeSocket = new DatagramSocket(Utils.DEFAULT_PORT);

            
        }
        catch(IOException e){
            System.out.println("Impossível iniciar o cliente");
        }
    }
}
