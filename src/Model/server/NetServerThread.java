package Model.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Main Server class
 * @author Elizaveta Rudko
 * @version 1.1.0
 */
public class NetServerThread {

    public static LinkedList<ServerThread> serverList = new LinkedList<>(); // ������ ���� ����� - �����������
    // �������, ��������� ������ ������ �������

    public static final int PORT = 8080; // ??

    public static void main(String[] args) throws IOException {
        try {
            ServerSocket mainServer = new ServerSocket(PORT); // ?? port ?
            System.out.println("Server initialized");
            while (true) {
                //�������� �������
                Socket clientSocket = mainServer.accept();

                String clientName = clientSocket.getInetAddress().getHostName();
                System.out.println(clientName + " connected");

                //addClient(clientName); // ??

                //�������� ���������� ������(����� �������) ��� ������ ������� � ������������� ��������, ���������� � ������
                try {
                    ServerThread server = new ServerThread(clientSocket);
                    serverList.add(server);
                    server.start(); //������ ������
                } catch (IOException e)
                {
                    clientSocket.close();
                }


            }
        } catch (IOException e) {
            System.err.println(e);
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
