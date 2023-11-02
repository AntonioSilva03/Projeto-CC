package node;

import java.io.IOException;
import java.net.DatagramSocket;

import utils.*;

public class FS_Node {
    public static void main(String[] args) {
        try{
            DatagramSocket nodeSocket = new DatagramSocket(Utils.DEFAULT_PORT);
        }
        catch(IOException e){
            System.out.println("Impossível iniciar o cliente");
        }
    }
}
