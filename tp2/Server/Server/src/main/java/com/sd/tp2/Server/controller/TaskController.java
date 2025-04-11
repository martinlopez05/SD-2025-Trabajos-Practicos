/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd.tp2.Server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectExecResponse.Container;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.sd.tp2.Server.Model.Task;
import com.sd.tp2.Server.dto.TaskRequestDto;
import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Usuario
 */
@RestController
public class TaskController {

    @PostMapping("/getRemoteTask")
    public ResponseEntity<String> ejecutarTareaRemota(@RequestBody Task request) throws IOException, InterruptedException {
        String containerId = null;
        try {
            ensureDockerNetworkExists("task-network");
            connectThisContainerToNetwork("task-network");
            String containerName = "taskContainer-" + UUID.randomUUID();
            containerId = startDockerContainer(request.getDockerImage(), containerName);
            if (!isContainerRunning(containerId)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error: El contenedor no se inició correctamente.");
            }
            TaskRequestDto requestDto = new TaskRequestDto();

            requestDto.setOperation(request.getOperation());
            requestDto.setParameter1(request.getParameter1());
            requestDto.setParameter2(request.getParameter2());
            String result = callTaskService(requestDto, containerName);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error ejecutando la tarea: " + e.getMessage());
        } finally {
            stopDockerContainer(containerId);
        }
    }

    private String startDockerContainer(String imageName, String containerName) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "docker", "run", "-d",
                "--name", containerName,
                "--hostname", containerName,
                "--network", "task-network",
                "-p", "8081:8081",
                "--restart", "no",
                imageName
        );

        Process process = pb.start();
        process.waitFor();
        return new String(process.getInputStream().readAllBytes()).trim();
    }

    private boolean isContainerRunning(String containerId) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("docker", "inspect", "-f", "{{.State.Running}}", containerId);
        Process process = pb.start();
        process.waitFor();
        String output = new String(process.getInputStream().readAllBytes()).trim();
        return "true".equals(output);
    }

    private void waitForService(String url) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10; i++) {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    return;
                }
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
    }

    private String callTaskService(TaskRequestDto request, String containerName) throws IOException, InterruptedException {
        String url = "http://" + containerName + ":8081/executeTask";
        waitForService("http://" + containerName + ":8081/health");
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);
        System.out.println("Enviando JSON al taskserver: " + json);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }

    private void stopDockerContainer(String containerName) throws IOException, InterruptedException {
        if (containerName == null || containerName.isEmpty()) {
            return;
        }
        new ProcessBuilder("docker", "stop", containerName).start().waitFor();
        new ProcessBuilder("docker", "rm", containerName).start().waitFor();
    }

    private void ensureDockerNetworkExists(String networkName) throws IOException, InterruptedException {
        ProcessBuilder checkNetwork = new ProcessBuilder(
                "docker", "network", "ls", "--filter", "name=" + networkName, "--format", "{{.Name}}"
        );
        Process checkProcess = checkNetwork.start();
        checkProcess.waitFor();
        String output = new String(checkProcess.getInputStream().readAllBytes()).trim();

        if (!output.equals(networkName)) {
            System.out.println("Red Docker no encontrada. Creando: " + networkName);
            ProcessBuilder createNetwork = new ProcessBuilder(
                    "docker", "network", "create", "--driver", "bridge", "--attachable", networkName
            );
            Process createProcess = createNetwork.start();
            int result = createProcess.waitFor();

            if (result != 0) {
                String errorOutput = new String(createProcess.getErrorStream().readAllBytes());
                throw new RuntimeException("No se pudo crear la red Docker: " + errorOutput);
            }
        } else {
            System.out.println("Red Docker ya existe: " + networkName);
        }
    }

    private void connectThisContainerToNetwork(String networkName) throws IOException, InterruptedException {
        // Obtener el ID del contenedor actual desde el cgroup (funciona dentro de un contenedor Linux)
        String containerId = Files.lines(Path.of("/proc/self/cgroup"))
                .filter(line -> line.contains("docker"))
                .map(line -> line.substring(line.lastIndexOf("/") + 1))
                .findFirst()
                .orElse(null);

        if (containerId == null || containerId.isBlank()) {
            System.out.println("Advertencia: no se pudo detectar el ID del contenedor actual. ¿Está ejecutando esto fuera de Docker?");
            return;
        }

        // Comprobar si ya está conectado a la red
        ProcessBuilder inspectPb = new ProcessBuilder("docker", "inspect", containerId, "--format", "{{json .NetworkSettings.Networks}}");
        Process inspectProcess = inspectPb.start();
        inspectProcess.waitFor();

        String output = new String(inspectProcess.getInputStream().readAllBytes());
        if (output.contains(networkName)) {
            System.out.println("Contenedor ya conectado a la red " + networkName);
            return;
        }

        // Conectar a la red
        System.out.println("Conectando contenedor actual a la red " + networkName);
        ProcessBuilder connectPb = new ProcessBuilder("docker", "network", "connect", networkName, containerId);
        Process connectProcess = connectPb.start();
        int result = connectProcess.waitFor();
        if (result != 0) {
            String err = new String(connectProcess.getErrorStream().readAllBytes());
            throw new RuntimeException("Error conectando el contenedor a la red: " + err);
        }

        System.out.println("Contenedor conectado exitosamente a la red " + networkName);
    }

}
