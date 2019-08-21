package com.codegym.inote.service;

import com.codegym.inote.model.NoteType;
import com.codegym.inote.model.Stack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteTypeService extends GeneralService<NoteType> {
    Page<NoteType> findNoteTypeByName(String name, Pageable pageable);

    Page<NoteType> findNoteTypeByStack(Stack stack, Pageable pageable);

}
