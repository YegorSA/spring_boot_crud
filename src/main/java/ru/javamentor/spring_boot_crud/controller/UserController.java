package ru.javamentor.spring_boot_crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javamentor.spring_boot_crud.model.Role;
import ru.javamentor.spring_boot_crud.service.UserService;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public String redirect(Principal principal) {
		long id = userService.findByUsername(principal.getName()).getId();
		return "redirect:/user/" + id;
	}

	@GetMapping("/{id}")
	public String showUser(@PathVariable("id") long id, ModelMap model, Principal principal) {
		if (Objects.equals(userService.getUserById(id).getUsername(), principal.getName()) || userService.findByUsername(principal.getName()).getRoles().stream().map(Role::getName).anyMatch(a -> a.equals("ROLE_ADMIN"))) {
			model.addAttribute("user", userService.getUserById(id));
			model.addAttribute("userRoles", userService.getUserById(id).getRoles());
			return "user/userByID";
		} else {
			return "user/failPage";
		}
	}


}