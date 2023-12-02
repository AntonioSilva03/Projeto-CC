package node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.InetSocketAddress;

import utils.*;
/**
 * Classe principal do programa-cliente.
 */
public class FS_Node {
    private static Socket tcpSocket;
    private static DatagramSocket udpSocket;
    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static String filepath;
    private static File sharedFiles;
    private static HashMap<String, Integer> chunkFiles;
    public static HashMap<String, List<byte[]>> splittedFiles;
    /**
     * Função que divide um ficheiro nos seus respetivos blocos e guarda-os numa lista.
     * @param file Ficheiro que se pretende dividir.
     * @param chunks Número de chunks que se pretende dividir o ficheiro.
     * @return Lista com parcelas de um ficheiro representadas em bytes.
     */

    public static List<byte[]> getFileOutputStream(File file, int chunks){
        List<byte[]> streamList = new ArrayList<>();

        try{
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[Utils.BLOCK_SIZE];

            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] block = new byte[bytesRead]; // Ajuste para considerar o número real de bytes lidos
                System.arraycopy(buffer, 0, block, 0, bytesRead);
                streamList.add(block);
            }
            fis.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return streamList;
    }

    /**
     * Função que divide os ficheiros disponíveis para seeding em blocos.
     */
    public static void splitFiles(){
        splittedFiles = new HashMap<>();

        chunkFiles.forEach((stringFile, chunks) -> {
            File file = new File(filepath + "/" + stringFile);

            splittedFiles.put(stringFile, getFileOutputStream(file, chunks));
        });
    }

    public static void updateServer(HashMap<String, Integer> chunks) throws IOException{
        dos.writeUTF("UPDATE");
        byte[] serializedSharedFiles = Utils.serializeMap(chunkFiles);
        dos.writeInt(serializedSharedFiles.length);
        dos.write(serializedSharedFiles);
        dos.flush();
    }
    /**
     * Função que efetua o registo do cliente e comunica ao servidor a sua presença bem como os ficheiros que disponibiliza
     * @throws IOException
     */
    public static void register() throws IOException{
        try{
            tcpSocket = new Socket("10.0.0.10", Utils.DEFAULT_PORT);
            try{
                udpSocket = new DatagramSocket(Utils.DEFAULT_PORT);
            }
            catch(SocketException e){
                System.out.println("Impossível iniciar conexão para seed");
                tcpSocket.close();
                System.exit(0);
            }
            dis = new DataInputStream(tcpSocket.getInputStream());
            dos = new DataOutputStream(tcpSocket.getOutputStream());
        }
        catch(IOException e){
            System.out.println("Impossível conectar-se ao servidor");
            System.exit(0);
        }

        dos.writeInt(udpSocket.getLocalPort());
        dos.flush();

        sharedFiles = new File(filepath);

        chunkFiles = Chunks.getChunks(filepath, sharedFiles);

        byte[] serializedSharedFiles = Utils.serializeMap(chunkFiles);
        dos.writeInt(serializedSharedFiles.length);
        dos.write(serializedSharedFiles);
        dos.flush();
    }
    /**
     * Função que trata da desconexão do cliente, fecho dos sockets e dos processos auxiliares.
     */
    public static void disconnect(){
        try{
            SeedingServer.setState(false);
            tcpSocket.close();
            udpSocket.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Função que comunica ao servidor que o cliente vai se desconectar.
     */
    public static void quit(){
        try{
            dos.writeUTF("QUIT");
            dos.flush();
            disconnect();
        }
        catch(IOException e){ // Caso em que o servidor fechou enquanto o cliente ainda se encontra conectado, logo a comunicação não é possível.
            disconnect();
        }
    }
    /**
     * Função que pede ao servidor os nodos disponíveis para download do ficheiro pedido.
     * @param pedido Ficheiro pedido.
     */
    public static void requestDownload(String pedido){
        if (chunkFiles.containsKey(pedido)) {
            return;
        }
        List<InetSocketAddress> nodosDisponveis;
        try{
            dos.writeUTF("REQUEST " + pedido);
            dos.flush();

            int length = dis.readInt();
            byte[] data = new byte[length];
            dis.readFully(data);
            nodosDisponveis = Utils.deserializeList(data);

            if(nodosDisponveis.size() > 0) {
                int chunks = dis.readInt();
                Transfer.selectNodes(pedido, nodosDisponveis, chunks);

                chunkFiles.put(pedido, chunks);
                updateServer(chunkFiles);
            }
            else return;
            System.out.println("Ficheiro transferido com sucesso\n");
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    /**
     * Função principal do programa
     * @param args Obrigatoriamente deve incluir o caminho para os ficheiros disponíveis para partilha.
     */
    public static void main(String[] args) {
        filepath = args[0];
        try{
            register();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        splitFiles();

        Thread server = new Thread(new SeedingServer(udpSocket));
        server.start();
        
        boolean end = false;
        while(!end){
            if(!Utils.checkConnection(dos)){
                disconnect();
                break;
            }
            int op = UI.menuPrincipal();
            if (op == 1) {
                String pedido = UI.menuDownload();
                requestDownload(pedido);
            }
            else if(op == 2){
                System.out.println("Ficheiros partilhados: " + String.join(" ", sharedFiles.list()) + "\n");
            }
            else if(op == 3){
                String ficheiro = UI.menuApagar();
                apagaFile(ficheiro);
            }
            else if(op == 4){
                end = true;
                quit();
                System.out.println("Desconectado com sucesso");
                return;
            }
        }
        System.out.println("Conexão ao servidor perdida");
    }
    /**
     * Função que fornece os ficheiros que está a partilhar
     * @return Devolve uma referência para todos os ficheiros que estão a ser partilhados
     */
    public static File getFiles(){
        return sharedFiles;
    }
    /**
     * Função que fornece o caminho para a pasta de partilha
     * @return Caminho para a pasta de partilha
     */
    public static String getFilepath(){
        return filepath;
    }
    /**
     * Função que devolve o socket UDP principal do cliente.
     * @return Socket UDP principal do cliente.
     */
    public static DatagramSocket getUdpSocket(){
        return udpSocket;
    }
    /**
     * Função que adiciona um ficheiro novo à pasta de partilha de ficheiros.
     * @param file Nome do ficheiro novo para partilha.
     */
    public static void addNewFile(String file){
        try{
            FileOutputStream novoFile = new FileOutputStream(filepath + "/" + file);

            List<byte[]> bytesFicheiro = splittedFiles.get(file);
            for(byte[] bloco: bytesFicheiro){
                novoFile.write(bloco);
            }
            novoFile.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Função que atualiza um ficheiro já existente com blocos novos.
     * @param file Nome do ficheiro a ser atualizado.
     */
    public static void updateFile(String file){
        try{
            FileOutputStream updateFile = new FileOutputStream(filepath + "/" + file);

            List<byte[]> bytesFicheiro = splittedFiles.get(file);
            for(byte[] bloco: bytesFicheiro){
                updateFile.write(bloco);
            }
            updateFile.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void apagaFile(String ficheiro){
        if(!splittedFiles.containsKey(ficheiro)){
            System.out.println("Ficheiro não existe na pasta partilhada");
        }
        else{
            try{
                splittedFiles.remove(ficheiro);
                chunkFiles.remove(ficheiro);
                Files.delete(Paths.get(filepath + "/" + ficheiro));
                sharedFiles = new File(filepath);
                updateServer(chunkFiles);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
