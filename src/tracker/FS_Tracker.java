package tracker;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import utils.Utils;

public class FS_Tracker {
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(Utils.DEFAULT_PORT);

        Manager manager = new Manager();

        while(true)
        {
            Socket clientSocket = serverSocket.accept();
            Thread handleRequest = new Thread(new Handler(clientSocket, manager));
            handleRequest.start();
        }
    }
}
