package tracker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Handler implements Runnable {
    private Socket clientSocket;
    private Manager manager;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Handler(Socket clientSocket, Manager manager) throws IOException{
        this.clientSocket = clientSocket;
        this.manager = manager;
        this.dis = new DataInputStream(clientSocket.getInputStream());
        this.dos = new DataOutputStream(clientSocket.getOutputStream());
    }
    public void run(){
        String initRequest = dis.readUTF();
        InetSocketAddress clientAddress = new InetSocketAddress(clientSocket.getInetAddress(), Integer.parseInt(initRequest));

        boolean end = false;
        while(!end){
        }
    }
}
