package com.example.car_service.web.controllers;

import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.service_reminder.service.ServiceReminderService;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.service.VehicleService;
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

    private final VehicleService vehicleService;

    private final ServiceReminderService serviceReminderService;

    private final ServiceRecordClient serviceRecordClient;

    @GetMapping("")
    public ModelAndView getAdminPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-index");
        modelAndView.addObject("users", userService.getAllUsers().size());
        modelAndView.addObject("vehicles", vehicleService.getAllVehicles().size());
        modelAndView.addObject("reminders", serviceReminderService.getAllReminders().size());
        modelAndView.addObject("records", serviceRecordClient.count());
        return modelAndView;
    }

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
