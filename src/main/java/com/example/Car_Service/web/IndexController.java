package com.example.Car_Service.web;


import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.property.UserProperties;
import com.example.Car_Service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private final UserService userService;

    private final UserProperties userProperties;

    @Autowired
    public IndexController(UserService userService, UserProperties userProperties) {
        this.userService = userService;
        this.userProperties = userProperties;
    }

    @GetMapping("/")
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @GetMapping("/dashboard")
    public ModelAndView getDashboard() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard");
        User user = userService.getByUserName(userProperties.getDefaultUser().getUsername());
        if (user != null) {

        modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

}
