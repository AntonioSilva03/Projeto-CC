package utils;

import java.io.DataOutputStream;
import java.io.IOException;

public class Utils {
    public static final int DEFAULT_PORT = 9090;

    public static boolean checkConnection(DataOutputStream dos){
        try{
            dos.writeUTF("PING");
            dos.flush();
            return true;
        }
        catch(IOException e){
            return false;
        }
    }
}
