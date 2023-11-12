package tracker;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private HashMap<InetSocketAddress, Node> nodes;

    public Manager(){
        nodes = new HashMap<>();
    }

    public void registerNode(InetSocketAddress address, String[] request){
        List<String> files = new ArrayList<>(Arrays.asList(request));
        files.remove(0);
        nodes.put(address, new Node(address, files));
    }

    public void removeNode(InetSocketAddress address){
        nodes.remove(address);
    }
}
