package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

        int outcome = fileService.addFile(file, currentUser.getUserid());

        if (outcome > 0) {
            model.addAttribute("success", "File was successfully saved");
        } else if (outcome == 0) {
            model.addAttribute("exist", "Your changes were not saved, filename already exists.");
        } else {
            model.addAttribute("error", "There was an error with file upload");
        }
        return "result";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") int fileId, Model model) {
        if (fileService.deleteFile(fileId) > 0) {
            model.addAttribute("success", "File was successfully deleted");
        }
        return "result";
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewFile(@PathVariable("id") int fileId) {
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }
}
