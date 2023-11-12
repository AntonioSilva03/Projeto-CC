package node;

import java.net.DatagramPacket;

public class NodeHandler implements Runnable{
    private DatagramPacket dataRequest;
    
    public NodeHandler(DatagramPacket dataRequest){
        this.dataRequest = dataRequest;
    }

    public void run(){

    }
}
