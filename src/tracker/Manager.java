package tracker;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import utils.Utils;

/**
 * Classe que guarda todos os dados da rede.
 * Inclui os dados dos nodos que estão conectados e algumas operações sobre eles.
 */
public class Manager {
    public HashMap<InetSocketAddress, Node> nodes;

    /**
     * Função que inicializa a base de dados.
     */
    public Manager(){
        nodes = new HashMap<>();
    }

    /**
     * Função que regista um nodo novo na base de dados.
     * @param address Endereço do nodo a conectar.
     * @param request Pacote enviado pelo nodo com as informações necessárias para registo.
     * @param connection Objeto que lida com os pedidos do cliente a conectar.
     */
    public void registerNode(InetSocketAddress address, byte[] request, Handler connection){
        HashMap<String, Integer> files = new HashMap<>(Utils.deserializeMap(request));
        nodes.put(address, new Node(address, files, connection));
    }

    /**
     * Função que remove um nodo da base de dados
     * @param address Endereço do nodo a remover
     */
    public void removeNode(InetSocketAddress address){
        nodes.remove(address);
    }

    /**
     * Função que seleciona os nodos que possuem um ficheiro.
     * @param file Ficheiro pedido.
     * @return Lista com os endereços dos clientes.
     */
    public List<InetSocketAddress> getNodesFile(String file){
        List<InetSocketAddress> disponiveis = new ArrayList<>();
        this.nodes.forEach((address, node) -> {
            if(node.getFiles().containsKey(file)){
                disponiveis.add(address);
            }
        });
        return disponiveis;
    }

    /**
     * Função que retorna a quantidade de chunks que um ficheiro está dividido.
     * @param address Endereço do cliente.
     * @param file Ficheiro pedido.
     * @return Quantidade de chunks.
     */
    public int getChunks(InetSocketAddress address, String file){
        return nodes.get(address).getChunk(file);
    }

    public void updateNode(InetSocketAddress address, byte[] request){
        HashMap<String, Integer> newFiles = new HashMap<>(Utils.deserializeMap(request));
        Node alterado = nodes.get(address);
        alterado.setFiles(newFiles);
        nodes.put(address, alterado);
    }
}
