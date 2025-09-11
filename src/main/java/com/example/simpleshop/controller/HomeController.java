package com.example.simpleshop.controller;

import com.example.simpleshop.model.User;
import com.example.simpleshop.repo.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductRepository productRepo;

    public HomeController(ProductRepository productRepo){
        this.productRepo = productRepo;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model, HttpSession session){
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("user", session.getAttribute("user"));
        return "index"; // index.html
    }
}
