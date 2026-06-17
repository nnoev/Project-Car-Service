package com.example.Car_Service.web;

import com.example.Car_Service.user.service.UserService;
import com.example.Car_Service.vehicle.model.Vehicle;
import com.example.Car_Service.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;
@Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/vehicles")
    public ModelAndView getVehicles() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("vehicles");
        List<Vehicle> vehicles = vehicleService.findByOwner();
        return modelAndView;
    }
}
