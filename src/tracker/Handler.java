package tracker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Handler implements Runnable {
    private Socket clientSocket;
    private Manager manager;
    private DataInputStream dis;
    private DataOutputStream dos;
    InetSocketAddress clientAddress;

    public Handler(Socket clientSocket, Manager manager) throws IOException{
        this.clientSocket = clientSocket;
        this.manager = manager;
        this.dis = new DataInputStream(clientSocket.getInputStream());
        this.dos = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void register() throws IOException{
        int initRequest = dis.readInt();
        clientAddress = new InetSocketAddress(clientSocket.getInetAddress(), initRequest);
        manager.registerNode(clientAddress);
    }

    public void quit() throws IOException{
        manager.removeNode(clientAddress);
        dis.close();
        dos.close();
        clientSocket.close();
    }

    public void run(){
        try{
            register();
            System.out.println("Cliente " + clientAddress + " " + "conectado com sucesso");

            boolean end = false;
            while(!end){
                String[] request = dis.readUTF().split(" ");
                if(request[0].equals("QUIT")){
                    end = true;
                    quit();
                }
            }
        }
        catch(IOException e){
            System.out.println("Cliente desconectado inesperadamente");
            manager.removeNode(clientAddress);
        }
        System.out.println("Cliente " + clientAddress + " " + "desconectado com sucesso");
    }
}
