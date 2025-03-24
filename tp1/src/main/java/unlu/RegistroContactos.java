package unlu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class RegistroContactos {
    private int puerto;
    private Map<String, String> nodosRegistrados; // IP -> Puerto

    public RegistroContactos(int puerto) {
        this.puerto = puerto;
        this.nodosRegistrados = new HashMap<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("[Nodo D] Nodo D escuchando en el puerto " + puerto);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Nodo D] Nueva conexión desde: " + clientSocket.getInetAddress());

                // Leer datos de entrada
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String receivedData = in.readLine();
                System.out.println("[Nodo D] Recibido: " + receivedData);

                try {
                    // Parsear JSON recibido
                    JSONObject requestJson = new JSONObject(receivedData);

                    String ip = requestJson.getString("ip");
                    String puerto = requestJson.getString("puerto");

                    System.out.println("[Nodo D] Registrando nodo - IP: " + ip + ", Puerto: " + puerto);

                    // Guardar nodo en el registro
                    nodosRegistrados.put(ip, puerto);

                    // Enviar respuesta
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("status", "OK");
                    responseJson.put("message", "Nodo registrado correctamente");

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println(responseJson.toString());

                } catch (Exception e) {
                    System.err.println("[Nodo D] Error al procesar solicitud: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.err.println("[Nodo D] Error en servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java RegistroContactos <Puerto>");
            return;
        }

        int puerto = Integer.parseInt(args[0]);
        RegistroContactos nodoD = new RegistroContactos(puerto);
        nodoD.start();
    }
}
