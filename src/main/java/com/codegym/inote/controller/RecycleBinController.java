package com.codegym.inote.controller;

import com.codegym.inote.model.Note;
import com.codegym.inote.model.Trash;
import com.codegym.inote.service.NoteService;
import com.codegym.inote.service.RecycleBinService;
import com.codegym.inote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/recycleBin")
public class RecycleBinController {

    public static final String ERROR_404 = "/error-404";
    public static final String TRASH = "trash";

    @Autowired
    private RecycleBinService recycleBinService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @GetMapping("/trashes")
    public ModelAndView recycleBin(Pageable pageable) {
        Page<Trash> trashes = recycleBinService.findAllByUser(userService.getCurrentUser(), pageable);

        ModelAndView modelAndView = new ModelAndView("/recycleBin/list");
        modelAndView.addObject("trashes", trashes);
        modelAndView.addObject("user", userService.getCurrentUser());
        return modelAndView;
    }

    @GetMapping("/recovery/{id}")
    public ModelAndView RecoveryForm(@PathVariable Long id) {
        Trash trash = recycleBinService.findById(id);
        if (trash != null) {
            ModelAndView modelAndView = new ModelAndView("/recycleBin/recovery");
            modelAndView.addObject(TRASH, trash);
            return modelAndView;
        }
        return new ModelAndView(ERROR_404);
    }

    @PostMapping("/recovery")
    public String recoverNote(@ModelAttribute Trash trash) {
        Trash currentTrash = recycleBinService.findById(trash.getId());
        Note note = new Note();
        note.setTitle(currentTrash.getTitle());
        note.setContent(currentTrash.getContent());
        note.setUser(currentTrash.getUser());
        note.setTime(currentTrash.getTime());
        noteService.save(note);
        recycleBinService.remove(trash.getId());

        ModelAndView modelAndView = new ModelAndView("/recycleBin/recovery");
        modelAndView.addObject("trash", trash);
        modelAndView.addObject("note", note);
        return "redirect:/user/recycleBin/trashes";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Trash trash = recycleBinService.findById(id);
        if (trash != null) {
            ModelAndView modelAndView = new ModelAndView("/recycleBin/delete");
            modelAndView.addObject(TRASH, trash);
            return modelAndView;
        }
        return new ModelAndView(ERROR_404);
    }

    @PostMapping("/delete")
    public String deleteNoteType(@ModelAttribute Trash trash) {
        recycleBinService.remove(trash.getId());
        return "redirect:/user/recycleBin/trashes";
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewNote(@PathVariable Long id) {
        Trash trash = recycleBinService.findById(id);
        if (trash == null) {
            return new ModelAndView(ERROR_404);
        }

        ModelAndView modelAndView = new ModelAndView("/recycleBin/view");
        modelAndView.addObject(TRASH, trash);
        return modelAndView;
    }
}
