package com.codegym.inote.repository;

import com.codegym.inote.model.Note;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NoteRepository extends PagingAndSortingRepository<Note,Long> {
}
