package com.prosilion.presto.controller;

import com.prosilion.presto.model.dto.ExampleJpaUserDto;
import com.prosilion.presto.service.ExampleJpaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.InvocationTargetException;

@Controller
public class EditJpaUserController {
  private static final Logger LOGGER = LoggerFactory.getLogger(EditJpaUserController.class);
  private final ExampleJpaUserService exampleJpaUserService;

  @Autowired
  public EditJpaUserController(ExampleJpaUserService exampleJpaUserService) {
    this.exampleJpaUserService = exampleJpaUserService;
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(Model model, @PathVariable("id") Long id)
      throws InvocationTargetException, IllegalAccessException {
    model.addAttribute("user", exampleJpaUserService.findById(id).convertToDto());
    return "thymeleaf/edit";
  }

  @PostMapping("/edit")
  public String updateUser(@ModelAttribute("user") ExampleJpaUserDto exampleJpaUserDto, BindingResult result, Model model) {

    if (result.hasErrors()) {
      LOGGER.info("User [{}] returned with with following binding errors:", result.getFieldErrors());
      model.addAttribute("user", exampleJpaUserDto);
      return "redirect:/edit";
    }

    try {
      ExampleJpaUserDto updatedExampleJpaUserDto = exampleJpaUserService.update(exampleJpaUserDto);
      model.addAttribute("user", updatedExampleJpaUserDto);
      return "redirect:/users";
    } catch (InvocationTargetException | IllegalAccessException e) {
      LOGGER.info("User [{}] InvocationTarget / IllegalAccess exception.");
      model.addAttribute("user", exampleJpaUserDto);
      return "redirect:/users";
    }
  }
}
