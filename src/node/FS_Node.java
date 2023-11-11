package node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import utils.*;

public class FS_Node {
    private static Socket tcpSocket;
    private static DatagramSocket udpSocket;
    private static DataInputStream dis;
    private static DataOutputStream dos;

    public static void quit() throws IOException{
        dos.writeUTF("QUIT");
        dos.flush();
        tcpSocket.close();
        udpSocket.close();
        System.out.println("Desconectado com sucesso");
    }
    public static void main(String[] args) {
        try{
            tcpSocket = new Socket("localhost", Utils.DEFAULT_PORT);
            try{
                udpSocket = new DatagramSocket(Utils.DEFAULT_PORT);
            }
            catch(SocketException e){
                System.out.println("Impossível iniciar conexão para seed");
                System.exit(0);
            }
            dis = new DataInputStream(tcpSocket.getInputStream());
            dos = new DataOutputStream(tcpSocket.getOutputStream());

            dos.writeInt(Utils.DEFAULT_PORT);
            dos.flush();
        }
        catch(IOException e){
            System.out.println("Impossível conectar-se ao servidor");
            System.exit(0);
        }

        Thread server = new Thread(new SeedingServer(udpSocket));
        
        boolean end = false;
        while(!end){
            int op = UI.menuPrincipal();
            if (op == 1) {
                
            }
            else if(op == 2){
                end = true;
                try{
                    quit();
                }
                catch(IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
