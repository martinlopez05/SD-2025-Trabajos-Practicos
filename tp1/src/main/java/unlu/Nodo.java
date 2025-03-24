package unlu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

public class Nodo {

    private final int listenPort;
    private final String peerHost;
    private final int peerPort;

    public Nodo(int listenPort, String peerHost, int peerPort) {
        this.listenPort = listenPort;
        this.peerHost = peerHost;
        this.peerPort = peerPort;
    }

    public void start() {
        Thread serverThread = new Thread(this::startServer);
        Thread clientThread = new Thread(this::startClient);

        serverThread.start();
        clientThread.start();
    }

    private void startServer() {
        try (ServerSocket servidorSocket = new ServerSocket(listenPort)) {
            System.out.println("Servidor escuchando en el puerto " + listenPort);
            
            while (true) {
                try (Socket clienteSocket = servidorSocket.accept();
                     BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                     PrintWriter salidaCliente = new PrintWriter(clienteSocket.getOutputStream(), true)) {
                    
                    System.out.println("Cliente conectado desde " + clienteSocket.getInetAddress());

                    // Enviar mensaje de bienvenida al cliente
                    JSONObject mensajeBienvenida = new JSONObject().put("contenido", "¡Hola, Cliente!");
                    salidaCliente.println(mensajeBienvenida.toString());

                    // Leer mensajes entrantes
                    String mensajeRecibido;
                    while ((mensajeRecibido = entradaCliente.readLine()) != null) {
                        try {
                            JSONObject mensajeJson = new JSONObject(mensajeRecibido);
                            System.out.println("Cliente dice: " + mensajeJson.getString("contenido"));
                        } catch (Exception e) {
                            System.err.println("Error procesando JSON: " + mensajeRecibido);
                        }
                    }
                    System.out.println("Cliente desconectado.");
                } catch (IOException e) {
                    System.err.println("Error de comunicación con el cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    private void startClient() {
        int intentos = 0;
        while (intentos < 10) {  
            try (Socket socket = new Socket(peerHost, peerPort);
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                System.out.println("Conectado a " + peerHost + ":" + peerPort);
                intentos = 0; 

                while (true) {
                    JSONObject mensajeJson = new JSONObject().put("contenido", "Hola, Servidor!");
                    output.println(mensajeJson.toString());

                    String responseJson = input.readLine();
                    if (responseJson == null) {
                        System.out.println("Servidor cerró la conexión inesperadamente.");
                        break;
                    }

                    JSONObject respuesta = new JSONObject(responseJson);
                    System.out.println("Servidor responde: " + respuesta.getString("contenido"));

                    Thread.sleep(5000);
                }
            } catch (IOException | InterruptedException e) {
                intentos++;
                System.err.println("No se pudo conectar a " + peerHost + ":" + peerPort + ". Reintentando (" + intentos + "/10)...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
            }
        }
        System.err.println("No se pudo conectar tras 10 intentos. Terminando cliente.");
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Uso: java Nodo <puerto escucha> <IP destino> <puerto destino>");
            return;
        }

        try {
            int listenPort = Integer.parseInt(args[0]);
            String peerHost = args[1];
            int peerPort = Integer.parseInt(args[2]);

            if (listenPort <= 0 || peerPort <= 0) {
                throw new NumberFormatException("Los puertos deben ser números positivos.");
            }

            Nodo nodo = new Nodo(listenPort, peerHost, peerPort);
            nodo.start();
        } catch (NumberFormatException e) {
            System.err.println("Error: Asegúrate de ingresar números válidos para los puertos.");
        }
    }
}
