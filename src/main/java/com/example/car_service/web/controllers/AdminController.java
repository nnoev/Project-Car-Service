package com.example.car_service.web.controllers;

import com.example.car_service.admin.AdminService;
import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.service_reminder.service.ServiceReminderService;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.vehicle.service.VehicleService;
import com.example.car_service.web.dtos.AdminCaching;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final VehicleService vehicleService;

    private final ServiceReminderService serviceReminderService;

    private final ServiceRecordClient serviceRecordClient;

    private final AdminService adminService;

    @GetMapping("")
    public ModelAndView getAdminPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/dashboard");
        AdminCaching summary = adminService.getSummary();
        modelAndView.addObject("users", summary.getUsers());
        modelAndView.addObject("vehicles", summary.getVehicles());
        modelAndView.addObject("reminders", summary.getReminders());
        modelAndView.addObject("records", summary.getServices());
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
    public ModelAndView changeRole(
            @PathVariable UUID id,
            @RequestParam UserRole role, RedirectAttributes redirectAttributes
    ) {
        userService.changeRole(id, role);
        redirectAttributes.addFlashAttribute("message", "Role changed successfully");
        log.info("Role changed for user {} by administrator", id);
        return new ModelAndView("redirect:/admin/users");
    }

    @CacheEvict(cacheNames = "adminSummary", allEntries = true)
    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable UUID id, RedirectAttributes redirectAttributes
    ) {
        User user = userService.getDeletableUser(id);
        serviceReminderService.deleteAllByUserId(id);
        serviceRecordClient.deleteAllByUserId(id);
        vehicleService.deleteAllByUserId(id);
        userService.deleteUser(user);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        log.info("User {} deleted by administrator", id);
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

    @CacheEvict(cacheNames = "adminSummary", allEntries = true)
    @PostMapping("/vehicles/{id}/delete")
    public ModelAndView deleteVehicle(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        Vehicle vehicle = vehicleService.getById(id);
        serviceReminderService.deleteAllByVehicleId(vehicle.getId());
        serviceRecordClient.deleteAllByVehicleId(vehicle.getId());
        vehicleService.deleteVehicleAdmin(vehicle);
        redirectAttributes.addFlashAttribute("message", "Vehicle deleted successfully");
        log.info("Vehicle {} including related service records and service reminders deleted by administrator", id);
        return new ModelAndView("redirect:/admin/vehicles");
    }

    @GetMapping("/reminders")
    public ModelAndView getReminders() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/reminders");
        modelAndView.addObject("reminders", serviceReminderService.getAllReminders());
        return modelAndView;
    }

    @CacheEvict(cacheNames = "adminSummary", allEntries = true)
    @PostMapping("/reminders/{id}/delete")
    public ModelAndView deleteReminder(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        serviceReminderService.deleteReminder(serviceReminderService.getById(id));
        redirectAttributes.addFlashAttribute("message", "Service Reminder deleted successfully");
        log.info("Service Reminder {} deleted by administrator", id);
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

    @CacheEvict(cacheNames = "adminSummary", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/services/{id}/delete")
    public ModelAndView deleteRecord(@PathVariable UUID id, @RequestParam UUID userId, RedirectAttributes redirectAttributes) {
        serviceRecordClient.delete(id, userId);
        redirectAttributes.addFlashAttribute("message", "Service Record deleted successfully");
        log.info("Service Record {} deleted by administrator", id);
        return new ModelAndView("redirect:/admin/services");
    }
}
