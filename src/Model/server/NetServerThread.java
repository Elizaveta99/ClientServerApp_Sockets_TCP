package Model.server;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Main Server class
 * @author Elizaveta Rudko
 * @version 1.1.0
 */
public class NetServerThread {

    public static final int PORT = 8080;

    static final Logger rootLogger = LogManager.getRootLogger();
    static final Logger netServerThreadLogger = LogManager.getLogger(NetServerThread.class);

    public static int countRuns = 0;

    /**
     * method, which initialized server, create list of connections with each new client
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        try {
            ServerSocket mainServer = new ServerSocket(PORT);
            netServerThreadLogger.info("Server initialized");
            while (true) {
                //ожидание клиента
                Socket clientSocket = mainServer.accept();
                String clientName = clientSocket.getInetAddress().getHostName();

                //создание отдельного потока(копии сервера) для обмена данными с соединившимся клиентом, добавление в список
                try {
                    ServerThread server = new ServerThread(clientSocket);
                    ServerThread.serverList.add(server);
                    server.start(); //запуск потока

                } catch (IOException e)
                {
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            netServerThreadLogger.error(e);
        }
    }

    public static void addClient(String clientName) throws IOException {
        File fout = new File("clientsList.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(clientName);
        bw.newLine();
        bw.close();
    }
}
