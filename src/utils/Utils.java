package utils;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectInput;
import java.util.List;
import java.util.Map;
import java.net.InetSocketAddress;

/**
 * Classe auxiliar de funcionamento geral do programa
 */
public class Utils {
    public static final int DEFAULT_PORT = 9090; // Porta genérica que todos os sockets se conectam se possível.
    public static final int BLOCK_SIZE = 5000; // Tamanho em bytes de um bloco.
    public static final int MARGEM_ERRO = 500; // Margem de erro de espaço para bytes das próprias estruturas.

    /**
     * Função que envia pacotes periódicos ao servidor para verificar conexão.
     * @param dos Stream associada ao socket para escrita de dados.
     * @return Estado atual do servidor.
     */
    public static boolean checkConnection(DataOutputStream dos){
        try{
            dos.writeUTF("PING");
            dos.flush();
            return true;
        }
        catch(IOException e){
            return false;
        }
    }

    /**
     * Função que serializa uma string.
     * @param list String a ser serializada
     * @return Bytes que representam a string serializada
     * @throws IOException
     */
    public static byte[] serializeString(String list) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(list);
        byte[] serializedData = bos.toByteArray();
        return serializedData;
    }

    /**
     * Função que deserializa uma string
     * @param data Bytes que representam uma string
     * @return String reconstruída
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static String deserializeString(byte[] data) throws ClassNotFoundException, IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream in = new ObjectInputStream(bis);

        String addresses = (String) in.readObject();

        return addresses;
    }

    /**
     * Função que serializa uma lista de arrays de bytes (ficheiro dividido em blocos representados por bytes).
     * @param list Lista a ser serializada
     * @return Bytes que representam a lista serializada
     * @throws IOException
     */
    public static byte[] serializeListBytes(List<byte[]> list) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(list);
        byte[] serializedData = bos.toByteArray();
        return serializedData;
    }

    /**
     * Função que deserializa uma lista de arrays de bytes (ficheiro dividido em blocos representados por bytes).
     * @param data Bytes que representam uma lista de arrays de bytes
     * @return Lista de arrays de bytes reconstruida
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static List<byte[]> deserializeListBytes(byte[] data) throws ClassNotFoundException, IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = new ObjectInputStream(bis);

        List<byte[]> addresses = (List<byte[]>) in.readObject();

        return addresses;
    }

    /**
     * Função que serializa uma lista de endereços
     * @param list Lista a ser serializada
     * @return Bytes que representam a lista serializada
     * @throws IOException
     */
    public static byte[] serializeList(List<InetSocketAddress> list) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(list);
        byte[] serializedData = bos.toByteArray();
        return serializedData;
    }

    /**
     * Função que deserializa uma lista de endereços
     * @param data Bytes que representam uma lista de endereços
     * @return Lista reconstruida
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static List<InetSocketAddress> deserializeList(byte[] data) throws ClassNotFoundException, IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = new ObjectInputStream(bis);

        List<InetSocketAddress> addresses = (List<InetSocketAddress>) in.readObject();

        return addresses;
    }

    /**
     * Função que serializa um hashmap.
     * @param map Hashmap a ser serializado
     * @return Bytes que representam o hashmap
     * @throws IOException
     */
    public static byte[] serializeMap(Map<String, Integer> map) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(map);
        byte[] serializedData = bos.toByteArray();
        return serializedData;
    }

    /**
     * Função que deserializa um hashmap
     * @param data Bytes que representam o hashmap
     * @return Hashmap reconstruido.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Integer> deserializeMap(byte[] data){
        Map<String, Integer> map = null;
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);

            map = (Map<String, Integer>) in.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return map;
    }
}
