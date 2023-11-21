package node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import java.util.List;
import java.net.InetSocketAddress;

import utils.*;

public class FS_Node {
    private static Socket tcpSocket;
    private static DatagramSocket udpSocket;
    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static String filepath;
    private static File sharedFiles;

    public static void register() throws IOException{
        try{
            tcpSocket = new Socket("10.0.0.10", Utils.DEFAULT_PORT);
            try{
                udpSocket = new DatagramSocket(Utils.DEFAULT_PORT);
            }
            catch(SocketException e){
                System.out.println("Impossível iniciar conexão para seed");
                tcpSocket.close();
                System.exit(0);
            }
            dis = new DataInputStream(tcpSocket.getInputStream());
            dos = new DataOutputStream(tcpSocket.getOutputStream());
        }
        catch(IOException e){
            System.out.println("Impossível conectar-se ao servidor");
            System.exit(0);
        }

        dos.writeInt(udpSocket.getLocalPort());
        dos.flush();

        sharedFiles = new File(filepath);

        byte[] serializedSharedFiles = Utils.serializeMap(Chunks.getChunks(filepath, sharedFiles));
        dos.writeInt(serializedSharedFiles.length);
        dos.write(serializedSharedFiles);
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
    public static void requestDownload(String pedido){
        List<InetSocketAddress> nodosDisponveis;
        try{
            dos.writeUTF("REQUEST " + pedido);
            dos.flush();

            int length = dis.readInt();
            byte[] data = new byte[length];
            dis.readFully(data);
            nodosDisponveis = Utils.deserializeList(data);

            if(nodosDisponveis.size() > 0) {
                int chunks = dis.readInt();
                Transfer.selectNodes(nodosDisponveis, chunks);
            }
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        filepath = args[0];
        try{
            register();
        }
        catch(IOException e){
            e.printStackTrace();
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
                String pedido = UI.menuDownload();
                requestDownload(pedido);
            }
            else if(op == 2){
                System.out.println("Ficheiros partilhados: " + String.join(" ", sharedFiles.list()) + "\n");
            }
            else if(op == 3){
                end = true;
                quit();
                System.out.println("Desconectado com sucesso");
                return;
            }
        }
        System.out.println("Conexão ao servidor perdida");
    }
}
