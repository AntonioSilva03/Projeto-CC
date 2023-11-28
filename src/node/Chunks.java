package node;

import utils.*;

import java.util.HashMap;
import java.io.File;

public class Chunks {
    public static HashMap<String, Integer> getChunks(String filepath, File sharedFiles){
        HashMap<String, Integer> chunks = new HashMap<>();

        String[] files = sharedFiles.list();
        for(String f: files){
            chunks.put(f, calculateChunks(filepath, f));
        }
        return chunks;
    }

    public static int calculateChunks(String filepath, String f){
        return (int)(new File(filepath + "/" + f).length() / Utils.BLOCK_SIZE) + 1; // Um bloco extra para casos em que sobram bytes que nao formam um bloco completo.
    }
}
