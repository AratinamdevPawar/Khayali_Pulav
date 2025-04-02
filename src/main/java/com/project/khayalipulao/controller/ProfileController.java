package com.project.khayalipulao.controller;

import com.project.khayalipulao.model.User;
import com.project.khayalipulao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {
        // Get logged in user's ID from session
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if not logged in
        }
        
        // Load user data
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty()) {
            return "redirect:/login"; // User not found in database
        }
        
        User user = userOptional.get();
        model.addAttribute("user", user);
        model.addAttribute("hasProfileImage", user.getProfileImage() != null && user.getProfileImage().length > 0);
        
        return "profile";
    }
    
    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("city") String city,
            @RequestParam("pinCode") String pinCode,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImage,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty()) {
            return "redirect:/login";
        }
        
        User currentUser = userOptional.get();
        
        // Update user fields
        currentUser.setName(name);
        currentUser.setPhone(phone);
        currentUser.setCity(city);
        currentUser.setPinCode(pinCode);
        
        // Handle profile image upload if a new file was provided
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                currentUser.setProfileImage(profileImage.getBytes());
                currentUser.setProfileImageContentType(profileImage.getContentType());
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Failed to upload profile image: " + e.getMessage());
                return "redirect:/profile";
            }
        }
        
        // Save the updated user
        userService.updateUser(currentUser);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        
        return "redirect:/profile";
    }
    
    // Endpoint to serve user profile images from the database
    @GetMapping("/user/profile-image/{id}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable("id") int userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        
        if (userOptional.isPresent() && userOptional.get().getProfileImage() != null) {
            User user = userOptional.get();
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(user.getProfileImageContentType()))
                    .body(user.getProfileImage());
        }
        
        return ResponseEntity.notFound().build();
    }
}