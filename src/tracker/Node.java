package tracker;

import java.net.InetSocketAddress;
import java.util.List;

public class Node {
    private InetSocketAddress address;
    private List<String> files;

    public Node(InetSocketAddress address, List<String> files){
        this.address = address;
        this.files = files;
    }
}
