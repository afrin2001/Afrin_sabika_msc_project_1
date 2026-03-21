package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StudentController {

    private final StudentRepository repo;
    private final GroqService aiService;

    public StudentController(StudentRepository repo, GroqService aiService){
        this.repo = repo;
        this.aiService = aiService;
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

    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestParam String message) {
    
        List<Student> students = repo.findAll();
    
        // 🔥 SIMPLE INTENT DETECTION (REAL ACTION)
        if (message.toLowerCase().contains("delete")) {
    
            for (Student s : students) {
                if (message.toLowerCase().contains(s.getName().toLowerCase())) {
                    repo.deleteById(s.getId());
                    return "✅ Student " + s.getName() + " deleted successfully!";
                }
            }
    
            return "⚠️ Student not found!";
        }
    
        // 🤖 Otherwise use AI
        return aiService.getAIResponse(message, students);
    }
}
