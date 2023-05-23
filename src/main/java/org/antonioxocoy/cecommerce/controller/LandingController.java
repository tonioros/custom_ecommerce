package org.antonioxocoy.cecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/home")
public class LandingController {


    @GetMapping("/")
    public String home() {
        return "HOLA MUNDO";
    }
}
