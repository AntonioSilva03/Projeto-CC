package tracker;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import utils.Utils;

public class Manager {
    public HashMap<InetSocketAddress, Node> nodes;

    public Manager(){
        nodes = new HashMap<>();
    }

    public void registerNode(InetSocketAddress address, byte[] request, Handler connection){
        HashMap<String, Integer> files = new HashMap<>(Utils.deserializeMap(request));
        nodes.put(address, new Node(address, files, connection));
    }

    public void removeNode(InetSocketAddress address){
        nodes.remove(address);
    }

    public List<InetSocketAddress> getNodesFile(String file){
        List<InetSocketAddress> disponiveis = new ArrayList<>();
        this.nodes.forEach((address, node) -> {
            if(node.getFiles().containsKey(file)){
                disponiveis.add(address);
            }
        });
        return disponiveis;
    }

    public int getChunks(InetSocketAddress address, String file){
        return nodes.get(address).getChunk(file);
    }
}
