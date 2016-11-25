/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arquetipo.herramientas;

import com.bbu.middleware.java.esb.ReceptorEmisor;
import com.bbu.middleware.java.esb.ReceptorEmisorService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 * @author roberto.espinoza
 */

@Component
public class Aramis {
    
    public ReceptorEmisor get() {
        
        ReceptorEmisorService servicio = new ReceptorEmisorService();
        //ReceptorEmisor llamado = servicio.getReceptorEmisor();
        return servicio.getReceptorEmisor();
            
    }
}
