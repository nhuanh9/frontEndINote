package com.codegym.inote.service.impl;

import com.codegym.inote.model.Note;
import com.codegym.inote.repository.NoteRepository;
import com.codegym.inote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class NoteServiceImpl implements NoteService {
    @Autowired
    NoteRepository noteRepository;

    @Override
    public Page<Note> findAll(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }

    @Override
    public Note findById(Long id) {
        return noteRepository.findOne(id);
    }

    @Override
    public void save(Note model) {
        noteRepository.save(model);
    }

    @Override
    public void remove(Long id) {
        noteRepository.delete(id);
    }
}
