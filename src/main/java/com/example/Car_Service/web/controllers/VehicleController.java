package com.example.Car_Service.web.controllers;

import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.service.UserService;
import com.example.Car_Service.vehicle.model.Vehicle;
import com.example.Car_Service.vehicle.service.VehicleService;
import com.example.Car_Service.web.dtos.VehicleAddRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        modelAndView.addObject("isEdit",false);
        return modelAndView;
    }

    @PostMapping("/vehicles/add")
    public ModelAndView addVehicle(@Valid @ModelAttribute("vehicle") VehicleAddRequest vehicleAddRequest, BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("vehicle-form");
            modelAndView.addObject("vehicle", vehicleAddRequest);
            modelAndView.addObject("isEdit", false);

            User user = userService.getById((UUID) session.getAttribute("userId"));
            modelAndView.addObject("vehicles", user.getVehicles());

            return modelAndView;
        }
        UUID userId = (UUID) session.getAttribute("userId");
        User user = userService.getById(userId);
        vehicleService.addVehicle(vehicleAddRequest, user);
        redirectAttributes.addFlashAttribute("message", "Vehicle added successfully");
        return new ModelAndView("redirect:/vehicles");
    }
    @PostMapping("/vehicles/delete/{id}")
    public ModelAndView deleteVehicle(@PathVariable UUID id, HttpSession session, RedirectAttributes redirectAttributes) {

        UUID userId = (UUID) session.getAttribute("userId");
        User user = userService.getById(userId);
        Vehicle vehicle = vehicleService.getById(id);
        boolean ownsVehicle = user.getVehicles()
                .stream()
                .anyMatch(v -> v.equals(vehicle));

        if (!ownsVehicle) {
            throw new RuntimeException("No permission");
        }
        vehicleService.deleteVehicle(vehicle);
        redirectAttributes.addFlashAttribute("message", "Vehicle deleted successfully");
        return new ModelAndView("redirect:/vehicles");
    }
    @GetMapping("/vehicles/edit/{id}")
    public ModelAndView editVehicle(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("vehicle-form");
        Vehicle vehicle = vehicleService.getById(id);
        VehicleAddRequest vehicleAddRequest = new VehicleAddRequest();
        vehicleAddRequest.setModel(vehicle.getModel());
        vehicleAddRequest.setMake(vehicle.getMake());
        vehicleAddRequest.setYear(vehicle.getYear());
        vehicleAddRequest.setMileage(vehicle.getMileage());
        vehicleAddRequest.setVin(vehicle.getVin());
        vehicleAddRequest.setId(vehicle.getId());
        modelAndView.addObject("vehicle", vehicleAddRequest);
        modelAndView.addObject("isEdit",true);
        return modelAndView;
    }
    @PostMapping("/vehicles/edit/{id}")
    public ModelAndView editVehicle(@Valid @ModelAttribute("vehicle")VehicleAddRequest vehicleAddRequest, BindingResult bindingResult,@PathVariable UUID id,HttpSession session, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("vehicle-form");
            modelAndView.addObject("vehicle", vehicleAddRequest);
            modelAndView.addObject("isEdit", true);

            User user = userService.getById((UUID) session.getAttribute("userId"));
            modelAndView.addObject("vehicles", user.getVehicles());

            return modelAndView;
        }
        Vehicle vehicle = vehicleService.getById(id);
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
