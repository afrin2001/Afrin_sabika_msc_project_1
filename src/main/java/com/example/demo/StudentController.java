package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {

    private final StudentRepository repo;

    public StudentController(StudentRepository repo){
        this.repo = repo;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("students", repo.findAll());
        return "index";
    }

    @PostMapping("/save")
    public String save(Student student){
        repo.save(student);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        repo.deleteById(id);
        return "redirect:/";
    }
}