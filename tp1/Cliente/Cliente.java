package tp1.Cliente;

import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("server", 12345);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            output.println("Hola, Servidor!");
            String response = input.readLine();
            System.out.println("Servidor responde: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
