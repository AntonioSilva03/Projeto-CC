package tracker;

import java.io.IOException;
import java.util.Scanner;

public class Interact implements Runnable{
    private static Scanner s;
    private Manager manager;
    public Interact(Manager manager){
        s = new Scanner(System.in);
        this.manager = manager;
    }

    private void closeall(){
        this.manager.nodes.forEach((address, node) -> {
            try{
                node.getConnection().quit();
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        });
        this.manager.nodes.clear();
    }

    public void run(){
        int input = s.nextInt();
        if(input == 0){
            closeall();
            FS_Tracker.state = false;
            try{
                FS_Tracker.serverSocket.close();
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
