package com.codegym.inote.controller;

import com.codegym.inote.model.Note;
import com.codegym.inote.model.NoteType;
import com.codegym.inote.model.Tag;
import com.codegym.inote.service.NoteService;
import com.codegym.inote.service.NoteTypeService;
import com.codegym.inote.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/note")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteTypeService noteTypeService;

    @Autowired
    private TagService tagService;

    @ModelAttribute("noteTypes")
    public Page<NoteType> noteTypes(Pageable pageable) {
        return noteTypeService.findAll(pageable);
    }

    @ModelAttribute("tags")
    public Page<Tag> tags(Pageable pageable) {
        return tagService.findAll(pageable);
    }

    @GetMapping("/notes")
    public ModelAndView showNoteList(Pageable pageable, @RequestParam("search") Optional<String> search) {
        Page<Note> notes;

        if (search.isPresent()) {
            notes = noteService.findNoteByTitleContains(search.get(), pageable);
        } else {
            notes = noteService.findAll(pageable);
        }

        ModelAndView modelAndView = new ModelAndView("/note/list");
        modelAndView.addObject("notes", notes);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/note/create");
        modelAndView.addObject("note", new Note());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveNoteType(@ModelAttribute Note note) {
        noteService.save(note);

        ModelAndView modelAndView = new ModelAndView("/note/create");
        modelAndView.addObject("note", new Note());
        modelAndView.addObject("message", "Created!");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Note note = noteService.findById(id);
        if (note != null) {
            ModelAndView modelAndView = new ModelAndView("/note/edit");
            modelAndView.addObject("note", note);
            return modelAndView;
        }
        return new ModelAndView("/error-404");
    }

    @PostMapping("/edit")
    public ModelAndView editNoteType(@ModelAttribute Note note) {
        noteService.save(note);

        ModelAndView modelAndView = new ModelAndView("/note/edit");
        modelAndView.addObject("note", note);
        modelAndView.addObject("message", "Updated!");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Note note = noteService.findById(id);
        if (note != null) {
            ModelAndView modelAndView = new ModelAndView("/note/delete");
            modelAndView.addObject("note", note);
            return modelAndView;
        }
        return new ModelAndView("/error-404");
    }

    @PostMapping("/delete")
    public String deleteNoteType(@ModelAttribute Note note) {
        noteService.remove(note.getId());
        return "redirect:/user/note/notes";
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewNote(@PathVariable Long id, Pageable pageable) {
        Note note = noteService.findById(id);
        if (note == null) {
            return new ModelAndView("/error-404");
        }

        List<Tag> tags;
        tags = (List<Tag>) tagService.findAllByNotes(note);
        note.setTags(tags);

        ModelAndView modelAndView = new ModelAndView("/note/view");
        modelAndView.addObject("note", note);
        return modelAndView;
    }
}
