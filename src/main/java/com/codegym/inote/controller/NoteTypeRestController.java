package com.codegym.inote.controller;

import com.codegym.inote.model.NoteType;
import com.codegym.inote.service.NoteTypeService;
import com.codegym.inote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restful")
public class NoteTypeRestController {

    @Autowired
    private NoteTypeService noteTypeService;

    @Autowired
    private UserService userService;

    @GetMapping("/noteTypes")
    public ResponseEntity<Page<NoteType>> showAllNoteType(Pageable pageable) {
        Page<NoteType> noteTypes = noteTypeService.findAllByUser(userService.getCurrentUser(), pageable);
        if (noteTypes.getTotalElements() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(noteTypes, HttpStatus.OK);
    }

    @GetMapping("/noteTypes/{id}")
    public ResponseEntity<NoteType> getNoteType(@PathVariable Long id) {
        NoteType noteType = noteTypeService.findById(id);
        if (noteType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(noteType, HttpStatus.CREATED);
    }

    @PostMapping("/noteTypes")
    public ResponseEntity<String> createNoteType(@RequestBody NoteType noteType) {
        noteType.setUser(userService.getCurrentUser());
        noteTypeService.save(noteType);
        return new ResponseEntity<String>("Created!", HttpStatus.CREATED);
    }

    @PutMapping("/noteTypes/{id}")
    public ResponseEntity<NoteType> updateNoteType(@PathVariable Long id, @RequestBody NoteType noteType) {
        NoteType currentNoteType = noteTypeService.findById(id);
        if (currentNoteType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentNoteType.setName(noteType.getName());
        currentNoteType.setNotes(noteType.getNotes());
        currentNoteType.setStack(noteType.getStack());
        currentNoteType.setUser(noteType.getUser());
        noteTypeService.save(currentNoteType);
        return new ResponseEntity<>(currentNoteType, HttpStatus.OK);
    }

    @DeleteMapping("/noteTypes/{id}")
    public ResponseEntity<NoteType> deleteNoteType(@PathVariable Long id) {
        NoteType noteType = noteTypeService.findById(id);
        if (noteType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        noteTypeService.remove(noteType.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
