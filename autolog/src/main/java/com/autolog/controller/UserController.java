package com.autolog.controller;

import com.autolog.model.Rol;
import com.autolog.model.User;
import com.autolog.service.RolService;
import com.autolog.service.UserService;
import com.autolog.validation.OnCreate;
import com.autolog.validation.OnUpdate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/users")
@SessionAttributes({ "user" })
public class UserController {

    private final UserService userService;
    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;


    public UserController(UserService userService, RolService rolService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("userAutolog", new User());
        model.addAttribute("roles", rolService.findAllActive());
        return "users/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("userAutolog", user);
        model.addAttribute("roles", rolService.findAllActive());
        return "users/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("userAutolog")
                       User formUser,
                       BindingResult result,
                       @RequestParam(required = false) Set<Integer> roleIds,
                       Model model) {

        if (formUser.getId() == null) {
            // CREATE
            result = validate(formUser, OnCreate.class, result);
        } else {
            // UPDATE
            result = validate(formUser, OnUpdate.class, result);
        }

        if (result.hasErrors()) {
            model.addAttribute("roles", rolService.findAllActive());
            return "users/form";
        }

        User user;

        if (formUser.getId() != null) {
            user = userService.findById(formUser.getId());

            user.setUsername(formUser.getUsername());
            user.setEmail(formUser.getEmail());
            user.setActiveRow(formUser.getActiveRow());

            if (formUser.getPassword() != null &&
                !formUser.getPassword().isEmpty()) {

                user.setPassword(passwordEncoder
                        .encode(formUser.getPassword()));
            }

        } else {
            user = formUser;
            user.setPassword(passwordEncoder
                    .encode(formUser.getPassword()));
        }

        user.getRoles().clear();

        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Rol> roles = rolService.findByIds(roleIds);
            user.getRoles().addAll(roles);
        }

        userService.save(user);

        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        userService.delete(id);
        return "redirect:/users";
    }
    
    private BindingResult validate(User user,
            Class<?> group,
            BindingResult result) {
	
    	return result;
	}
}
