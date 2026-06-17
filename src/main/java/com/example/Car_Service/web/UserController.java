package com.example.Car_Service.web;

import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ModelAndView getProfile(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        User user = userService.getById(id);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

}
