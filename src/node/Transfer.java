/*
 * Algoritmo de seleção e transferência de chunks.
 */
package node;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;

import java.net.InetSocketAddress;
import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;

public class Transfer {
    public static void handleResponse(String file, DatagramPacket response, int startOffset, int endOffset) throws ClassNotFoundException, IOException{
        byte[] recieved = response.getData();
        List<byte[]> chunks = Utils.deserializeListBytes(recieved);

        if(FS_Node.splittedFiles.containsKey(file)){
            for(int i = startOffset, j = 0; i < endOffset; i++, j++){
                FS_Node.splittedFiles.get(file).add(i, chunks.get(j));
            }
        }
        else if(!FS_Node.splittedFiles.containsKey(file)){
            List<byte[]> newSplittedFile = new ArrayList<>();
            for(int i = startOffset, j = 0; i < endOffset; i++, j++){
                newSplittedFile.add(i, chunks.get(j));
            }
            FS_Node.splittedFiles.put(file, newSplittedFile);

            FS_Node.addNewFile(file);
        }
    }
    public static void pedido(String file, DatagramPacket sender, int startOffset, int endOffset){
        try{
            DatagramSocket socketAux = new DatagramSocket();
            byte[] data = new byte[(endOffset - startOffset) * Utils.BLOCK_SIZE + Utils.MARGEM_ERRO];
            DatagramPacket reciever = new DatagramPacket(data, data.length);

            socketAux.send(sender);

            socketAux.receive(reciever);

            handleResponse(file, reciever, startOffset, endOffset);

            socketAux.close();
        }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
    }

    public static void preparePedido(String file, InetSocketAddress nodoAtual, int startOffset, int endOffset){
        String data = new String(file + " " + startOffset + " " + endOffset);
        try{
            byte[] bytes = Utils.serializeString(data);

            DatagramPacket sender = new DatagramPacket(bytes, bytes.length, nodoAtual.getAddress(), nodoAtual.getPort());

            pedido(file, sender, startOffset, endOffset);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void selectNodes(String file, List<InetSocketAddress> nodosDisponveis, int chunks){
        int chunkAtual = 0;

        int chunkPorNodo = chunks / nodosDisponveis.size();
        int missingChunks = chunks - (chunkPorNodo * nodosDisponveis.size());

        for(InetSocketAddress address : nodosDisponveis){
            preparePedido(file, address, chunkAtual, (chunkAtual = chunkAtual + chunkPorNodo));
        }
    }

    static class NewRunnable implements Runnable{
        public void run(){

        }
    }
}
