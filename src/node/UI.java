package node;

import java.util.Scanner;

public class UI {
    private static Scanner s = new Scanner(System.in);
    public static int menuPrincipal(){
        System.out.println("----------Peer-to-peer service----------\n");
        System.out.println("1. Download");
        System.out.println("2. Ficheiros partilhados");
        System.out.println("3. Sair\n");
        return s.nextInt();
    }
}