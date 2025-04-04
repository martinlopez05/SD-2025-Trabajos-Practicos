/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd.tp2.TaskServer.Controller;

import com.sd.tp2.TaskServer.Model.TaskRequest;
import com.sd.tp2.TaskServer.Model.TaskResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Usuario
 */
@RestController

public class TaskController {

    @PostMapping("/executeTask")
    public ResponseEntity<?> ejecutarTarea(@RequestBody TaskRequest request) {
        TaskResponse response = new TaskResponse();

        double resultado;
        Double param1 = request.getParam1();
        Double param2 = request.getParam2();
      
        switch (request.getOperation().toLowerCase()) {
            case "suma":
                resultado = param1 + param2;
                break;
            case "resta":
                resultado = param1 - param2;
                break;
            case "producto":
                resultado = param1 * param2;
                break;
            case "division":
                if (param2 == 0) {
                    return ResponseEntity.badRequest().body(new TaskResponse("Error: División por cero", null));
                }
                resultado = param1 / param2;
                break;
            default:
                return ResponseEntity.badRequest().body(new TaskResponse("Operación no válida", null));
        }
        
        response.setOperation(request.getOperation());
        response.setResult(resultado);
        return ResponseEntity.ok(response);
    }

}
