package com.example.car_service.web.controllers;

import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.service_reminder.service.ServiceReminderService;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        modelAndView.setViewName("admin/dashboard");
        modelAndView.addObject("users", userService.getAllUsers().size());
        modelAndView.addObject("vehicles", vehicleService.getAllVehicles().size());
        modelAndView.addObject("reminders", serviceReminderService.getAllReminders().size());
        modelAndView.addObject("records", serviceRecordClient.count());
        return modelAndView;
    }

    @GetMapping("/users")
    public ModelAndView getUsers() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/users");
        modelAndView.addObject("users", userService.getAllUsers());
        return modelAndView;
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(
            @PathVariable UUID id,
            @RequestParam UserRole role, RedirectAttributes redirectAttributes
    ) {
        userService.changeRole(id, role);
        redirectAttributes.addFlashAttribute("message", "Role changed successfully");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable UUID id, RedirectAttributes redirectAttributes
    ) {
        User user = userService.getById(id);
        serviceReminderService.deleteAllByUserId(id);
        serviceRecordClient.deleteAllByUserId(id);
        vehicleService.deleteAllByUserId(id);
        userService.deleteUser(user);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        return "redirect:/admin/users";
    }

    @GetMapping("/vehicles")
    public ModelAndView getVehicles() {
        ModelAndView modelAndView =
                new ModelAndView("admin/vehicles");
        modelAndView.addObject(
                "vehicles",
                vehicleService.getAllVehicles()
        );
        return modelAndView;
    }

    @PostMapping("/vehicles/{id}/delete")
    public ModelAndView deleteVehicle(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        serviceReminderService.deleteAllByVehicleId(vehicleService.getById(id).getOwner().getId());
        serviceRecordClient.deleteAllByVehicleId(vehicleService.getById(id).getId());
        vehicleService.deleteVehicleAdmin(vehicleService.getById(id));
        redirectAttributes.addFlashAttribute("message", "Vehicle deleted successfully");
        return new ModelAndView("redirect:/admin/vehicles");
    }

    @GetMapping("/reminders")
    public ModelAndView getReminders() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/reminders");
        modelAndView.addObject("reminders", serviceReminderService.getAllReminders());
        return modelAndView;
    }

    @PostMapping("/reminders/{id}/delete")
    public ModelAndView deleteReminder(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        serviceReminderService.deleteReminder(serviceReminderService.getById(id));
        redirectAttributes.addFlashAttribute("message", "Service Reminder deleted successfully");
        return new ModelAndView("redirect:/admin/reminders");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/services")
    public ModelAndView getRecords() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/services");
        modelAndView.addObject("services", serviceRecordClient.getAll());
        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/services/{id}/delete")
    public ModelAndView deleteRecord(@PathVariable UUID id, @RequestParam UUID userId, RedirectAttributes redirectAttributes) {
        serviceRecordClient.delete(id, userId);
        redirectAttributes.addFlashAttribute("message", "Service Record deleted successfully");
        return new ModelAndView("redirect:/admin/services");
    }
}
