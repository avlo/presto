package com.prosilion.presto.jpa.controller;

import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.security.service.AuthUserService;
import com.prosilion.presto.web.controller.AuthController;
import com.prosilion.presto.web.model.AppUserDto;
import com.prosilion.presto.web.model.AppUserDtoIF;
import com.prosilion.presto.web.model.NostrAppUserDtoIF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
public class JpaAuthController implements AuthController {
  private final AuthUserService authUserService;

  public JpaAuthController(AuthUserService authUserService) {
    this.authUserService = authUserService;
  }

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    AppUserDtoIF user = new AppUserDto();
    model.addAttribute("user", user);
    return "thymeleaf/register";
  }

  @PostMapping("/register")
  public String registration(@ModelAttribute("user") AppUserDtoIF appUserDto, BindingResult result, Model model) {

    if (result.hasErrors()) {
      log.info("User [{}] returned with with binding errors.", result.getFieldErrors());
      model.addAttribute("user", appUserDto);
      return "redirect:/register";
    }

    try {
      AppUserAuthUser appUserAuthUser = authUserService.createUser(appUserDto.getUsername(), appUserDto.getPassword());
      log.info("Registered AppUserAuthUser [{}]", appUserAuthUser.getAuthUserName());
      model.addAttribute("user", appUserDto);
      return "redirect:/login";
    } catch (PreExistingUserException e) {
      log.info("User [{}] already exists.", appUserDto.getUsername());
      model.addAttribute("user", appUserDto);
      return "redirect:/login";
    }
  }

  //  TODO: remove below
  @Override
  public String registration(NostrAppUserDtoIF nostrAppUserDtoIF, BindingResult result, Model model) {
    return null;
  }
}
