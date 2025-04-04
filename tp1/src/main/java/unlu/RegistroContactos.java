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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegistroContactos {
    private final int puerto;
    private static final String ARCHIVO_INSCRIPCIONES = "inscripciones.json";
    private static final ZoneId ZONA_ARGENTINA = ZoneId.of("America/Argentina/Buenos_Aires");

    public RegistroContactos(int puerto) {
        this.puerto = puerto;
        iniciarActualizacionPeriodica();
    }

    private synchronized void registrarNodo(String ip, String puerto, String horaRegistro) {
        try {
            List<JSONObject> inscripciones = cargarInscripciones();
            String ventanaTiempo = calcularVentanaSiguiente(horaRegistro);

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

    private synchronized String calcularVentanaSiguiente(String hora) {
        LocalTime tiempo = LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm:ss")).withSecond(0);
        LocalTime siguienteVentana = tiempo.plusMinutes(1);
        return siguienteVentana.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private synchronized String obtenerVentanaActual() {
        return LocalTime.now(ZONA_ARGENTINA)
                .withSecond(0)
                .format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private synchronized String obtenerInscripcionesActivas() {
        List<JSONObject> inscripciones = cargarInscripciones();
        String ventanaActual = obtenerVentanaActual();

        JSONArray activas = new JSONArray();
        for (JSONObject inscripcion : inscripciones) {
            if (inscripcion.getString("ventana").equals(ventanaActual)) {
                activas.put(inscripcion);
            }
        }
        return activas.toString();
    }

    private void iniciarActualizacionPeriodica() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                actualizarVentana();
            }
        }, 0, 60 * 1000); // Ejecutar cada 60 segundos
    }

    private synchronized void actualizarVentana() {
        List<JSONObject> inscripciones = cargarInscripciones();
        String ventanaActual = obtenerVentanaActual();
        List<JSONObject> nuevasInscripciones = new ArrayList<>();

        Iterator<JSONObject> iterator = inscripciones.iterator();
        while (iterator.hasNext()) {
            JSONObject inscripcion = iterator.next();
            if (inscripcion.getString("ventana").equals(ventanaActual)) {
                nuevasInscripciones.add(inscripcion);
                iterator.remove();
            }
        }

        // Guardar solo las inscripciones futuras en el archivo
        guardarInscripciones(inscripciones);

        System.out.println("[Nodo D] Ventana actualizada: " + ventanaActual);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("[Nodo D] Escuchando en el puerto " + puerto);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String receivedData = in.readLine();

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                if ("CONSULTAR_INSCRIPCIONES".equals(receivedData)) {
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