/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arquetipo.controlador;

/**
 *
 * @author roberto.espinoza
 */

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/")
@RestController()
public class Controlador {

	/*
        @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HelloweenResponse> hello(Principal principal) {

		return new ResponseEntity<HelloweenResponse>(
				new HelloweenResponse("Happy Halloween, " + principal.getName() + "!"), HttpStatus.OK);
	}

	public static class HelloweenResponse {
		private String message;
		public HelloweenResponse(String message) {
			this.message = message;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
        */
        
	@RequestMapping(value={"hola"})
	public String hola(Model model) throws Exception {
                String titulo = "Bienvenido al sistema... ya vamos a empezar";
		model.addAttribute("titulo", titulo);
		return "index";
	}
}
