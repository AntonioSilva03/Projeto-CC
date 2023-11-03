package tracker;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class Manager {
    private HashMap<InetSocketAddress, Node> nodes;

    public Manager(){
        nodes = new HashMap<>();
    }

    public void registerNode(InetSocketAddress address){
        nodes.put(address, new Node(address));
    }

    public void removeNode(InetSocketAddress address){
        nodes.remove(address);
    }
}
