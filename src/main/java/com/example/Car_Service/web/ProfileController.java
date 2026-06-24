package com.example.Car_Service.web;

import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.property.UserProperties;
import com.example.Car_Service.user.service.UserService;
import com.example.Car_Service.web.dtos.ChangePasswordRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class ProfileController {

    private final UserService UserService;

    private final UserProperties userProperties;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService, UserProperties userProperties, PasswordEncoder passwordEncoder) {
        UserService = userService;
        this.userProperties = userProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/profile/change-password")
    public ModelAndView changePassword(@Valid ChangePasswordRequest changePasswordRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("profile");
        }
        Object userId = session.getAttribute("userId");
        User user = UserService.getById((UUID) userId);
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())){
            return new ModelAndView("profile");
        }
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())){
            return new ModelAndView("profile");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        UserService.save(user);
        return new ModelAndView("redirect:/profile");

    }

}
