package com.codegym.inote.controller;

import com.codegym.inote.model.Trash;
import com.codegym.inote.service.RecycleBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Trash trash = recycleBinService.findById(id);
        if (trash != null) {
            ModelAndView modelAndView = new ModelAndView("/recycleBin/delete");
            modelAndView.addObject("trash", trash);
            return modelAndView;
        }
        return new ModelAndView("/error-404");
    }

    @PostMapping("/delete")
    public String deleteNoteType(@ModelAttribute Trash trash) {
        recycleBinService.remove(trash.getId());
        return "redirect:/user/recycleBin/trashes";
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewNote(@PathVariable Long id) {
        Trash trash = recycleBinService.findById(id);
        if (trash == null) {
            return new ModelAndView("/error-404");
        }

        ModelAndView modelAndView = new ModelAndView("/recycleBin/view");
        modelAndView.addObject("trash", trash);
        return modelAndView;
    }
}
