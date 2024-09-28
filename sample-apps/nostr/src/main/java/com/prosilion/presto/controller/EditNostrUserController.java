package com.prosilion.presto.controller;

import com.prosilion.presto.model.dto.ExampleNostrUserDto;
import com.prosilion.presto.service.ExampleNostrUserService;
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
public class EditNostrUserController {
  private static final Logger LOGGER = LoggerFactory.getLogger(EditNostrUserController.class);
  private final ExampleNostrUserService exampleNostrUserService;

  @Autowired
  public EditNostrUserController(ExampleNostrUserService exampleNostrUserService) {
    this.exampleNostrUserService = exampleNostrUserService;
  }

  @GetMapping("/welcome")
  public String welcome(Model model) {
    return "jsp/welcome";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(Model model, @PathVariable("id") Long id)
      throws InvocationTargetException, IllegalAccessException {
    model.addAttribute("user", exampleNostrUserService.findById(id).convertToDto());
    return "thymeleaf/edit";
  }

  @PostMapping("/edit")
  public String updateUser(@ModelAttribute("user") ExampleNostrUserDto exampleNostrUserDto, BindingResult result, Model model) {

    if (result.hasErrors()) {
      LOGGER.info("User [{}] returned with with following binding errors:", result.getFieldErrors());
      model.addAttribute("user", exampleNostrUserDto);
      return "redirect:/edit";
    }

    try {
      ExampleNostrUserDto updatedExampleNostrUserDto = exampleNostrUserService.update(exampleNostrUserDto);
      model.addAttribute("user", updatedExampleNostrUserDto);
      return "redirect:/users";
    } catch (InvocationTargetException | IllegalAccessException e) {
      LOGGER.info("User [{}] InvocationTarget / IllegalAccess exception.");
      model.addAttribute("user", exampleNostrUserDto);
      return "redirect:/users";
    }
  }
}
