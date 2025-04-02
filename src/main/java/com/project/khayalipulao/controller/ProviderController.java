package com.project.khayalipulao.controller;
import com.project.khayalipulao.model.MenuItem;
import com.project.khayalipulao.service.ProviderService;
import com.project.khayalipulao.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/provider")
public class ProviderController {
	 @Autowired
	    private ProviderService providerService;

	    @Autowired
	    private UserService userService;

	    @GetMapping("/uploadMenu")
	    public String showMenuPage(HttpSession session, Model model) {
	        Object providerId = session.getAttribute("providerId");

	        if (providerId == null) {
	            return "redirect:/login"; // Redirect if session is not set
	        }
	        
	        List<MenuItem> todayMenu = providerService.getTodayMenu();
	        model.addAttribute("todayMenu", todayMenu);
	        
	        model.addAttribute("providerId", providerId); // Pass providerId to view
	        return "uploadMenu"; 
	    }

	    @PostMapping("/uploadMenu")
	    public String uploadMenuItem(@ModelAttribute MenuItem menu, HttpSession session,  RedirectAttributes redirectAttributes) {
	        
	    	Object providerIdObj = session.getAttribute("providerId");
	        
	        if (providerIdObj == null) {
	        	return "redirect:/login"; // Redirect if session is not set
	        }

	        int providerId = (int) providerIdObj; // Cast to int
	        providerService.addMenuItem(menu, providerId);
	        redirectAttributes.addFlashAttribute("success", "Menu item added successfully!");


	        return "redirect:/provider/uploadMenu";
	    }

	   
}

