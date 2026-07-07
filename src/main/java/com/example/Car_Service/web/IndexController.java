package com.example.Car_Service.web;

import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.property.UserProperties;
import com.example.Car_Service.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class IndexController {

    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/dashboard")
    public ModelAndView getDashboard(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard");
        UUID userId = (UUID) session.getAttribute("userId");
        User user = userService.getById(userId);
        if (user != null) {
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

}
