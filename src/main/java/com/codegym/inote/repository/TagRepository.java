package com.codegym.inote.repository;

import com.codegym.inote.model.Note;
import com.codegym.inote.model.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
    Iterable<Tag> findAllByNotes(Note note);
}
