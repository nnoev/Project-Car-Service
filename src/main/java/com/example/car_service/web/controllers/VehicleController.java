package com.example.car_service.web.controllers;

import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.vehicle.service.VehicleService;
import com.example.car_service.web.dtos.VehicleAddRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;

    private final UserService userService;

    @Autowired
    public VehicleController(VehicleService vehicleService, UserService userService) {
        this.vehicleService = vehicleService;
        this.userService = userService;
    }

    @GetMapping("/vehicles/add")
    public ModelAndView addVehicle() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("vehicle-form");
        modelAndView.addObject("vehicle", new VehicleAddRequest());
        modelAndView.addObject("isEdit", false);
        return modelAndView;
    }

    @PostMapping("/vehicles/add")
    public ModelAndView addVehicle(@Valid @ModelAttribute("vehicle") VehicleAddRequest vehicleAddRequest, BindingResult bindingResult, @AuthenticationPrincipal UserData principal, RedirectAttributes redirectAttributes) {
        User user = userService.getById(principal.getId());
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("vehicle-form");
            modelAndView.addObject("vehicle", vehicleAddRequest);
            modelAndView.addObject("isEdit", false);
            modelAndView.addObject("vehicles", user.getVehicles());
            return modelAndView;
        }
        vehicleService.addVehicle(vehicleAddRequest, user);
        redirectAttributes.addFlashAttribute("message", "Vehicle added successfully");
        return new ModelAndView("redirect:/vehicles");
    }

    @PostMapping("/vehicles/delete/{id}")
    public ModelAndView deleteVehicle(@PathVariable UUID id, @AuthenticationPrincipal UserData principal, RedirectAttributes redirectAttributes) {
        User user = userService.getById(principal.getId());
        Vehicle vehicle = vehicleService.getById(id);
        vehicleService.checkOwnership(vehicle,user);
        vehicleService.deleteVehicle(vehicle);
        redirectAttributes.addFlashAttribute("message", "Vehicle deleted successfully");
        return new ModelAndView("redirect:/vehicles");
    }

    @GetMapping("/vehicles/edit/{id}")
    public ModelAndView editVehicle(@PathVariable UUID id, @AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("vehicle-form");
        Vehicle vehicle = vehicleService.getById(id);
        User user = userService.getById(principal.getId());
        vehicleService.checkOwnership(vehicle,user);
        VehicleAddRequest vehicleAddRequest = new VehicleAddRequest();
        vehicleAddRequest.setModel(vehicle.getModel());
        vehicleAddRequest.setMake(vehicle.getMake());
        vehicleAddRequest.setYear(vehicle.getYear());
        vehicleAddRequest.setMileage(vehicle.getMileage());
        vehicleAddRequest.setVin(vehicle.getVin());
        vehicleAddRequest.setId(vehicle.getId());
        modelAndView.addObject("vehicle", vehicleAddRequest);
        modelAndView.addObject("isEdit", true);
        return modelAndView;
    }

    @PostMapping("/vehicles/edit/{id}")
    public ModelAndView editVehicle(@Valid @ModelAttribute("vehicle") VehicleAddRequest vehicleAddRequest, BindingResult bindingResult, @PathVariable UUID id, @AuthenticationPrincipal UserData principal, RedirectAttributes redirectAttributes) {
        User user = userService.getById(principal.getId());
        Vehicle vehicle = vehicleService.getById(id);
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("vehicle-form");
            modelAndView.addObject("vehicle", vehicleAddRequest);
            modelAndView.addObject("isEdit", true);
            modelAndView.addObject("vehicles", user.getVehicles());
            return modelAndView;
        }
        vehicleService.checkOwnership(vehicle,user);
        vehicle.setModel(vehicleAddRequest.getModel());
        vehicle.setMake(vehicleAddRequest.getMake());
        vehicle.setYear(vehicleAddRequest.getYear());
        vehicle.setMileage(vehicleAddRequest.getMileage());
        vehicle.setVin(vehicleAddRequest.getVin());
        vehicleService.save(vehicle);
        redirectAttributes.addFlashAttribute("message", "Vehicle updated successfully");
        return new ModelAndView("redirect:/vehicles");
    }

}
