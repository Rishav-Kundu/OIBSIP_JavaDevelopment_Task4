package com.rishav.onlineexam.controller;

import com.rishav.onlineexam.entity.User;
import com.rishav.onlineexam.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam Map<String, String> params,
            Model model) {

        String name = params.getOrDefault("name", "").trim();
        String email = params.getOrDefault("email", "").trim();
        String phone = params.getOrDefault("phone", "").trim();
        String rollNo = params.getOrDefault("rollNo", "").trim();
        String password = params.getOrDefault("password", "").trim();

        if (name.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "Name and password are required.");
            return "signup";
        }

        if (userService.findByUsername(name) != null) {
            model.addAttribute("error", "That username already exists. Choose a different name.");
            return "signup";
        }

        User user = new User(
                name,
                passwordEncoder.encode(password),
                name,
                email,
                phone,
                rollNo,
                "ROLE_STUDENT"
        );

        userService.save(user);

        return "redirect:/login?registered";
    }
}