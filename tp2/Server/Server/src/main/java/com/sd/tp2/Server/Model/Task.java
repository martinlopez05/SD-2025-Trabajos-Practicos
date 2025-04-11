/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd.tp2.Server.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Usuario
 */
@Getter
@Setter
@AllArgsConstructor
public class Task {
    @NotBlank(message = "La operación no puede estar vacía")
    private String operation;

    @NotNull(message = "El parámetro 1 es obligatorio")
    private Double parameter1;

    @NotNull(message = "El parámetro 2 es obligatorio")
    private Double parameter2;

    @NotBlank(message = "La imagen de Docker es obligatoria")
    private String dockerImage;

}
