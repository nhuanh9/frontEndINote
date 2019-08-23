package com.codegym.inote.service.impl;

import com.codegym.inote.model.Trash;
import com.codegym.inote.repository.RecylceBinRepository;
import com.codegym.inote.service.RecycleBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class RecycleBinServiceImpl implements RecycleBinService {

    @Autowired
    private RecylceBinRepository recylceBinRepository;

    @Override
    public Page<Trash> findAll(Pageable pageable) {
        return recylceBinRepository.findAll(pageable);
    }

    @Override
    public Trash findById(Long id) {
        return recylceBinRepository.findOne(id);
    }

    @Override
    public void save(Trash model) {
        recylceBinRepository.save(model);
    }

    @Override
    public void remove(Long id) {
        recylceBinRepository.delete(id);
    }
}
