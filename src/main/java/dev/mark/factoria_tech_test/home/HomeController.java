package dev.mark.factoria_tech_test.home;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(path = "")
    public String index() {
        
        return "Hello, Factoría F5! This is the home page for the backend part of the technical skills test.";
        
    }
    
}