package com.codegym.inote.controller;

import com.codegym.inote.model.Note;
import com.codegym.inote.model.NoteType;
import com.codegym.inote.model.Stack;
import com.codegym.inote.service.NoteService;
import com.codegym.inote.service.NoteTypeService;
import com.codegym.inote.service.StackService;
import com.codegym.inote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/user/noteType")
public class NoteTypeController {

    public static final String NOTE_TYPE = "noteType";
    public static final String ERROR_404 = "/error-404";
    @Autowired
    private NoteTypeService noteTypeService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private StackService stackService;

    @Autowired
    private UserService userService;

    @ModelAttribute("stacks")
    public Page<Stack> stacks(Pageable pageable) {
        return stackService.findAllByUser(userService.getCurrentUser(), pageable);
    }

    @GetMapping("/noteTypeList")
    public ModelAndView showNoteTypeList(@RequestParam("search") Optional<String> search, Pageable pageable) {
        Page<NoteType> noteTypes;

        if (search.isPresent()) {
            noteTypes = noteTypeService.findNoteTypeByName(search.get(), pageable);
        } else {
            noteTypes = noteTypeService.findAllByUser(userService.getCurrentUser(), pageable);
        }

        ModelAndView modelAndView = new ModelAndView("/noteType/list");
        modelAndView.addObject("noteTypes", noteTypes);
        modelAndView.addObject("user",userService.getCurrentUser());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/noteType/create");
        modelAndView.addObject(NOTE_TYPE, new NoteType());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveNoteType(@ModelAttribute NoteType noteType) {
        noteType.setUser(userService.getCurrentUser());
        noteTypeService.save(noteType);

        ModelAndView modelAndView = new ModelAndView("/noteType/create");
        modelAndView.addObject(NOTE_TYPE, new NoteType());
        modelAndView.addObject("message", "Created!");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        NoteType noteType = noteTypeService.findById(id);
        if (noteType != null) {
            ModelAndView modelAndView = new ModelAndView("/noteType/edit");
            modelAndView.addObject(NOTE_TYPE, noteType);
            return modelAndView;
        }
        return new ModelAndView(ERROR_404);
    }

    @PostMapping("/edit")
    public ModelAndView editNoteType(@ModelAttribute NoteType noteType) {
        noteType.setUser(userService.getCurrentUser());
        noteTypeService.save(noteType);

        ModelAndView modelAndView = new ModelAndView("/noteType/edit");
        modelAndView.addObject(NOTE_TYPE, noteType);
        modelAndView.addObject("message", "Updated!");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        NoteType noteType = noteTypeService.findById(id);
        if (noteType != null) {
            ModelAndView modelAndView = new ModelAndView("/noteType/delete");
            modelAndView.addObject(NOTE_TYPE, noteType);
            return modelAndView;
        }
        return new ModelAndView(ERROR_404);
    }

    @PostMapping("/delete")
    public String deleteNoteType(@ModelAttribute NoteType noteType) {
        noteTypeService.remove(noteType.getId());
        return "redirect:/user/noteType/noteTypeList";
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewNoteType(@PathVariable Long id, Pageable pageable) {
        NoteType noteType = noteTypeService.findById(id);
        if (noteType == null) {
            return new ModelAndView(ERROR_404);
        }
        Page<Note> notes = noteService.findAllByNoteType(noteType, pageable);
        ModelAndView modelAndView = new ModelAndView("/noteType/view");
        modelAndView.addObject(NOTE_TYPE, noteType);
        modelAndView.addObject("notes", notes);
        return modelAndView;
    }
}
