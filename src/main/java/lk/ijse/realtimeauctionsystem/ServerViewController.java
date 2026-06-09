package lk.ijse.realtimeauctionsystem;

import javafx.fxml.Initializable;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;


public class ServerViewController  {
    static ServerSocket serverSocket;
    static List<Socket> clientList = new ArrayList<>();
    static String msg;
    public static void main(String[] args) {

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(6000);
                System.out.println("Auction Server Started -Port 6000");

                handleItem();
                while (true) {
                    Socket socket = serverSocket.accept();
                    clientList.add(socket);

                    System.out.println("Client Connected -");
                    new Thread(() -> handleClient(socket)).start();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private static void handleItem(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the item you want to bid ->");
        String item = sc.nextLine();
        System.out.println("Please enter the item price ->");
        double price = sc.nextDouble();


        DataInputStream dis = new DataInputStream(System.in);
        String message = "";
        try {
            message = dis.readUTF();
            broadcast(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String message;
            while (true) {
                message = in.readUTF();
                broadcast(message);
            }

        } catch (IOException e) {
            System.out.println("[Auction close] ");
       }
    }

  private static void broadcast(String message) {
        for (Socket socket : clientList) {
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(message);
                out.flush();

           } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

