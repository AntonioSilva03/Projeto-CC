package node;

import utils.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class NodeHandler implements Runnable{
    private DatagramSocket serverUdpSocket;
    private DatagramPacket dataRequest;
    private InetAddress clientAddress;
    private int clientPort;
    
    public NodeHandler(DatagramPacket dataRequest, DatagramSocket serverUdpSocket){
        this.serverUdpSocket = serverUdpSocket;
        this.dataRequest = dataRequest;
        this.clientAddress = dataRequest.getAddress();
        this.clientPort = dataRequest.getPort();
    }

    public List<byte[]> readFile(String file, int startOffset, int endOffset){
        List<byte[]> chunks = FS_Node.splittedFiles.get(file);

        List<byte[]> chunksPedidas = new ArrayList<>(chunks.subList(startOffset, endOffset));
        return chunksPedidas;
    }

    public void run(){
        try{
            String requestString = Utils.deserializeString(dataRequest.getData());
            String[] request = requestString.split(" ");

            String file = request[0];
            int startOffset = Integer.parseInt(request[1]);
            int endOffset = Integer.parseInt(request[2]);

            byte[] data = Utils.serializeListBytes(readFile(file, startOffset, endOffset));
            DatagramPacket dataResponse = new DatagramPacket(data, data.length, clientAddress, clientPort);

            serverUdpSocket.send(dataResponse);
            System.out.println("Ficheiro " + file + " enviado com sucesso\n");
        }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
    }
}
