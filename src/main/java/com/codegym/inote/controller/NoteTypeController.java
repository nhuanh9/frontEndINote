package com.codegym.inote.controller;

import com.codegym.inote.model.Note;
import com.codegym.inote.model.NoteType;
import com.codegym.inote.service.NoteService;
import com.codegym.inote.service.NoteTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/noteType")
public class NoteTypeController {

    @Autowired
    private NoteTypeService noteTypeService;

    @Autowired
    private NoteService noteService;

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

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        NoteType noteType = noteTypeService.findById(id);
        if (noteType != null) {
            ModelAndView modelAndView = new ModelAndView("/noteType/edit");
            modelAndView.addObject("noteType", noteType);
            return modelAndView;
        }
        return new ModelAndView("/error-404");
    }

    @PostMapping("/edit")
    public ModelAndView editNoteType(@ModelAttribute NoteType noteType) {
        noteTypeService.save(noteType);

        ModelAndView modelAndView = new ModelAndView("/noteType/edit");
        modelAndView.addObject("noteType", noteType);
        modelAndView.addObject("message", "Updated!");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        NoteType noteType = noteTypeService.findById(id);
        if (noteType != null) {
            ModelAndView modelAndView = new ModelAndView("/noteType/delete");
            modelAndView.addObject("noteType", noteType);
            return modelAndView;
        }
        return new ModelAndView("/error-404");
    }

    @PostMapping("/delete")
    public String deleteNoteType(@ModelAttribute NoteType noteType) {
        noteTypeService.remove(noteType.getId());
        return "redirect:/noteType/noteTypeList";
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewNoteType(@PathVariable Long id, Pageable pageable) {
        NoteType noteType = noteTypeService.findById(id);
        if (noteType == null) {
            return new ModelAndView("/error-404");
        }
        Page<Note> notes = noteService.findAllByNoteType(noteType, pageable);
        ModelAndView modelAndView = new ModelAndView("/noteType/view");
        modelAndView.addObject("noteType", noteType);
        modelAndView.addObject("notes", notes);
        return modelAndView;
    }
}
