package com.example.simpleshop.controller;

import com.example.simpleshop.model.CartItem;
import com.example.simpleshop.model.Product;
import com.example.simpleshop.model.User;
import com.example.simpleshop.repo.CartItemRepository;
import com.example.simpleshop.repo.ProductRepository;
import com.example.simpleshop.repo.UserRepository;  // Import UserRepository
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartOrderController {

    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo; // Add UserRepository

    public CartOrderController(CartItemRepository cartRepo, ProductRepository productRepo, UserRepository userRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo; // Initialize it
    }

    // =======================
    // View Cart Page
    // =======================
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        User user = userRepo.findById(sessionUser.getId()).orElse(null);
        if (user == null) return "redirect:/login";

        List<CartItem> items = cartRepo.findByUserId(user.getId());
        model.addAttribute("cartItems", items);
        return "cart"; // Thymeleaf template "cart.html"
    }

    // =======================
    // Add Product to Cart via GET
    // =======================
    @GetMapping("/add/{productId}")
    public String addToCartGet(@PathVariable Long productId, HttpSession session) {
        return addToCartInternal(productId, session);
    }

    // =======================
    // Add Product to Cart via POST
    // =======================
    @PostMapping("/add-to-cart")
    public String addToCartPost(@RequestParam Long productId, HttpSession session) {
        return addToCartInternal(productId, session);
    }

    // =======================
    // Internal Add to Cart Logic
    // =======================
    private String addToCartInternal(Long productId, HttpSession session) {
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null) return "redirect:/login";

    // Fetch user from DB to ensure it's managed
    User user = userRepo.findById(sessionUser.getId()).orElse(null);
    if (user == null) return "redirect:/login";

    // Fetch product from DB to ensure it's managed and exists
    Product product = productRepo.findById(productId).orElse(null);
    if (product == null) return "redirect:/";

    // Check if the product is already in the cart
    CartItem item = cartRepo.findByUserId(user.getId())
            .stream()
            .filter(ci -> ci.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(null);

    if (item == null) {
        item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(1);
    } else {
        item.setQuantity(item.getQuantity() + 1);
    }

    cartRepo.save(item);

    return "redirect:/cart";
}


    // =======================
    // Remove item from cart
    // =======================
    @GetMapping("/remove/{id}")
    public String removeItem(@PathVariable Long id) {
        cartRepo.deleteById(id);
        return "redirect:/cart";
    }

    // =======================
    // Checkout
    // =======================
    @PostMapping("/checkout")
    public String checkout(@RequestParam String address, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        User user = userRepo.findById(sessionUser.getId()).orElse(null);
        if (user == null) return "redirect:/login";

        List<CartItem> items = cartRepo.findByUserId(user.getId());

        // Save order logic could go here if needed

        cartRepo.deleteAll(items); // Empty cart after checkout

        return "redirect:/cart";
    }
}
