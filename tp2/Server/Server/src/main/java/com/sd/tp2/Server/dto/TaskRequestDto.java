/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd.tp2.Server.dto;

/**
 *
 * @author Usuario
 */
public class TaskRequestDto {
    
    private String operation;
    private Double param1;
    private Double param2; 

    public String getOperation() {
        return operation;
    }

    public Double getParam1() {
        return param1;
    }

    public Double getParam2() {
        return param2;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setParam1(Double param1) {
        this.param1 = param1;
    }

    public void setParam2(Double param2) {
        this.param2 = param2;
    }

    public TaskRequestDto() {
    }
    
    
}
