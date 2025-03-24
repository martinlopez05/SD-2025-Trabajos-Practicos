package unlu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

public class Nodo {
    private int puertoLocal;
    private String ipNodoD;
    private int puertoNodoD;

    public Nodo(int puertoLocal, String ipNodoD, int puertoNodoD) {
        this.puertoLocal = puertoLocal;
        this.ipNodoD = ipNodoD;
        this.puertoNodoD = puertoNodoD;
    }

    public void registerInD() {
        try {
            // Construir JSON correctamente
            JSONObject json = new JSONObject();
            json.put("ip", InetAddress.getLocalHost().getHostAddress());
            json.put("puerto", String.valueOf(puertoLocal)); // Convertimos el puerto a String

            System.out.println("[Nodo C] Enviando JSON a Nodo D: " + json.toString());

            // Enviar JSON al nodo D
            Socket socket = new Socket(ipNodoD, puertoNodoD);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(json.toString());

            // Recibir respuesta
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            System.out.println("[Nodo C] Respuesta de Nodo D: " + response);

            // Cerrar conexión con Nodo D (pero no el nodo C)
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("[Nodo C] Error registrando en D: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void startListening() {
        try (ServerSocket serverSocket = new ServerSocket(puertoLocal)) {
            System.out.println("[Nodo C] Nodo escuchando en el puerto " + puertoLocal);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Nodo C] Nueva conexión recibida desde: " + clientSocket.getInetAddress());

                // Leer mensaje recibido
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = in.readLine();
                System.out.println("[Nodo C] Mensaje recibido: " + message);

                // Responder
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Mensaje recibido por Nodo C");

                in.close();
                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[Nodo C] Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Uso: java Nodo <Puerto local> <IP Nodo D> <Puerto Nodo D>");
            return;
        }

        int puertoLocal = Integer.parseInt(args[0]);
        String ipNodoD = args[1];
        int puertoNodoD = Integer.parseInt(args[2]);

        Nodo nodo = new Nodo(puertoLocal, ipNodoD, puertoNodoD);
        nodo.registerInD();
        nodo.startListening();  // Mantener Nodo C en ejecución
    }
}
