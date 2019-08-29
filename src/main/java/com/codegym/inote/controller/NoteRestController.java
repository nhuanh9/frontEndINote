package com.codegym.inote.controller;

import com.codegym.inote.model.Note;
import com.codegym.inote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/restful/note")
public class NoteRestController {

    @Autowired
    private NoteService noteService;


    @GetMapping("/notes")
    public ResponseEntity<Page<Note>> showAllNote(Pageable pageable) {
        Page<Note> notes = noteService.findAll(pageable);
        if (notes.getTotalElements() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @GetMapping(value = "/notes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Note> getNote(@PathVariable Long id) {
        Note note = noteService.findById(id);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @PostMapping(value = "/notes",produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Void> createNote(@RequestBody Note note, UriComponentsBuilder ucBuilder) {
        noteService.save(note);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/note/{id}").buildAndExpand(note.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note) {
        Note currentNote = noteService.findById(id);
        if (currentNote == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        currentNote.setTitle(note.getTitle());
        currentNote.setContent(note.getContent());
        currentNote.setUser(note.getUser());
        currentNote.setTime(note.getTime());
        currentNote.setNoteType(note.getNoteType());
        currentNote.setTags(note.getTags());
        noteService.save(currentNote);
        return new ResponseEntity<>(currentNote, HttpStatus.OK);
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Note> deleteNote(@PathVariable Long id) {
        Note note = noteService.findById(id);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        noteService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
