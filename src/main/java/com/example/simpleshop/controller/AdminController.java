package com.example.simpleshop.controller;

import com.example.simpleshop.model.Admin;
import com.example.simpleshop.model.Product;
import com.example.simpleshop.repo.AdminRepository;
import com.example.simpleshop.repo.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;

    public AdminController(AdminRepository adminRepository, ProductRepository productRepository) {
        this.adminRepository = adminRepository;
        this.productRepository = productRepository;
    }

    // =======================
    // LOGIN
    // =======================
    @GetMapping("/login")
    public String loginPage() {
        return "admin-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional<Admin> adminOpt = adminRepository.findByUsernameAndPassword(username, password);
        if (adminOpt.isPresent()) {
            session.setAttribute("admin", adminOpt.get());
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "admin-login";
        }
    }

    // =======================
    // REGISTER
    // =======================
    @GetMapping("/register")
    public String registerPage() {
        return "admin-register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           HttpSession session,
                           Model model) {

        if (adminRepository.existsByUsername(username)) {
            model.addAttribute("error", "Username already exists");
            return "admin-register";
        }

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        adminRepository.save(admin);

        session.setAttribute("admin", admin);
        return "redirect:/admin/dashboard";
    }

    // =======================
    // LOGOUT
    // =======================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    // =======================
    // DASHBOARD
    // =======================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("products", productRepository.findAll());
        return "admin-dashboard";
    }

    // =======================
    // ADD PRODUCT – Using Image URL
    // =======================
    @PostMapping("/add-product")
    public String addProduct(@RequestParam String name,
                             @RequestParam String description,
                             @RequestParam String brand,
                             @RequestParam String size,
                             @RequestParam double price,
                             @RequestParam String imagePath) {

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setBrand(brand);
        product.setSize(size);
        product.setPrice(price);
        product.setImagePath(imagePath); // Save image URL directly

        productRepository.save(product);
        return "redirect:/admin/dashboard";
    }

    // =======================
    // DELETE PRODUCT
    // =======================
    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }
}
