package com.arquetipo;

import com.arquetipo.herramientas.Aramis;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ArquetipoApplicationTests {

    private Aramis aramis;
    
    //@Test
    public void contextLoads() {
            
        try {
            aramis.get().callSocket("", "");
        } catch (Exception ex) {
            Logger.getLogger(ArquetipoApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Esta bien");
        
    }
}
