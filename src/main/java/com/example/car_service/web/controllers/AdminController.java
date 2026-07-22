package com.example.car_service.web.controllers;

import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ModelAndView getUsers() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-users");
        modelAndView.addObject("users", userService.getAllUsers());
        return modelAndView;
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(
            @PathVariable UUID id,
            @RequestParam UserRole role
    ) {
        userService.changeRole(id, role);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable UUID id
    ) {
        userService.deleteUser(
                userService.getById(id)
        );
        return "redirect:/admin/users";
    }

}
