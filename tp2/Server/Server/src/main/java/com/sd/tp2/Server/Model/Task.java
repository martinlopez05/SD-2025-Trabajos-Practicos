/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd.tp2.Server.Model;

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
    
    private String operation;
    private Double parameter1;
    private Double parameter2;  
    private String dockerImage;

}
