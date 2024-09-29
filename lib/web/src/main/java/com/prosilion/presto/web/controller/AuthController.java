package com.prosilion.presto.web.controller;

import com.prosilion.presto.web.model.AppUserDtoIF;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public interface AuthController {

  @GetMapping("/register")
  String showRegistrationForm(Model model);

  @PostMapping("/register")
  String registration(@ModelAttribute("user") AppUserDtoIF appUserDtoIF, BindingResult result, Model model);
}
