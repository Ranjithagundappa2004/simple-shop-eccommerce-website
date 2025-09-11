package com.example.simpleshop.controller;

import com.example.simpleshop.model.User;
import com.example.simpleshop.model.Product;
import com.example.simpleshop.repo.UserRepository;
import com.example.simpleshop.repo.ProductRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public AuthController(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    // Process login form
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user); // store user in session
            return "redirect:/home"; // go to user home/dashboard
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login"; // return login page with error
        }
    }

    // Show register page
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // register.html
    }

    // Process registration form
    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        if (userRepository.findByEmail(email) != null) {
            model.addAttribute("error", "Email already exists");
            return "register"; // show register page with error
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
        return "redirect:/login"; // after successful registration
    }

    // Show home/dashboard with products
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("user", session.getAttribute("user"));
        return "userpage"; // userpage.html
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
