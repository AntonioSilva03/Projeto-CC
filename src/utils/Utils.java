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

public class Utils {
    public static final int DEFAULT_PORT = 9090;
    public static final int BLOCK_SIZE = 5000; // Tamanho em bytes
    public static final int MARGEM_ERRO = 500; // Margem de erro para bytes das próprias estruturas

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

    public static byte[] serializeString(String list) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(list);
        byte[] serializedData = bos.toByteArray();
        return serializedData;
    }

    public static String deserializeString(byte[] data) throws ClassNotFoundException, IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream in = new ObjectInputStream(bis);

        String addresses = (String) in.readObject();

        return addresses;
    }

    public static byte[] serializeListBytes(List<byte[]> list) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(list);
        byte[] serializedData = bos.toByteArray();
        return serializedData;
    }

    @SuppressWarnings("unchecked")
    public static List<byte[]> deserializeListBytes(byte[] data) throws ClassNotFoundException, IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = new ObjectInputStream(bis);

        List<byte[]> addresses = (List<byte[]>) in.readObject();

        return addresses;
    }

    public static byte[] serializeList(List<InetSocketAddress> list) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(list);
        byte[] serializedData = bos.toByteArray();
        return serializedData;
    }

    @SuppressWarnings("unchecked")
    public static List<InetSocketAddress> deserializeList(byte[] data) throws ClassNotFoundException, IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = new ObjectInputStream(bis);

        List<InetSocketAddress> addresses = (List<InetSocketAddress>) in.readObject();

        return addresses;
    }

    public static byte[] serializeMap(Map<String, Integer> map) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(map);
        byte[] serializedData = bos.toByteArray();
        return serializedData;
    }

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
