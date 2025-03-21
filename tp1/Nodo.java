
import java.io.*;
import java.net.*;

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
        try (ServerSocket serverSocket = new ServerSocket(listenPort)) {
            System.out.println("Servidor escuchando en el puerto " + listenPort);
            while (true) {
                try (Socket socket = serverSocket.accept(); BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("Cliente conectado desde " + socket.getInetAddress());
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

    private void startClient() {
        while (true) {
            try (Socket socket = new Socket(peerHost, peerPort); PrintWriter output = new PrintWriter(socket.getOutputStream(), true); BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                System.out.println("Conectado a " + peerHost + ":" + peerPort);

                while (true) {
                    output.println("Hola, Servidor!");
                    String response = input.readLine();
                    if (response == null) {
                        System.out.println("Servidor cerró la conexión inesperadamente.");
                        break;
                    }
                    System.out.println("Servidor responde: " + response);
                    Thread.sleep(5000);
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("No se pudo conectar a " + peerHost + ":" + peerPort + ". Reintentando...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Uso: java Nodo <puerto escucha> <IP destino> <puerto destino>");
            return;
        }
        int listenPort = Integer.parseInt(args[0]);
        String peerHost = args[1];
        int peerPort = Integer.parseInt(args[2]);

        Nodo nodo = new Nodo(listenPort, peerHost, peerPort);
        nodo.start();
    }
}
