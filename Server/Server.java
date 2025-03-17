package Server;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor esperando conexión...");

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                    String message = input.readLine();
                    System.out.println("Cliente dice: " + message);
                    output.println("Hola, Cliente!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
