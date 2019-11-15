package Model.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Class-copy of server
 */
class ServerThread extends Thread {

    public static LinkedList<ServerThread> serverList = new LinkedList<>();

    private PrintStream os;//передача
    private BufferedReader is;//чтение
    private InetAddress clientAddress;//адрес клиента

    private static BufferedReader reader; // с консоли сообщение, которое посылает сервер всем клиентам

    /**
     * @param clientSocket - new client
     * @throws IOException
     * os - output for this socket
     * is - input for this socket
     */
    public ServerThread(Socket clientSocket) throws IOException {
        os = new PrintStream(clientSocket.getOutputStream());
        is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        clientAddress = clientSocket.getInetAddress();
    }
    public void run() {
            while (true) {
                NetServerThread.countRuns++;
                reader = new BufferedReader(new InputStreamReader(System.in));
                String message = "";
                try {
                    message = enterMessage(NetServerThread.countRuns);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (NetServerThread.countRuns >= 10)
                    message = "stop";
                // отослать сообщение с консоли от сервера всем клиентам
                for (ServerThread st : serverList) {
                    st.sendMessageToClient(message);
                }
                if (message.equals("stop"))
                    break;
            }

    }

    private void sendMessageToClient(String msg) {
        os.println(msg);
    }

    private String enterMessage(int number) {
        return "message" + number;
//        System.out.println("Enter message for clients: ");
//
//        try {
//            String msg = reader.readLine();
//            if (msg.equals("stop"))
//                //os.println(msg);
//                return msg;
//            else
//                //os.println("Hello, " + clientAddress.getHostName() + ". New message for you: " + msg);
//            return "Hello, " + clientAddress.getHostName() + ". New message for you: " + msg;
//            //os.println("Hello, " + msg + "\n");
//            //os.flush();
//        } catch (IOException ignored) {
//            System.out.println("exception");
//            return "stop1";
//        }
    }
}

