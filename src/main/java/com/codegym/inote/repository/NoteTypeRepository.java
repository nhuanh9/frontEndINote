package com.codegym.inote.repository;

import com.codegym.inote.model.NoteType;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NoteTypeRepository extends PagingAndSortingRepository<NoteType,Long> {
}
