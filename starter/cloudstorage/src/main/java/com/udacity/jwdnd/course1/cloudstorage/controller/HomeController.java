package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/home")
@Controller
public class HomeController {

    private final UserService userService;
    private final NoteService noteService;
    private final FileService fileService;
    private final CredentialService credentialService;

    public HomeController(UserService userService, NoteService noteService, FileService fileService, CredentialService credentialService) {
        this.userService = userService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String getStorageHome(Authentication authentication, Model model) {

        if (authentication != null) {
            User loggedInUser = userService.getUser(authentication.getName());
            int userId = loggedInUser.getUserid();
            getUserFiles(userId, model);
            getUserNotes(userId, model);
            getUserCredentials(userId, model);
        }
        return "home";
    }

    private void getUserCredentials(int userId, Model model) {
        List<Credential> userCredentials = credentialService.getCredentialsByUserId(userId);
        model.addAttribute("credentials", userCredentials);
        model.addAttribute("currentCredential", new Credential());
    }

    private void getUserNotes(int userId, Model model) {
        List<Note> userNotes = noteService.getNotesByUserId(userId);
        model.addAttribute("notes", userNotes);
        model.addAttribute("currentNote", new Note());
    }

    private void getUserFiles(int userId, Model model) {
        List<File> userFiles = fileService.getFilesByUserId(userId);
        model.addAttribute("files", userFiles);
    }
}
