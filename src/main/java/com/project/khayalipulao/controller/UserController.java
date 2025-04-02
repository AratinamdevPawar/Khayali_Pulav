package com.project.khayalipulao.controller;
import com.project.khayalipulao.model.User;
import com.project.khayalipulao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

   

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }

        userService.saveUser(user);
        model.addAttribute("success", "Registration successful! Please log in.");
        return "login"; 
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, 
                            Model model, HttpSession session) {
        User user = userService.authenticateUser(email, password);
        if (user != null) {
            String role = user.getRole();       
            session.setAttribute("userId", user.getId()); // Store user ID in session

            if ("provider".equalsIgnoreCase(role)) {
                session.setAttribute("providerId", user.getId()); // Store providerId in session
                return "redirect:/provider";
            } else if ("user".equalsIgnoreCase(role)) {
                return "redirect:/user";
            }
        }

        model.addAttribute("error", "Invalid email or password");
        return "login"; 
    }

    @GetMapping("/user")
    public String showUserPage() {
        return "user"; 
    }

    @GetMapping("/provider")
    public String showProviderPage(HttpSession session, Model model) {
        Object providerId = session.getAttribute("providerId");
        
        if (providerId == null) {
            return "redirect:/login"; // Redirect to login if session is not set
        }

        model.addAttribute("providerId", providerId); // Pass providerId to view
        return "home_provider"; 
    }
    
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clears session data
        return "redirect:/login"; 
    }
}