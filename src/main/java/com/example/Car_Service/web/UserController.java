package com.example.Car_Service.web;

import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.service.UserService;
import com.example.Car_Service.web.dtos.ChangePasswordRequest;
import com.example.Car_Service.web.dtos.ChangeProfileRequest;
import com.example.Car_Service.web.dtos.LoginRequest;
import com.example.Car_Service.web.dtos.UserRegistration;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ModelAndView getLogin() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView postLogin(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("login");
        }
        User user = userService.login(loginRequest);
        session.setAttribute("userId", user.getId());
        return new ModelAndView("redirect:/dashboard");
    }

    @PostMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/index");
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
        userService.getByUserName(userRegistration.getUsername());
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        Object userId = session.getAttribute("userId");
        User user = userService.getById((UUID) userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("changePassword", new ChangePasswordRequest());
        modelAndView.addObject("changeProfile", new ChangeProfileRequest());
        return modelAndView;
    }

    @GetMapping("/vehicles")
    public ModelAndView getVehicles(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        User user = userService.getById((UUID) userId);
        modelAndView.setViewName("vehicles");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
    @GetMapping("/service-records")
    public ModelAndView getServiceRecords(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        User user = userService.getById((UUID) userId);
        modelAndView.setViewName("service-records");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
    @GetMapping("/reminders")
    public ModelAndView getReminders(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        User user = userService.getById((UUID) userId);
        modelAndView.setViewName("reminders");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
