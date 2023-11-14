package node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

import utils.*;

public class FS_Node {
    private static Random random = new Random();
    private static Socket tcpSocket;
    private static DatagramSocket udpSocket;
    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static String filepath;

    public static void register() throws IOException{
        File sharedFiles = new File(filepath);
        dos.writeInt(udpSocket.getLocalPort());
        dos.flush();
        dos.writeUTF(String.join(" ", sharedFiles.list()));
        dos.flush();
    }
    public static void disconnect(){
        try{
            SeedingServer.setState(false);
            tcpSocket.close();
            udpSocket.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void quit(){
        try{
            dos.writeUTF("QUIT");
            dos.flush();
            disconnect();
        }
        catch(IOException e){
            disconnect();
        }
    }
    public static void main(String[] args) {
        filepath = args[0];
        try{
            tcpSocket = new Socket("10.0.0.10", Utils.DEFAULT_PORT);
            try{
                udpSocket = new DatagramSocket(Utils.DEFAULT_PORT /* +random.nextInt(101)*/); // Adiciona um número entre 1 e 100 à porta para iniciar vários nodes na mesma máquina.
            }
            catch(SocketException e){
                System.out.println("Impossível iniciar conexão para seed");
                tcpSocket.close();
                System.exit(0);
            }
            dis = new DataInputStream(tcpSocket.getInputStream());
            dos = new DataOutputStream(tcpSocket.getOutputStream());

            register();
        }
        catch(IOException e){
            System.out.println("Impossível conectar-se ao servidor");
            System.exit(0);
        }

        Thread server = new Thread(new SeedingServer(udpSocket));
        server.start();
        
        boolean end = false;
        while(!end){
            if(!Utils.checkConnection(dos)){
                disconnect();
                break;
            }
            int op = UI.menuPrincipal();
            if (op == 1) {
                
            }
            else if(op == 2){
                end = true;
                quit();
                System.out.println("Desconectado com sucesso");
                return;
            }
        }
        System.out.println("Conexão ao servidor perdida");
    }
}
