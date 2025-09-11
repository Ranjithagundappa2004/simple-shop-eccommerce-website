package com.example.simpleshop.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.simpleshop.model.User;
import com.example.simpleshop.repo.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/myaccount")
    public String myAccount(HttpSession session, Model model) {
        // Get the user from session
        User sessionUser = (User) session.getAttribute("user");
        
        if (sessionUser == null) {
            return "redirect:/login"; // If not logged in, redirect to login
        }

        // Fetch user from database using ID
        User user = userRepo.findById(sessionUser.getId()).orElse(null);
        if (user == null) {
            return "redirect:/login"; // If not found, redirect to login
        }

        // Add user details to the model
        model.addAttribute("user", user);
        
        return "user"; // This will load user.html template
    }
}
