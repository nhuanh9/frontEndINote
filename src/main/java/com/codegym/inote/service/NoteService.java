package com.codegym.inote.service;

import com.codegym.inote.model.Note;
import com.codegym.inote.model.NoteType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService extends GeneralService<Note> {
    Page<Note> findAllByNoteType(NoteType noteType, Pageable pageable);
}
