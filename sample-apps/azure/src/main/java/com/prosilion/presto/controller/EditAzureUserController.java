package com.prosilion.presto.controller;

import com.prosilion.presto.model.dto.ExampleAzureUserDto;
import com.prosilion.presto.service.ExampleAzureUserService;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EditAzureUserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EditAzureUserController.class);
	private final ExampleAzureUserService exampleAzureUserService;

	@Autowired
	public EditAzureUserController(@NonNull ExampleAzureUserService exampleAzureUserService) {
		this.exampleAzureUserService = exampleAzureUserService;
	}

	@GetMapping("/")
	public String successfulLoginRedirect() {
		return "redirect:/users";
	}

	@GetMapping("/welcome")
	public String welcome(Model model) {
		return "jsp/welcome";
	}

	@Secured({ "ROLE_ANONYMOUS", "ANONYMOUS","ROLE_USER", "USER"})
	@GetMapping("/edit/{id}")
	public String showEditForm(Model model, @PathVariable("id") Long id)
			throws InvocationTargetException, IllegalAccessException {
		model.addAttribute("user", exampleAzureUserService.findById(id).convertToDto());
		return "thymeleaf/edit";
	}

	@Secured({ "ROLE_ANONYMOUS", "ANONYMOUS","ROLE_USER", "USER"})
	@PostMapping("/edit")
	public String updateUser(@ModelAttribute("user") ExampleAzureUserDto exampleAzureUserDto, BindingResult result, Model model) {

		if (result.hasErrors()) {
			LOGGER.info("User [{}] returned with with following binding errors:", result.getFieldErrors());
			model.addAttribute("user", exampleAzureUserDto);
			return "redirect:/edit";
		}

		try {
			ExampleAzureUserDto updatedExampleAzureUserDto = exampleAzureUserService.update(
					exampleAzureUserDto);
			model.addAttribute("user", updatedExampleAzureUserDto);
			return "redirect:/users";
		} catch (InvocationTargetException | IllegalAccessException e) {
			LOGGER.info("User [{}] InvocationTarget / IllegalAccess exception.");
			model.addAttribute("user", exampleAzureUserDto);
			return "redirect:/users";
		}
	}
}
