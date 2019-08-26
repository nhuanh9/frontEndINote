package com.codegym.inote.controller;

import com.codegym.inote.model.NoteType;
import com.codegym.inote.model.Stack;
import com.codegym.inote.service.NoteTypeService;
import com.codegym.inote.service.StackService;
import com.codegym.inote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/stack")
public class StackController {
    @Autowired
    private StackService stackService;

    @Autowired
    private NoteTypeService noteTypeService;

    @Autowired
    private UserService userService;

    @GetMapping("/stacks")
    public ModelAndView showAllStack(Pageable pageable) {
        Page<Stack> stacks = stackService.findAllByUser(userService.getCurrentUser(), pageable);

        ModelAndView modelAndView = new ModelAndView("/stack/list");
        modelAndView.addObject("stacks", stacks);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/stack/create");
        modelAndView.addObject("stack", new Stack());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveStack(@ModelAttribute Stack stack) {
        stack.setUser(userService.getCurrentUser());
        stackService.save(stack);

        ModelAndView modelAndView = new ModelAndView("/stack/create");
        modelAndView.addObject("stack", new Stack());
        modelAndView.addObject("message", "Created!");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Stack stack = stackService.findById(id);
        if (stack != null) {
            ModelAndView modelAndView = new ModelAndView("/stack/edit");
            modelAndView.addObject("stack", stack);
            return modelAndView;
        }
        return new ModelAndView("/error-404");
    }

    @PostMapping("/edit")
    public ModelAndView updateStack(@ModelAttribute Stack stack) {
        stack.setUser(userService.getCurrentUser());
        stackService.save(stack);

        ModelAndView modelAndView = new ModelAndView("/stack/edit");
        modelAndView.addObject("stack", stack);
        modelAndView.addObject("message", "Updated!");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Stack stack = stackService.findById(id);
        if (stack != null) {
            ModelAndView modelAndView = new ModelAndView("/stack/delete");
            modelAndView.addObject("stack", stack);
            return modelAndView;
        }
        return new ModelAndView("/error-404");
    }

    @PostMapping("/delete")
    public String deleteTag(@ModelAttribute Stack stack) {
        stackService.remove(stack.getId());
        return "redirect:/user/stack/stacks";
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewTag(@PathVariable Long id, Pageable pageable) {
        Stack stack = stackService.findById(id);
        if (stack == null) {
            return new ModelAndView("/error-404");
        }
        Page<NoteType> noteTypes = noteTypeService.findNoteTypeByStack(stack, pageable);
        ModelAndView modelAndView = new ModelAndView("/stack/view");
        modelAndView.addObject("stack", stack);
        modelAndView.addObject("noteTypes", noteTypes);
        return modelAndView;
    }
}
