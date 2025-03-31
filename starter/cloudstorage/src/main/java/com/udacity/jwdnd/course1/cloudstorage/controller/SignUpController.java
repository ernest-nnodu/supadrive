package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignUp() {
        return "/signup";
    }

    @PostMapping
    public String signUpUser(User user, Model model) {
        if (userNameExists(user.getUsername())) {
            model.addAttribute("error", "Username already exists!");
            return "signup";
        }

        if (userService.addUser(user) > 0) {
            model.addAttribute("success", "You have successfully signed up!");
            return "signup";
        } else {
            model.addAttribute("error", "Error with registration, please try again later");
            return "signup";
        }
    }

    private boolean userNameExists(String username) {

        return userService.getUser(username) != null;
    }


}
