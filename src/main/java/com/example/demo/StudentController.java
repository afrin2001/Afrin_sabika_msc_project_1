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

    // ✅ CHAT FEATURE (INSIDE CLASS)
    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestParam String message) {

        message = message.toLowerCase();

        if(message.contains("hello"))
            return "Hello! How can I help you?";

        else if(message.contains("add"))
            return "Click Add Student button.";

        else if(message.contains("delete"))
            return "Use delete button near student.";

        else if(message.contains("ci") || message.contains("cd"))
            return "CI/CD automates build using Jenkins.";

        else
            return "I didn't understand. Try again.";
    }
}