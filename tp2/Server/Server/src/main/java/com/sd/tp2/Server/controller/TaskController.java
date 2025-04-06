/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd.tp2.Server.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectExecResponse.Container;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.sd.tp2.Server.Model.Task;
import com.sd.tp2.Server.dto.TaskRequestDto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
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

    @GetMapping("/getRemoteTask")
    public ResponseEntity<String> ejecutarTareaRemota(@RequestBody Task request) throws IOException, InterruptedException {
        String containerId = null;
        try {
            containerId = startDockerContainer(request.getDockerImage());
            if (!isContainerRunning(containerId)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error: El contenedor no se inició correctamente.");
            }
            TaskRequestDto requestDto = new TaskRequestDto();

            requestDto.setOperation(request.getOperation());
            requestDto.setParameter1(request.getParameter1());
            requestDto.setParameter2(request.getParameter2());
            String result = callTaskService(requestDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error ejecutando la tarea: " + e.getMessage());
        } finally {
            stopDockerContainer(containerId);
        }
    }

   

    private String startDockerContainer(String imageName) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("docker", "run", "-d", "-p", "8081:8081", imageName);
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

    private String callTaskService(TaskRequestDto request) throws IOException, InterruptedException {
        String url = "http://localhost:8081/executeTask";
        waitForService(url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }

    private void stopDockerContainer(String containerId) throws IOException, InterruptedException {
        if (containerId == null || containerId.isEmpty()) {
            return;
        }
        new ProcessBuilder("docker", "stop", containerId).start().waitFor();
        new ProcessBuilder("docker", "rm", containerId).start().waitFor();
    }

}
