package com.codegym.inote.repository;

import com.codegym.inote.model.Trash;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecylceBinRepository extends PagingAndSortingRepository<Trash, Long> {
}
