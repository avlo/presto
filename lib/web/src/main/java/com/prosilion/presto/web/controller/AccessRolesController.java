package com.prosilion.presto.web.controller;

import lombok.NoArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@NoArgsConstructor
@Controller
public class AccessRolesController implements ErrorController {

  @Secured({ "ROLE_ANONYMOUS", "ANONYMOUS","ROLE_USER", "USER"})
  @GetMapping("/login")
  public String login() {
    return "thymeleaf/login";
  }

  /**
   * Default error handler.  Customizations capable via any of the following:
   * - application.properties handler page:
   * server.error.path=/custom_error_page
   * or
   * - create a customized error handler / override below method
   * or
   * - change error handler below
   *
   * @return redirect to login page
   */
  @GetMapping("/error")
  public String handleError() {
    return "redirect:/login";
  }
}