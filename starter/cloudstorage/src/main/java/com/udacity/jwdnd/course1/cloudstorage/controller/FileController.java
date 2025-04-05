package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/files")
@Controller
public class FileController {

    private final UserService userService;
    private final FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping
    public String saveFile(Authentication authentication, Model model,
                           @RequestParam("fileUpload") MultipartFile file) {

        String username = authentication.getName();
        User currentUser = userService.getUser(username);


        if (fileService.addFile(file, currentUser.getUserid()) > 0) {
            model.addAttribute("success", "File was successfully saved");
        }
        return "result";
    }

    @GetMapping("/{id}")
    public String deleteFile(@PathVariable("id") int fileId, Model model) {
        if (fileService.deleteFile(fileId) > 0) {
            model.addAttribute("success", "File was successfully deleted");
        }
        return "result";
    }
}
