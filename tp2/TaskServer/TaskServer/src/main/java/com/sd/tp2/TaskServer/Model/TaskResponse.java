/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd.tp2.TaskServer.Model;

/**
 *
 * @author Usuario
 */
public class TaskResponse {
    
    private String operation;
    private Double result;

    

    public Double getResult() {
        return result;
    }

    
    

    public void setResult(Double result) {
        this.result = result;
    }

    public TaskResponse() {
    }

    public TaskResponse(String operation, Double result) {
        this.operation = operation;
        this.result = result;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    

    
       
    
}
