import java.io.*;
import java.net.*;

public class Cliente {
    private static final String SERVER_HOST = "server";
    private static final int SERVER_PORT = 9000;
    private static final int MAX_RETRIES = 5;  // Máximo de intentos antes de abortar
    private static final int RETRY_DELAY = 5000; // Tiempo de espera en milisegundos

    public static void main(String[] args) {
        int retryCount = 0;  // Contador de intentos fallidos

        while (true) { // Bucle de reconexión
            try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                System.out.println("Conectado al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
                retryCount = 0; // Reiniciar contador si la conexión es exitosa

                // Bucle para mantener la conexión y enviar mensajes periódicamente
                while (true) {
                    output.println("Hola, Servidor!");

                    String response = input.readLine();
                    if (response == null) {
                        System.out.println("Servidor cerró la conexión inesperadamente.");
                        break; // Salir del bucle interno y reintentar conexión
                    }

                    System.out.println("Servidor responde: " + response);
                    Thread.sleep(5000); // Esperar 5 segundos antes de enviar otro mensaje
                }

            } catch (IOException e) {
                retryCount++;
                System.out.println("No se pudo conectar al servidor (" + retryCount + "/" + MAX_RETRIES + "). Reintentando en " + (RETRY_DELAY / 1000) + " segundos...");

                if (retryCount >= MAX_RETRIES) {
                    System.out.println("El servidor no está disponible. Intenta nuevamente más tarde.");
                    break; // Salir si se supera el número máximo de intentos
                }

                try {
                    Thread.sleep(RETRY_DELAY);
                } catch (InterruptedException ignored) {}
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

