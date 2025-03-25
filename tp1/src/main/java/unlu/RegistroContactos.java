package unlu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegistroContactos {
    private final int puerto;
    private static final String ARCHIVO_INSCRIPCIONES = "inscripciones.json";

    public RegistroContactos(int puerto) {
        this.puerto = puerto;
    }

    private synchronized void registrarNodo(String ip, String puerto, String horaRegistro) {
        try {
            List<JSONObject> inscripciones = cargarInscripciones();
            String ventanaTiempo = calcularVentana(horaRegistro);
            
            JSONObject nuevaInscripcion = new JSONObject();
            nuevaInscripcion.put("ip", ip);
            nuevaInscripcion.put("puerto", puerto);
            nuevaInscripcion.put("ventana", ventanaTiempo);

            inscripciones.add(nuevaInscripcion);
            guardarInscripciones(inscripciones);
        } catch (Exception e) {
            System.err.println("[Nodo D] Error registrando nodo: " + e.getMessage());
        }
    }

    private synchronized List<JSONObject> cargarInscripciones() {
        File archivo = new File(ARCHIVO_INSCRIPCIONES);
        if (!archivo.exists()) return new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String contenido = reader.readLine();
            if (contenido == null || contenido.isEmpty()) return new ArrayList<>();
            JSONArray jsonArray = new JSONArray(contenido);
            List<JSONObject> lista = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) lista.add(jsonArray.getJSONObject(i));
            return lista;
        } catch (IOException e) {
            System.err.println("[Nodo D] Error leyendo inscripciones: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private synchronized void guardarInscripciones(List<JSONObject> inscripciones) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_INSCRIPCIONES))) {
            writer.println(new JSONArray(inscripciones).toString());
        } catch (IOException e) {
            System.err.println("[Nodo D] Error guardando inscripciones: " + e.getMessage());
        }
    }

    private synchronized String calcularVentana(String hora) {
        LocalTime tiempo = LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm:ss"));
        int minutoVentana = tiempo.getMinute() + 1;
        return String.format("%02d:%02d", tiempo.getHour(), minutoVentana);
    }

    private synchronized String obtenerInscripcionesActivas() {
        List<JSONObject> inscripciones = cargarInscripciones();
        String ventanaActual = calcularVentana(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        JSONArray activas = new JSONArray();
        for (JSONObject inscripcion : inscripciones) {
            if (inscripcion.getString("ventana").equals(ventanaActual)) {
                activas.put(inscripcion);
            }
        }
        return activas.toString();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("[Nodo D] Escuchando en el puerto " + puerto);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String receivedData = in.readLine();

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                if (receivedData.equals("CONSULTAR_INSCRIPCIONES")) {
                    out.println(obtenerInscripcionesActivas());
                } else {
                    JSONObject json = new JSONObject(receivedData);
                    registrarNodo(json.getString("ip"), json.getString("puerto"), json.getString("horaRegistro"));
                    out.println("{\"status\": \"OK\", \"message\": \"Nodo registrado para la ventana siguiente.\"}");
                }

                in.close();
                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[Nodo D] Error en servidor: " + e.getMessage());
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

