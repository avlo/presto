package com.prosilion.presto.nostr.controller;

import com.prosilion.presto.nostr.dto.model.NostrAppUserDto;
import com.prosilion.presto.nostr.service.NostrAuthUserService;
import com.prosilion.presto.security.PreExistingUserException;
import com.prosilion.presto.security.entity.AppUserAuthUser;
import com.prosilion.presto.web.controller.AuthController;
import com.prosilion.presto.web.model.AppUserDto;
import com.prosilion.presto.web.model.AppUserDtoIF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
public class NostrAuthController implements AuthController {
  private final NostrAuthUserService nostrAuthUserService;

  public NostrAuthController(NostrAuthUserService nostrAuthUserService) {
    this.nostrAuthUserService = nostrAuthUserService;
  }

  @Override
  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    NostrAppUserDto user = new NostrAppUserDto(new AppUserDto());
    model.addAttribute("user", user);
    return "thymeleaf/register-nostr";
  }

  @PostMapping("/register")
  public String registration(@ModelAttribute("user") AppUserDtoIF nostrAppUserDto, BindingResult result, Model model) {
//    public String registration(@ModelAttribute("user") NostrAppUserDtoIF nostrAppUserDto, BindingResult result, Model model) {

    if (result.hasErrors()) {
      log.info("User [{}] returned with with binding errors.", result.getFieldErrors());
      model.addAttribute("user", nostrAppUserDto);
      return "redirect:/register";
    }

    try {
      AppUserAuthUser appUserAuthUser = nostrAuthUserService.createUser(
          nostrAppUserDto.getUsername(),
          nostrAppUserDto.getPassword(),
//          nostrAppUserDto.getPubKey()
          "NEEDS FIX"
      );
      log.info("Registered AppUserAuthUser [{}]", appUserAuthUser.getAuthUserName());
      model.addAttribute("user", nostrAppUserDto);
      return "redirect:/login";
    } catch (PreExistingUserException e) {
      log.info("User [{}] already exists.", nostrAppUserDto.getUsername());
      model.addAttribute("user", nostrAppUserDto);
      return "redirect:/login";
    }
  }
}
