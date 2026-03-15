package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {

    @GetMapping("/")
    public String home() {
        return "CI/CD Pipeline Working!";
    }

    @GetMapping("/students")
    public String students() {
        return "Student API working";
    }
}