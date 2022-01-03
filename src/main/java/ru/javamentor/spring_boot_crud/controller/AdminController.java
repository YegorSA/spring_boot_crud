package ru.javamentor.spring_boot_crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javamentor.spring_boot_crud.model.Role;
import ru.javamentor.spring_boot_crud.model.User;
import ru.javamentor.spring_boot_crud.service.RoleService;
import ru.javamentor.spring_boot_crud.service.UserService;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showUsers(ModelMap model) {
        model.addAttribute("users", userService.findAll());
        return "admin/userList";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable("id") long id, ModelMap model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("userRoles", userService.getUserById(id).getRoles());
        return "user/userByID";
    }

    @GetMapping("/create")
    public String showAddUser(ModelMap model) {
        Set<Role> allRoles = roleService.findAll();
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("user", new User());
        return "admin/addUser";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, ModelMap model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/addUser";
        } else {
            //user.setRoles();
            model.forEach((k, v) -> System.out.println(k + v));
            userService.create(user);
            return "redirect:/admin/";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, ModelMap model) {
        User user = userService.getUserById(id);
        user.setPassword("");
        Set<Role> allRoles = roleService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "admin/editUser";
    }

    @PatchMapping("/{id}/edit")
    public String update(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.findAll().stream().map(Role::toString).collect(Collectors.toSet()));
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/editUser";
        } else {
            userService.update(user);
            return "redirect:/admin";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

}
