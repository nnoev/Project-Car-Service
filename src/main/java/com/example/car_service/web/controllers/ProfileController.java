package com.example.car_service.web.controllers;

import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.service.UserService;
import com.example.car_service.web.dtos.ChangePassword;
import com.example.car_service.web.dtos.ChangeProfile;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView("profile");
        User user = userService.getById(principal.getId());
        modelAndView.addObject("user", user);
        modelAndView.addObject("changeProfile", new ChangeProfile());
        modelAndView.addObject("changePassword", new ChangePassword());
        modelAndView.addObject("activeTab", "overview");
        return modelAndView;
    }

    @GetMapping("/profile/change-password")
    public ModelAndView getChangePassword(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView("profile");
        User user = userService.getById(principal.getId());
        modelAndView.addObject("user", user);
        modelAndView.addObject("changePassword", new ChangePassword());
        modelAndView.addObject("changeProfile", new ChangeProfile()); // REQUIRED
        modelAndView.addObject("activeTab", "change-password");
        return modelAndView;
    }

    @PostMapping("/profile/change-password")
    public ModelAndView changePassword(@Valid ChangePassword changePassword,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       @AuthenticationPrincipal UserData principal) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("activeTab", "change-password");
            modelAndView.addObject("changeProfile", new ChangeProfile());
            User user = userService.getById(principal.getId());
            modelAndView.addObject("user", user);
            modelAndView.addObject("changePassword", changePassword);
            return modelAndView;
        }
        User user = userService.getById(principal.getId());
        if (!passwordEncoder.matches(changePassword.getCurrentPassword(), user.getPassword())) {
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("activeTab", "change-password");
            modelAndView.addObject("changePassword", changePassword);
            modelAndView.addObject("changeProfile", new ChangeProfile());
            modelAndView.addObject("user", user);
            modelAndView.addObject("passwordError", "Current password is incorrect");
            return modelAndView;
        }
        if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("activeTab", "change-password");
            modelAndView.addObject("changePassword", changePassword);
            modelAndView.addObject("changeProfile", new ChangeProfile());
            modelAndView.addObject("user", user);
            modelAndView.addObject("passwordError", "Passwords do not match");
            return modelAndView;
        }
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "Password changed successfully");
        return new ModelAndView("redirect:/profile");
    }

    @GetMapping("/profile/edit")
    public ModelAndView getEditProfile(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView("profile");
        User user = userService.getById(principal.getId());
        ChangeProfile changeProfile = new ChangeProfile();
        changeProfile.setFirstName(user.getFirstName());
        changeProfile.setLastName(user.getLastName());
        changeProfile.setEmail(user.getEmail());
        modelAndView.addObject("user", user);
        modelAndView.addObject("changeProfile", changeProfile);
        modelAndView.addObject("changePassword", new ChangePassword());
        modelAndView.addObject("activeTab", "edit-profile");
        return modelAndView;
    }

    @PostMapping("/profile/edit")
    public ModelAndView changeProfile(@Valid ChangeProfile changeProfile,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      @AuthenticationPrincipal UserData principal) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("activeTab", "edit-profile");
            modelAndView.addObject("changeProfile", changeProfile);
            User user = userService.getById(principal.getId());
            modelAndView.addObject("user", user);
            modelAndView.addObject("changePassword", new ChangePassword());
            return modelAndView;
        }
        User user = userService.getById(principal.getId());
        user.setFirstName(changeProfile.getFirstName());
        user.setLastName(changeProfile.getLastName());
        user.setEmail(changeProfile.getEmail());
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully");
        return new ModelAndView("redirect:/profile");
    }

}
