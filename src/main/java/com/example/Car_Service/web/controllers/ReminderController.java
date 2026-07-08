package com.example.Car_Service.web.controllers;

import com.example.Car_Service.service_reminder.model.ServiceReminder;
import com.example.Car_Service.service_reminder.service.ServiceReminderService;
import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.service.UserService;
import com.example.Car_Service.vehicle.model.Vehicle;
import com.example.Car_Service.vehicle.service.VehicleService;
import com.example.Car_Service.web.dtos.ServiceReminderDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class ReminderController {

    private final UserService userService;

    private final VehicleService vehicleService;

    private final ServiceReminderService serviceReminderService;

    @Autowired
    public ReminderController(UserService userService, VehicleService vehicleService, ServiceReminderService serviceReminderService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.serviceReminderService = serviceReminderService;
    }

    @GetMapping("/reminders/add")
    public ModelAndView addReminder(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("reminder-form");
        modelAndView.addObject("reminder", new ServiceReminderDto());
        User user = userService.getUserBySession(session);
        modelAndView.addObject("vehicles", user.getVehicles());
        return modelAndView;
    }

    @PostMapping("/reminders/add")
    public ModelAndView addReminder(@Valid @ModelAttribute("reminder") ServiceReminderDto reminderDto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("reminder-form");
            modelAndView.addObject("vehicles",
                    userService.getUserBySession(session).getVehicles());
            return modelAndView;
        }
        User user = userService.getUserBySession(session);
        Vehicle vehicle = vehicleService.getById(reminderDto.getVehicleId());
        serviceReminderService.addReminder(reminderDto,vehicle,user);
        redirectAttributes.addFlashAttribute("message", "Reminder added successfully");
        return new ModelAndView("redirect:/reminders");
    }

    @PostMapping("/reminders/delete/{id}")
    public ModelAndView deleteVehicle(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        ServiceReminder serviceReminder = serviceReminderService.getById(id);
        serviceReminderService.deleteServiceRecord(serviceReminder);
        redirectAttributes.addFlashAttribute("message", "Service Reminder deleted successfully");
        return new ModelAndView("redirect:/reminders");
    }

    @PostMapping("/reminders/toggle/{id}")
    public String toggleReminder(@PathVariable UUID id,
                                 @RequestParam boolean completed,
                                 RedirectAttributes redirectAttributes) {
        ServiceReminder reminder = serviceReminderService.getById(id);
        reminder.setCompleted(completed);
        serviceReminderService.save(reminder);
        redirectAttributes.addFlashAttribute("message", "Reminder updated");
        return "redirect:/reminders";
    }

}
