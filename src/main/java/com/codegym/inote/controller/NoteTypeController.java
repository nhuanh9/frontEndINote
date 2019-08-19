package com.codegym.inote.controller;

import com.codegym.inote.model.NoteType;
import com.codegym.inote.service.NoteTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/noteType")
public class NoteTypeController {
    @Autowired
    private NoteTypeService noteTypeService;

    @GetMapping("/noteTypeList")
    public ModelAndView showNoteTypeList(Pageable pageable) {
        Page<NoteType> noteTypes = noteTypeService.findAll(pageable);

        ModelAndView modelAndView = new ModelAndView("/noteType/list");
        modelAndView.addObject("noteTypes", noteTypes);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/noteType/create");
        modelAndView.addObject("noteType", new NoteType());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveNoteType(@ModelAttribute NoteType noteType) {
        noteTypeService.save(noteType);

        ModelAndView modelAndView = new ModelAndView("/noteType/create");
        modelAndView.addObject("noteType", new NoteType());
        modelAndView.addObject("message", "Created!");
        return modelAndView;
    }
}
