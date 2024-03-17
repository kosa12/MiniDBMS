package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.io.File;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)
        ) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from client: " + message);

                //message feldolgozasa

                String[] parts = message.split(" ");

                if (Objects.equals(parts[0], "create") && Objects.equals(parts[1], "database") && parts[2] != null){
                    File folder = new File("src/databases/" + parts[2]);

                    if (folder.mkdirs()) {
                        System.out.println("Database succesfully created: " + folder.getAbsolutePath());
                    } else {
                        System.out.println("Could not create database <" + parts[2] +">.");
                    }
                }
                else if (Objects.equals(parts[0], "drop") && Objects.equals(parts[1], "database") && parts[2] != null){
                    File folder = new File("src/databases/" + parts[2]);

                    if(folder.delete()){
                        System.out.println("Database <" + parts[2] + "> dropped.");
                    }
                    else {
                        System.out.println("Could not drop database <" + parts[2] + ">." );
                    }
                }


                //

            }



            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}