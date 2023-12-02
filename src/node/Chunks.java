package node;

import utils.*;

import java.util.HashMap;
import java.io.File;

/**
 * Classe que trata dos cálculos das chunks para separação dos ficheiros
 */
public class Chunks {
    /**
     * Função que calcula a quantidade de chunks que cada ficheiro vai ser separado dependendo do seu tamanho.
     * @param filepath Caminho para a pasta de partilha
     * @param sharedFiles Referências para os ficheiros da pasta.
     * @return Quantidade de chunks que cada ficheiro tem que ser dividido associadas a cada ficheiro.
     */
    public static HashMap<String, Integer> getChunks(String filepath, File sharedFiles){
        HashMap<String, Integer> chunks = new HashMap<>();

        String[] files = sharedFiles.list();
        for(String f: files){
            chunks.put(f, calculateChunks(filepath, f));
        }
        return chunks;
    }

    /**
     * Função que calcula a quantidade de chunks que um ficheiro vai ser separado
     * @param filepath Caminho para a pasta de partilha
     * @param f Nome do ficheiro a ser calculado
     * @return Número de chunks que o ficheiro f tem que ser dividido.
     */
    public static int calculateChunks(String filepath, String f){
        return (int)(new File(filepath + "/" + f).length() / Utils.BLOCK_SIZE) + 1; // Um bloco extra para casos em que sobram bytes que nao formam um bloco completo.
    }
}
