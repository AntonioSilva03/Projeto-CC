package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SeedingServer implements Runnable{
    private DatagramSocket udpSocket;

    public SeedingServer(DatagramSocket udpSocket){
        this.udpSocket = udpSocket;
    }

    public void run(){
        while(true){
            byte[] recieveData = new byte[1024];
            DatagramPacket request = new DatagramPacket(recieveData, recieveData.length);
            try{
                udpSocket.receive(request);
                Thread handleRequest = new Thread(new NodeHandler(request));
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
