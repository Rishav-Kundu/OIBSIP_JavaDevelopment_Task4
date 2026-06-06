package com.rishav.onlineexam.controller;

import com.rishav.onlineexam.entity.Question;
import com.rishav.onlineexam.entity.Result;
import com.rishav.onlineexam.entity.User;
import com.rishav.onlineexam.service.ExamService;
import com.rishav.onlineexam.service.UserService;

import org.springframework.security.core.Authentication;
import com.rishav.onlineexam.config.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final UserService userService;
    private final ExamService examService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    public DashboardController(
            UserService userService,
            ExamService examService,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
            CustomUserDetailsService userDetailsService) {

        this.userService = userService;
        this.examService = examService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    private User getCurrentUser(
            Authentication authentication) {

        return userService.findByUsername(
                authentication.getName());
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication,
            Model model) {

        User user = getCurrentUser(authentication);

        model.addAttribute("user", user);

        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile(
            Authentication authentication,
            Model model) {

        User user = getCurrentUser(authentication);

        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            Authentication authentication,
            @RequestParam Map<String,String> params) {

        User user = getCurrentUser(authentication);
        boolean credentialsChanged = false;

        if(params.containsKey("name"))
            user.setName(params.get("name"));

        if(params.containsKey("email"))
            user.setEmail(params.get("email"));

        if(params.containsKey("phone"))
            user.setPhone(params.get("phone"));

        if(params.containsKey("rollNo"))
            user.setRollNo(params.get("rollNo"));

        // Handle password change: if provided, encode and save
        if(params.containsKey("password") && params.get("password") != null && !params.get("password").trim().isEmpty()) {
            String raw = params.get("password").trim();
            String encoded = passwordEncoder.encode(raw);
            user.setPassword(encoded);
            credentialsChanged = true;
        }

        userService.save(user);
        // If username or password changed, try to update the SecurityContext so user stays logged in
        if (credentialsChanged) {
            try {
                UserDetails ud = userDetailsService.loadUserByUsername(user.getUsername());
                UsernamePasswordAuthenticationToken newAuth =
                        new UsernamePasswordAuthenticationToken(ud, ud.getPassword(), ud.getAuthorities());
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(newAuth);
                return "redirect:/dashboard";
            } catch (Exception ex) {
                // If anything goes wrong (e.g., username not yet visible), clear context and require re-login
                org.springframework.security.core.context.SecurityContextHolder.clearContext();
                return "redirect:/login?updated";
            }
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/startExam")
    public String startExam(
            Authentication authentication,
            Model model) {

        User user = getCurrentUser(authentication);

        if(user.isExamAttempted()) {
            return "redirect:/viewResult";
        }

        List<Question> questions =
                examService.getRandomQuestions(10);

        model.addAttribute("questions", questions);
        model.addAttribute("timeSeconds", 300);

        return "exam";
    }

    @PostMapping("/submitExam")
    public String submitExam(
            Authentication authentication,
            @RequestParam Map<String,String> answers,
            Model model) {

        User user = getCurrentUser(authentication);
        if (user.isExamAttempted()) {
         return "redirect:/viewResult";
        }

        int score = 0;

        for(String key : answers.keySet()) {

            if(!key.startsWith("q_"))
                continue;

            Long id = Long.parseLong(
                    key.substring(2));

            Question q =
                    examService.findById(id);

            if(q != null &&
                    q.getCorrectAnswer().equals(
                            answers.get(key))) {

                score++;
            }
        }

        Result result =
                new Result(
                        user.getUsername(),
                        score,
                        10
                );

        examService.saveResult(result);

        user.setExamAttempted(true);

        userService.save(user);

        return "redirect:/dashboard?submitted";
    }

    @GetMapping("/viewResult")
    public String viewResult(
            Authentication authentication,
            Model model) {

        User user =
                getCurrentUser(authentication);

        Result result =
                examService.getLatestResult(
                        user.getUsername());

        model.addAttribute(
                "result",
                result);

        return "viewResult";
    }
}