package com.autolog.controller;

import com.autolog.dto.ChangePasswordDTO;
import com.autolog.dto.ProfileUpdateDTO;
import com.autolog.model.User;
import com.autolog.service.SettingsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@SessionAttributes("user")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    private User getUser(Model model, Principal principal) {
        User user = (User) model.getAttribute("user");
        if (user == null && principal != null) {
            user = settingsService.getCurrentUser(principal.getName());
            model.addAttribute("user", user);
        }
        return user;
    }

    @GetMapping("/settings")
    public String settings(Model model, Principal principal) {
        User user = getUser(model, principal);

        model.addAttribute("profileDTO", new ProfileUpdateDTO(user.getUsername(), user.getEmail()));
        model.addAttribute("passwordDTO", new ChangePasswordDTO());

        return "settings/form";
    }

    @PostMapping("/settings/profile")
    public String updateProfile(@Valid @ModelAttribute("profileDTO") ProfileUpdateDTO dto,
                                BindingResult result,
                                Model model,
                                Principal principal) {

        User user = getUser(model, principal);
        model.addAttribute("passwordDTO", new ChangePasswordDTO());

        if (result.hasErrors()) {
            return "settings/form";
        }

        try {
            settingsService.updateProfile(user, dto.getUsername(), dto.getEmail());
            model.addAttribute("successProfile", "Perfil actualizado correctamente");
            model.addAttribute("user", user); 
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorProfile", e.getMessage());
        }

        return "settings/form";
    }

    @PostMapping("/settings/password")
    public String changePassword(@Valid @ModelAttribute("passwordDTO") ChangePasswordDTO dto,
                                 BindingResult result,
                                 Model model,
                                 Principal principal,
                                 HttpServletRequest request) {

        User user = getUser(model, principal);
        model.addAttribute("profileDTO", new ProfileUpdateDTO(user.getUsername(), user.getEmail()));

        if (result.hasErrors()) {
            return "settings/form";
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "settings/form";
        }

        try {
            settingsService.changePassword(user, dto.getOldPassword(), dto.getNewPassword());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "settings/form";
        }

        try {
            request.logout(); 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/login?passwordChanged";
    }
}
