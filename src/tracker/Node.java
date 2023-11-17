package tracker;

import java.net.InetSocketAddress;
import java.util.List;

public class Node {
    private InetSocketAddress address;
    private List<String> files;
    private Handler connection;

    public Node(InetSocketAddress address, List<String> files, Handler connection){
        this.address = address;
        this.files = files;
        this.connection = connection;
    }

    public Handler getConnection(){
        return this.connection;
    }

    public List<String> getFiles(){
        return this.files;
    }
}
