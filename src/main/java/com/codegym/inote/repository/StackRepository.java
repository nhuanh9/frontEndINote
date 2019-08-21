package com.codegym.inote.repository;

import com.codegym.inote.model.Stack;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StackRepository extends PagingAndSortingRepository<Stack, Long> {
}
