package com.autolog.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {

	@GetMapping(value= {"/","/login"})
	public String login() {
		
		return "login";
	}
	
	@GetMapping("/index")
    public String index(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "index";
    }
}
