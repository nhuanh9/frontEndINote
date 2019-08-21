package com.codegym.inote.service;

import com.codegym.inote.model.NoteType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteTypeService extends GeneralService<NoteType> {
    Page<NoteType> findNoteTypeByName(String name, Pageable pageable);
}
