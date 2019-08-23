package com.codegym.inote.controller;

import com.codegym.inote.model.Trash;
import com.codegym.inote.service.RecycleBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/recycleBin")
public class RecycleBinController {

    @Autowired
    private RecycleBinService recycleBinService;

    @GetMapping("/trashes")
    public ModelAndView recycleBin(Pageable pageable) {
        Page<Trash> trashes = recycleBinService.findAll(pageable);

        ModelAndView modelAndView = new ModelAndView("/recycleBin/list");
        modelAndView.addObject("trashes", trashes);
        return modelAndView;
    }
}
