package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Classe que trata de receber pedidos UDP de outros clientes.
 */
public class SeedingServer implements Runnable{
    private DatagramSocket udpSocket;
    private static boolean state;

    /**
     * Função que inicializa o servidor, guarda o socket UDP e também o seu estado atual para futuro encerramento limpo.
     * @param udpSocket
     */
    public SeedingServer(DatagramSocket udpSocket){
        this.udpSocket = udpSocket;
        state = true;
    }

    /**
     * Função que corre o servidor e recebe novas conexões, criando novas threads independentes para cada cliente conectado.
     */
    public void run(){
        while(state){
            byte[] recieveData = new byte[1024];
            DatagramPacket request = new DatagramPacket(recieveData, recieveData.length);
            try{
                udpSocket.receive(request);
                Thread handleRequest = new Thread(new NodeHandler(request, udpSocket));
                handleRequest.start();
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Função que muda o estado do servidor para encerramento limpo
     * @param state
     */
    public static void setState(boolean state){
        SeedingServer.state = state;
    }
}
