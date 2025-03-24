package unlu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9000)) {
            System.out.println("Servidor esperando conexión...");

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("Cliente conectado");

                    // Enviar saludo al cliente
                    output.println("¡Hola, Cliente!");

                    
                    String message;
                    while ((message = input.readLine()) != null) {  
                        System.out.println("Cliente dice: " + message);
                    }

                    
                    System.out.println("Cliente desconectado.");

                } catch (IOException e) {
                    System.out.println("Error de comunicación con el cliente.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  
        }
    }
}