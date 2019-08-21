package com.codegym.inote.service;

import com.codegym.inote.model.Note;
import com.codegym.inote.model.Tag;

public interface TagService extends GeneralService<Tag> {
    Iterable<Tag> findAllByNotes(Note note);
}
