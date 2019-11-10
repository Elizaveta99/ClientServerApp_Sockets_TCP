package Model.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClientThread {

    public static final int PORT = 8080;
    private static PrintStream os;
    private static BufferedReader is;
    private static Socket currentClientSocket;

    public static void main(String[] args) {
        try {
            // ��������� ���������� � ��������
            currentClientSocket = new Socket(InetAddress.getLocalHost(), PORT);// "localhost" // ?? port ?
            String clientName = currentClientSocket.getInetAddress().getHostName();
            os = new PrintStream(currentClientSocket.getOutputStream());
            is = new BufferedReader(new InputStreamReader(currentClientSocket.getInputStream()));

            //ps.println(clientName + " is waiting for message..");
            System.out.println(clientName + " is waiting for message from server..");

            // ��� � ��������� ������?

            String msg;
            try {
                while (true) {
                    msg = is.readLine(); // ���� ��������� � �������
                    if (msg.equals("stop")) {  // ??
                        disconnect();
                        break; // ������� �� ����� ���� ������ "stop"
                    }
                    System.out.println(msg); // ����� ��������� � ������� �� �������
                }
            } catch (IOException e) {
                disconnect();
            }

            Thread.sleep(1000); // ??

//            ps.println("");
//            for (int i = 1; i <= 10; i++) {
//                ps.println("PING");
//                System.out.println(br.readLine());
//                Thread.sleep(1000);
//            }

            currentClientSocket.close();

        } catch (UnknownHostException e) {
            // ���� �� ������� ����������� � ��������
            System.out.println("����� ����������");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("������ I/� ������");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("������ ������ ����������");
            e.printStackTrace();
        } finally {
            disconnect(); // ??
        }
    }

    private static void disconnect() {
        try {
            //System.out.println(clientAddress.getHostName() + " disconnected");
            os.close();
            is.close();
            currentClientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

