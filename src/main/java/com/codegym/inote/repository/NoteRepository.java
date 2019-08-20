package com.codegym.inote.repository;

import com.codegym.inote.model.Note;
import com.codegym.inote.model.NoteType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NoteRepository extends PagingAndSortingRepository<Note, Long> {
    Page<Note> findAllByNoteType(NoteType noteType, Pageable pageable);
}
