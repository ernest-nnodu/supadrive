package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/notes")
@Controller
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public String saveNote(Authentication authentication, @ModelAttribute("note") Note note, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        note.setUserid(user.getUserid());

        if (noteService.saveNote(note) > 0) {
            model.addAttribute("success", "Note was successfully saved");
        }
        return "result";
    }

    @GetMapping("/{id}")
    public String deleteNote(@PathVariable("id") int noteId, Model model) {
        if (noteService.deleteNote(noteId) > 0) {
            model.addAttribute("success", "Note was successfully deleted");
        }
        return "result";
    }
}
