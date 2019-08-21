package com.codegym.inote.service;

import com.codegym.inote.model.Note;
import com.codegym.inote.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService extends GeneralService<Tag> {
    Iterable<Tag> findAllByNotes(Note note);

    Page<Tag> findTagByName(String name, Pageable pageable);

}
