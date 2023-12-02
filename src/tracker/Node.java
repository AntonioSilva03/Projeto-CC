package tracker;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;

/**
 * Classe que guarda as informações de um cliente.
 */
public class Node {
    private InetSocketAddress address;
    private HashMap<String, Integer> files;
    private Handler connection;

    /**
     * Função que inicializa um cliente.
     * @param address Endereço do cliente.
     * @param files Ficheiros disponibilizados pelo cliente
     * @param connection Classe que recebe os pedidos do cliente
     */
    public Node(InetSocketAddress address, HashMap<String, Integer> files, Handler connection){
        this.address = address;
        this.files = files;
        this.connection = connection;
    }

    /**
     * Função que retorna a classe que recebe os pedidos do cliente
     * @return
     */
    public Handler getConnection(){
        return this.connection;
    }

    /**
     * Classe que retorna os ficheiros que o cliente disponibilizou
     * @return Mapa que associa um ficheiro à quantidade de chunks dividido
     */
    public HashMap<String, Integer> getFiles(){
        return this.files;
    }

    /**
     * Função que retorna a quantidade de chunks de um ficheiro.
     * @param file Ficheiro pedido
     * @return Quantidade de chunks de um ficheiro.
     */
    public int getChunk(String file){
        return this.files.get(file);
    }

    public void setFiles(HashMap<String, Integer> newFiles){
        this.files = newFiles;
    }
}
