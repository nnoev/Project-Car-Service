package com.example.car_service.web.controllers;

import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.service.UserService;
import com.example.car_service.web.dtos.LoginRequest;
import com.example.car_service.web.dtos.UserRegistration;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView getLogin(@RequestParam(name = "error",required = false) String error) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());
        if (error != null) {
            modelAndView.addObject("error", "Invalid username or password");
        }
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("userRegistration", new UserRegistration());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView postRegister(@Valid UserRegistration userRegistration, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }
        userService.registerUser(userRegistration);
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/vehicles")
    public ModelAndView getVehicles(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(principal.getId());
        modelAndView.setViewName("vehicles");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/service-records")
    public ModelAndView getServiceRecords(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(principal.getId());
        modelAndView.setViewName("service-records");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/reminders")
    public ModelAndView getReminders(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(principal.getId());
        modelAndView.setViewName("reminders");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

}
