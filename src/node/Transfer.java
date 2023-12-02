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
/**
 * Classe que trata de toda a lógica por trás do pedido e recolha da transferência do ficheiro pretendido.
 */
public class Transfer {
    /**
     * Função que trata de lidar com a resposta do cliente a quem pediu um ficheiro ou blocos de um ficheiro
     * @param file Ficheiro que foi pedido
     * @param response Pacote recebido
     * @param startOffset Primeiro bloco pedido
     * @param endOffset Último bloco pedido
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static void handleResponse(String file, DatagramPacket response, int startOffset, int endOffset) throws ClassNotFoundException, IOException{
        byte[] recieved = response.getData();
        List<byte[]> chunks = Utils.deserializeListBytes(recieved);

        synchronized (FS_Node.splittedFiles) {
            if (FS_Node.splittedFiles.containsKey(file) || startOffset > 0) {
                List<byte[]> splittedFile = FS_Node.splittedFiles.computeIfAbsent(file, k -> new ArrayList<>());
    
                // Esperar pelos blocos anteriores
                while (splittedFile.size() < startOffset) {
                    try {
                        FS_Node.splittedFiles.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
    
                // Adicionar os blocos ao ficheiro
                for (int i = startOffset, j = 0; i < endOffset; i++, j++) {
                    splittedFile.add(i, chunks.get(j));
                }
    
                FS_Node.updateFile(file);
            } else {
                // Criar um novo ficheiro
                List<byte[]> newSplittedFile = new ArrayList<>();
                for (int i = startOffset, j = 0; i < endOffset; i++, j++) {
                    newSplittedFile.add(i, chunks.get(j));
                }
                FS_Node.splittedFiles.put(file, newSplittedFile);
    
                FS_Node.addNewFile(file);
            }
    
            // Notificar outras threads que estavam à espera
            FS_Node.splittedFiles.notifyAll();
        }
    }
    /**
     * Função que trata de efetuar o pedido de blocos a um cliente e que também recebe a resposta.
     * @param file Ficheiro pretendido
     * @param sender Pacote com o pedido de blocos
     * @param startOffset Primeiro bloco pedido
     * @param endOffset Último bloco pedido
     */
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

    /**
     * Função que prepara o pacote de pedido de blocos
     * @param file Ficheiro pedido
     * @param nodoAtual Endereço do cliente ao qual se pretende pedir blocos
     * @param startOffset Primeiro bloco pedido
     * @param endOffset Último bloco pedido
     */
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
    /**
     * Função que seleciona os blocos que serão pedidos a cada um dos nodos
     * @param file Ficheiro pedido
     * @param nodosDisponveis Todos os clientes disponíveis para partilhar o ficheiro pedido
     * @param chunks Quantidade de blocos em que o servidor está dividido
     */
    public static void selectNodes(String file, List<InetSocketAddress> nodosDisponveis, int chunks){
        int chunkAtual = 0;

        int chunkPorNodo = chunks / nodosDisponveis.size();
        int missingChunks = chunks - (chunkPorNodo * nodosDisponveis.size());

        for(InetSocketAddress address : nodosDisponveis){
            Thread t = new Thread(new NewRunnable(file, address, chunkAtual, (chunkAtual = chunkAtual + chunkPorNodo)));
            t.start();
        }
        int i = 0;
        while(missingChunks > 0){
            if(missingChunks >= chunkPorNodo){
                Thread t = new Thread(new NewRunnable(file, nodosDisponveis.get(i), chunkAtual, (chunkAtual = chunkAtual + chunkPorNodo)));
                t.start();
            }
            else{
                Thread t = new Thread(new NewRunnable(file, nodosDisponveis.get(i), chunkAtual, (chunkAtual = chunkAtual + missingChunks)));
                t.start();
            }
            i++;
            missingChunks -= chunkPorNodo;
        }
    }

    /**
     * Classe auxíliar para guardar informações que as várias threads precisam para efetuarem pedidos em paralelo.
     */
    static class NewRunnable implements Runnable{
        private String file;
        private InetSocketAddress address;
        private int chunkAtual;
        private int chunkFinal;
        public NewRunnable(String file, InetSocketAddress address, int chunkAtual, int chunkFinal){
            this.file = file;
            this.address = address;
            this.chunkAtual = chunkAtual;
            this.chunkFinal = chunkFinal;
        }
        public void run(){
            preparePedido(file, address, chunkAtual, chunkFinal);
        }
    }
}
