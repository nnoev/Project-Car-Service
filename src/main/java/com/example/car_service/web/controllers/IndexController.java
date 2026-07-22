package com.example.car_service.web.controllers;

import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView getDashboard(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard");
        User user = userService.getById(principal.getId());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/guest-login")
    public ModelAndView guestLogin(HttpSession session) {
        User guest = userService.getByUsername("guest");
        session.setAttribute("userId", guest.getId());
        return new ModelAndView("redirect:/dashboard");
    }

}
