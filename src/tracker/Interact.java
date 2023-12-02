package tracker;

import java.io.IOException;
import java.util.Scanner;

/**
 * Classe de interação com o servidor
 * Esta classe observa separadamente por inputs para interação.
 * Usada principalmente para fechar o servidor de maneira limpa.
 */
public class Interact implements Runnable{
    private static Scanner s;
    private Manager manager;
    /**
     * Função que inicializa a classe de interação
     * @param manager
     */
    public Interact(Manager manager){
        s = new Scanner(System.in);
        this.manager = manager;
    }
    /**
     * Função que desconecta todos os nodos que estão conectados.
     */
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

    /**
     * Função que espera por inputs e altera o estado do servidor para encerramento.
     */
    public void run(){
        int input = s.nextInt();
        while(input != 0){
            input = s.nextInt();
        }
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
