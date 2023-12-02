package tracker;

import utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
/**
 * Classe que recebe e lida com pedidos de um cliente conectado ao servidor
 */
public class Handler implements Runnable {
    private Socket clientSocket;
    private Manager manager;
    private DataInputStream dis;
    private DataOutputStream dos;
    private InetSocketAddress clientAddress;

    private boolean end = false;

    /**
     * Função que inicializa um handler para pedidos do cliente.
     * @param clientSocket Socket para comunicações com o cliente
     * @param manager Base de dados da rede
     * @throws IOException
     */
    public Handler(Socket clientSocket, Manager manager) throws IOException{
        this.clientSocket = clientSocket;
        this.manager = manager;
        this.dis = new DataInputStream(clientSocket.getInputStream());
        this.dos = new DataOutputStream(clientSocket.getOutputStream());
    }

    /**
     * Função que regista um cliente na sua base de dados
     * @throws IOException
     */
    public void register() throws IOException{
        int portaUDP = dis.readInt();
        int length = dis.readInt();
        byte[] initRequest = new byte[length];
        dis.readFully(initRequest);
        clientAddress = new InetSocketAddress(clientSocket.getInetAddress(), portaUDP);
        manager.registerNode(clientAddress, initRequest, this);
    }

    /**
     * Função que desconecta um cliente do servidor de forma limpa.
     * @throws IOException
     */
    public void quit() throws IOException{
        end = true;
        dis.close();
        dos.close();
        clientSocket.close();
    }

    /**
     * Função que recolhe todos os nodos disponíveis para partilha de um ficheiro e informa o cliente
     * @param file Ficheiro pedido
     */
    public void getNodes(String file){
        List<InetSocketAddress> disponiveis = manager.getNodesFile(file);
        try{
            byte[] serialized = Utils.serializeList(disponiveis);
            dos.writeInt(serialized.length);
            dos.flush();
            dos.write(serialized);
            dos.flush();

            if(disponiveis.size() > 0){
                int chunks = manager.getChunks(disponiveis.get(0), file);
                dos.writeInt(chunks);
                dos.flush();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Função que recebe os pedidos do cliente
     */
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
                else if(request[0].equals("REQUEST")){
                    getNodes(request[1]);
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
