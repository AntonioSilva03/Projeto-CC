package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SeedingServer implements Runnable{
    private DatagramSocket udpSocket;
    private static boolean state;

    public SeedingServer(DatagramSocket udpSocket){
        this.udpSocket = udpSocket;
        state = true;
    }

    public void run(){
        while(state){
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

    public static void setState(boolean state){
        SeedingServer.state = state;
    }
}
