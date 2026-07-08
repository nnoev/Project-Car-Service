package com.example.car_service.web.controllers;

import com.example.car_service.user.model.User;
import com.example.car_service.user.service.UserService;
import com.example.car_service.web.dtos.ChangePassword;
import com.example.car_service.web.dtos.ChangeProfile;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class ProfileController {

    private final UserService UserService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService, PasswordEncoder passwordEncoder) {
        UserService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/profile/change-password")
    public ModelAndView changePassword(@Valid ChangePassword changePassword,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       HttpSession session) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("activeTab", "change-password");
            modelAndView.addObject("changeProfile", new ChangeProfile());
            User user = UserService.getById((UUID) session.getAttribute("userId"));
            modelAndView.addObject("user", user);
            modelAndView.addObject("changePassword", changePassword);
            return modelAndView;
        }
        Object userId = session.getAttribute("userId");
        User user = UserService.getById((UUID) userId);
        if (!passwordEncoder.matches(changePassword.getCurrentPassword(), user.getPassword())) {
            return new ModelAndView("profile");
        }
        if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
            return new ModelAndView("profile");
        }
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        UserService.save(user);
        redirectAttributes.addFlashAttribute("message", "Password changed successfully");
        return new ModelAndView("redirect:/profile");
    }
@PostMapping("/profile/edit")
    public ModelAndView changeProfile(@Valid ChangeProfile changeProfile,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      HttpSession session) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("activeTab", "edit-profile");
            modelAndView.addObject("changeProfile", changeProfile);
            User user = UserService.getById((UUID) session.getAttribute("userId"));
            modelAndView.addObject("user", user);
            modelAndView.addObject("changePassword", new ChangePassword());
            return modelAndView;
        }
        Object userId = session.getAttribute("userId");
        User user = UserService.getById((UUID) userId);
        user.setFirstName(changeProfile.getFirstName());
        user.setLastName(changeProfile.getLastName());
        user.setEmail(changeProfile.getEmail());
        UserService.save(user);
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully");
        return new ModelAndView("redirect:/profile");
    }

}
