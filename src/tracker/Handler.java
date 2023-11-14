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
    private InetSocketAddress clientAddress;

    private boolean end = false;

    public Handler(Socket clientSocket, Manager manager) throws IOException{
        this.clientSocket = clientSocket;
        this.manager = manager;
        this.dis = new DataInputStream(clientSocket.getInputStream());
        this.dos = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void register() throws IOException{
        int portaUDP = dis.readInt();
        String[] initRequest = dis.readUTF().split(" ");
        clientAddress = new InetSocketAddress(clientSocket.getInetAddress(), portaUDP);
        manager.registerNode(clientAddress, initRequest, this);
    }

    public void quit() throws IOException{
        end = true;
        dis.close();
        dos.close();
        clientSocket.close();
    }

    public void run(){
        try{
            register();
            System.out.println("Cliente " + clientAddress + " " + "conectado com sucesso");

            while(!end){
                String[] request = dis.readUTF().split(" ");
                if(request[0].equals("QUIT")){
                    quit();
                    manager.removeNode(clientAddress);
                }
            }
        }
        catch(IOException e){
            System.out.println("Cliente desconectado inesperadamente");
            manager.removeNode(clientAddress);
            return;
        }
        System.out.println("Cliente " + clientAddress + " " + "desconectado com sucesso");
    }
}
