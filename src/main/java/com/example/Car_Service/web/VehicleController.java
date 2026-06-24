package com.example.Car_Service.web;

import com.example.Car_Service.vehicle.service.VehicleService;
import com.example.Car_Service.web.dtos.VehicleAddRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/vehicles/add")
    public ModelAndView addVehicle(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("vehicle-form");
        modelAndView.addObject("addVehicle", new VehicleAddRequest());
        return modelAndView;
    }

    @PostMapping("/vehicles/add")
    public ModelAndView addVehicle(@Valid VehicleAddRequest vehicleAddRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("vehicle-form");
        }
        vehicleService.addVehicle(vehicleAddRequest, session);
        return new ModelAndView("redirect:/vehicles");
    }

}
