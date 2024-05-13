package com.prosilion.presto.controller;

import com.prosilion.presto.model.dto.ExampleLdapUserDto;
import com.prosilion.presto.service.ExampleLdapUserService;
import java.lang.reflect.InvocationTargetException;
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

@Controller
public class EditLdapUserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EditLdapUserController.class);
	private final ExampleLdapUserService exampleLdapUserService;

	@Autowired
	public EditLdapUserController(ExampleLdapUserService exampleLdapUserService) {
		this.exampleLdapUserService = exampleLdapUserService;
	}

	@GetMapping("/welcome")
	public String welcome(Model model) {
		return "jsp/welcome";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(Model model, @PathVariable("id") Long id)
			throws InvocationTargetException, IllegalAccessException {
		model.addAttribute("user", exampleLdapUserService.findById(id).convertToDto());
		return "thymeleaf/edit";
	}

	@PostMapping("/edit")
	public String updateUser(@ModelAttribute("user") ExampleLdapUserDto exampleLdapUserDto, BindingResult result, Model model) {

		if (result.hasErrors()) {
			LOGGER.info("User [{}] returned with with following binding errors:", result.getFieldErrors());
			model.addAttribute("user", exampleLdapUserDto);
			return "redirect:/edit";
		}

		try {
			ExampleLdapUserDto updatedExampleLdapUserDto = exampleLdapUserService.update(exampleLdapUserDto);
			model.addAttribute("user", updatedExampleLdapUserDto);
			return "redirect:/users";
		} catch (InvocationTargetException | IllegalAccessException e) {
			LOGGER.info("User [{}] InvocationTarget / IllegalAccess exception.");
			model.addAttribute("user", exampleLdapUserDto);
			return "redirect:/users";
		}
	}
}
