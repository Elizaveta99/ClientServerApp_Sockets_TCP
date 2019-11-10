package Model.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

class ServerThread extends Thread {
    private PrintStream os;//��������
    private BufferedReader is;//������
    private InetAddress clientAddress;//����� �������

    private static BufferedReader reader; // � ������� ���������, ������� �������� ������ ���� ��������

    public ServerThread(Socket clientSocket) throws IOException {
        os = new PrintStream(clientSocket.getOutputStream());
        is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        clientAddress = clientSocket.getInetAddress();
    }
    public void run() {
        int i = 0;
        String clientMessage;
        try {
            clientMessage = is.readLine();
            //System.out.println(clientMessage); // ??
            reader = new BufferedReader(new InputStreamReader(System.in));
            String message = enterMessage();
            // �������� ��������� � ������� �� ������� ���� �������
            for (ServerThread st: NetServerThread.serverList) {
                st.sendMessageToClient(message);
            }
            //os.println("Hello, " + clientAddress.getHostName());

//            while ((str = is.readLine()) != null) {
//                if ("PING".equals(str))
//                    os.println("PONG "+ ++i);
//
//                System.out.println("PING-PONG" + i
//                        + " with " + clientAddress.getHostName());
//                if(!(str.equals(null))){System.out.println(str);
//                }
//            }

        } catch (IOException e) {
            //���� ������ �� ��������, ���������� � ��� ����������� ??
            System.out.println("Disconnect");
        } finally {
            disconnect(); //����������� ������
        }
    }

    public void disconnect() {
        try {
            System.out.println(clientAddress.getHostName() + " disconnected");
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }
    }

    private void sendMessageToClient(String msg) {
        os.println(msg);
    }

    private String enterMessage() {
        System.out.print("Enter message for clients: ");
        try {
            String msg = reader.readLine();
            if (msg.equals("stop"))
                //os.println(msg);
                return msg;
            else
                //os.println("Hello, " + clientAddress.getHostName() + ". New message for you: " + msg);
            return "Hello, " + clientAddress.getHostName() + ". New message for you: " + msg;
            //os.println("Hello, " + msg + "\n");
            //os.flush();
        } catch (IOException ignored) {
            return "stop";
        }
    }
}

