package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/credentials")
@Controller
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String saveCredential(Authentication authentication, Model model,
                                @ModelAttribute("credential") Credential credential) {
        String username = authentication.getName();
        User loggedInUser = userService.getUser(username);
        credential.setUserid(loggedInUser.getUserid());

        if (credentialService.saveCredential(credential) > 0) {
            model.addAttribute("success", "Credential was successfully saved.");
        }
        return "result";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredential(@PathVariable("id") int credentialId, Model model) {
        if (credentialService.deleteCredential(credentialId) > 0) {
            model.addAttribute("success", "Credential was successfully deleted");
        }
        return "result";
    }
}
