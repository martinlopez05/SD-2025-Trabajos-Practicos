/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd.tp2.TaskServer.Model;

/**
 *
 * @author Usuario
 */
public class TaskRequest {

    private String operation;
    private double parameter1;
    private double parameter2;

    // Getters y setters
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getParameter1() {
        return parameter1;
    }

    public void setParameter1(double param1) {
        this.parameter1 = param1;
    }

    public double getParameter2() {
        return parameter2;
    }

    public void setParameter2(double param2) {
        this.parameter2 = param2;
    }

    public TaskRequest() {
    }
    
    
}
