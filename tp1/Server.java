import java.io.*;
import java.net.*;

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

                    // Leer el mensaje enviado por el cliente
                    String message = input.readLine();
                    System.out.println("Cliente dice: " + message);

                } catch (IOException e) {
                    System.out.println("Cliente desconectado o error de comunicación.");
                }
            }
        } catch (IOException e) {
        }
    }
}
