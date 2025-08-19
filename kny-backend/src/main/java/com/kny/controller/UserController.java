package com.kny.controller;

import com.kny.model.User;
import com.kny.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                User user = userService.findByEmail(email);
                model.addAttribute("user", user);
                return "profile";
            }
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading profile: " + e.getMessage());
            return "profile";
        }
    }

    @PostMapping("/api/update")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody User user, Authentication authentication) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                User updatedUser = userService.updateUser(email, user);
                return ResponseEntity.ok(updatedUser);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }

    @GetMapping("/api/info")
    @ResponseBody
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                User user = userService.findByEmail(email);
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting user info: " + e.getMessage());
        }
    }
}