package tracker;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;

public class Node {
    private InetSocketAddress address;
    private HashMap<String, Integer> files;
    private Handler connection;

    public Node(InetSocketAddress address, HashMap<String, Integer> files, Handler connection){
        this.address = address;
        this.files = files;
        this.connection = connection;
    }

    public Handler getConnection(){
        return this.connection;
    }

    public HashMap<String, Integer> getFiles(){
        return this.files;
    }

    public int getChunk(String file){
        return this.files.get(file);
    }
}
