package node;

import java.util.Scanner;
/**
 * Classe que trata de toda a interface com o utilizador.
 */
public class UI {
    private static Scanner s = new Scanner(System.in);
    /**
     * Função que imprime o menu principal e recolhe a opção selecionada.
     * @return Inteiro correspondente à opção selecionada.
     */
    public static int menuPrincipal(){
        s.reset();
        System.out.println("----------Peer-to-peer service----------\n");
        System.out.println("1. Download");
        System.out.println("2. Ficheiros partilhados");
        System.out.println("3. Apagar ficheiro");
        System.out.println("4. Sair\n");
        return s.nextInt();
    }

    /**
     * Função que imprime o menu de downliad e recolhe o nome do ficheiro pretendido.
     * @return Nome do ficheiro pretendido para download.
     */
    public static String menuDownload(){
        s.nextLine();
        System.out.println("Que ficheiro deseja baixar?\n");
        return s.nextLine();
    }

    public static String menuApagar(){
        s.nextLine();
        System.out.println("Que ficheiro deseja apagar");
        return s.nextLine();
    }
}