package tracker;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class FS_Tracker {
    public static void main(String[] args){
        try{
            ServerSocket serverSocket = new ServerSocket();

            Manager manager = new Manager();

            while(true)
            {
                Socket clientSocket = serverSocket.accept();
            }
        }
        catch(IOException e){
            System.out.println("Impossível iniciar o servidor");
        }
    }
}
