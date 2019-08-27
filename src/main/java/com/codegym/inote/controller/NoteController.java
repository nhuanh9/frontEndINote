package com.codegym.inote.controller;

import com.codegym.inote.model.*;
import com.codegym.inote.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/note")
public class NoteController {
    public static final String ERROR_404 = "/error-404";
    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteTypeService noteTypeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private RecycleBinService recycleBinService;

    @Autowired
    private UserService userService;

    @ModelAttribute("noteTypes")
    public Page<NoteType> noteTypes(Pageable pageable) {
        return noteTypeService.findAllByUser(userService.getCurrentUser(), pageable);
    }

    @ModelAttribute("tags")
    public Page<Tag> tags(Pageable pageable) {
        return tagService.findAllByUser(userService.getCurrentUser(), pageable);
    }

    @GetMapping("/notes")
    public ModelAndView showNoteList(Pageable pageable, @RequestParam("search") Optional<String> search) {
        Page<Note> notes;

        if (search.isPresent()) {
            notes = noteService.findNoteByTitleContains(search.get(), pageable);
        } else {
            notes = noteService.findAllByUser(userService.getCurrentUser(), pageable);
        }

        ModelAndView modelAndView = new ModelAndView("/note/list");
        modelAndView.addObject("notes", notes);
        modelAndView.addObject("user",userService.getCurrentUser());
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
        note.setUser(userService.getCurrentUser());
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        note.setTime(date);
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
        return new ModelAndView(ERROR_404);
    }

    @PostMapping("/edit")
    public ModelAndView editNoteType(@ModelAttribute Note note) {
        note.setUser(userService.getCurrentUser());
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        note.setTime(date);
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
        return new ModelAndView(ERROR_404);
    }

    @PostMapping("/delete")
    public String deleteNoteType(@ModelAttribute Note note) {
        Note currentNote = noteService.findById(note.getId());
        recycleBinService.addNoteToRecycleBin(currentNote);
        return "redirect:/user/note/notes";
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewNote(@PathVariable Long id) {
        Note note = noteService.findById(id);
        if (note == null) {
            return new ModelAndView(ERROR_404);
        }

        List<Tag> tags;
        tags = (List<Tag>) tagService.findAllByNotes(note);
        note.setTags(tags);

        ModelAndView modelAndView = new ModelAndView("/note/view");
        modelAndView.addObject("note", note);
        return modelAndView;
    }
}
