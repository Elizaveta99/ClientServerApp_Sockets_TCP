package Model.client;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Main Client class
 */
public class NetClientThread {

    public static final int PORT = 8080;
    private static PrintStream os;
    private static BufferedReader is;
    private static Socket currentClientSocket;
    public static String clientName;

    static final Logger netClientThreadLogger = LogManager.getLogger(NetClientThread.class);

    /**
     * Client is waiting message from server
     * @param args
     */
    public static void main(String[] args) {
        try {
            // установка соединения с сервером
            currentClientSocket = new Socket(InetAddress.getLocalHost(), PORT);
            clientName = currentClientSocket.getInetAddress().getHostName();
            os = new PrintStream(currentClientSocket.getOutputStream());
            is = new BufferedReader(new InputStreamReader(currentClientSocket.getInputStream()));

            netClientThreadLogger.info("Waiting for message from server..");

            String msg;
            try {
                while (true) {
                    msg = is.readLine(); // ждем сообщения с сервера
                    if (msg.equals("stop")) {
                        break; // выходим из цикла если пришло "stop"
                    }
                    System.out.println(msg); // пишем сообщение с сервера на консоль
                }
            } catch (IOException e) {
                disconnect();
            }

            currentClientSocket.close();

        } catch (UnknownHostException e) {
            netClientThreadLogger.error("Address is not available");
        } catch (IOException e) {
            netClientThreadLogger.error("Error in I/O thread");
        } finally {
            disconnect();
        }
    }

    /**
     * method, which disconnect client from server
     */
    private static void disconnect() {
        try {
            netClientThreadLogger.info("This client was disconnected");
            os.close();
            is.close();
            currentClientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

