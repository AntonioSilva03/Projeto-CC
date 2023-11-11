package node;

import java.net.DatagramSocket;

public class SeedingServer implements Runnable{
    private DatagramSocket udpSocket;

    public SeedingServer(DatagramSocket udpSocket){
        this.udpSocket = udpSocket;
    }

    public void run(){
        /*while(true){

        }*/
    }
}
