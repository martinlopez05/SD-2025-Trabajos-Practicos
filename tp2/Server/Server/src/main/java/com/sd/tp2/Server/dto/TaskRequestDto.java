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
    private Double parameter1;
    private Double parameter2;

    public String getOperation() {
        return operation;
    }

    public Double getParameter1() {
        return parameter1;
    }

    public Double getParameter2() {
        return parameter2;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setParameter1(Double parameter1) {
        this.parameter1 = parameter1;
    }

    public void setParameter2(Double parameter2) {
        this.parameter2 = parameter2;
    }

    public TaskRequestDto() {
    }

}
