package unlu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class Nodo {
    private final int puertoLocal;
    private final String ipNodoD;
    private final int puertoNodoD;

    public Nodo(int puertoLocal, String ipNodoD, int puertoNodoD) {
        this.puertoLocal = puertoLocal;
        this.ipNodoD = ipNodoD;
        this.puertoNodoD = puertoNodoD;
    }

    public void registerInD() {
        try {
            JSONObject json = new JSONObject();
            json.put("ip", InetAddress.getLocalHost().getHostAddress());
            json.put("puerto", String.valueOf(puertoLocal));
            json.put("horaRegistro", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))); // Registrar hora

            System.out.println("[Nodo C] Enviando solicitud de inscripción a Nodo D: " + json.toString());

            Socket socket = new Socket(ipNodoD, puertoNodoD);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(json.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            System.out.println("[Nodo C] Respuesta de Nodo D: " + response);

            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("[Nodo C] Error registrando en D: " + e.getMessage());
        }
    }

    public void consultarInscripciones() {
        try {
            Socket socket = new Socket(ipNodoD, puertoNodoD);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("CONSULTAR_INSCRIPCIONES");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            System.out.println("[Nodo C] Inscripciones activas: " + response);

            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("[Nodo C] Error consultando inscripciones: " + e.getMessage());
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
        nodo.consultarInscripciones();
    }
}

