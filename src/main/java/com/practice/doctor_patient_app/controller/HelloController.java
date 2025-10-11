package com.practice.doctor_patient_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")  // 👈 secure base path
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, you are authenticated!";
    }
}
