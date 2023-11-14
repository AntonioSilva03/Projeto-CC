package tracker;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Manager {
    public HashMap<InetSocketAddress, Node> nodes;

    public Manager(){
        nodes = new HashMap<>();
    }

    public void registerNode(InetSocketAddress address, String[] request, Handler connection){
        List<String> files = new ArrayList<>(Arrays.asList(request));
        nodes.put(address, new Node(address, files, connection));
    }

    public void removeNode(InetSocketAddress address){
        nodes.remove(address);
    }
}
