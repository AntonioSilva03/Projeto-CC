package tracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import utils.Utils;

/**
 * Classe principal do servidor
 * Responsável por receber conexões e criar handlers para cada cliente conectado.
 */
public class FS_Tracker {
    public static boolean state = true;
    public static ServerSocket serverSocket;
    public static void main(String[] args) throws IOException{
        serverSocket = new ServerSocket(Utils.DEFAULT_PORT);

        System.out.println("Servidor ativo em " + serverSocket.getInetAddress().getHostAddress() + " na porta " + serverSocket.getLocalPort());

        Manager manager = new Manager();

        Thread interactions = new Thread(new Interact(manager));
        interactions.start();

        while(state)
        {
            try{
                Socket clientSocket = serverSocket.accept();
                Thread handleRequest = new Thread(new Handler(clientSocket, manager));
                handleRequest.start();
            }
            catch(IOException e){
                System.out.println("Servidor fechado");
            }
        }
    }
}
