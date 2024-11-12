package com.cst438.project3.api.controller;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.DeleteMapping;
@RestController
public class UserController {
    @GetMapping("/")
    public String home(){
        return "This is the root! Woohoo!!";
    }
    @GetMapping("/error")
    public String error(){
        return "error";
    }

}
