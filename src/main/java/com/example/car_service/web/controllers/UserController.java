package com.example.car_service.web.controllers;

import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.client.dto.ServiceRecordResponse;
import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.web.dtos.LoginRequest;
import com.example.car_service.web.dtos.UserRegistration;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;

    private final ServiceRecordClient serviceRecordClient;

    @Autowired
    public UserController(UserService userService, ServiceRecordClient serviceRecordClient) {
        this.userService = userService;
        this.serviceRecordClient = serviceRecordClient;
    }

    @GetMapping("/login")
    public ModelAndView getLogin(@RequestParam(name = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());
        if (error != null) {
            modelAndView.addObject("error", "Invalid username or password");
        }
        return modelAndView;
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
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/vehicles")
    public ModelAndView getVehicles(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        List<ServiceRecordResponse> serviceRecords = List.of();
        boolean microserviceIsActive = true;
        try {
             serviceRecords = serviceRecordClient.getAllByUserId(principal.getId());
        }catch (Exception e){
            System.out.println("Error fetching service records: " + e.getMessage());
            microserviceIsActive = false;
        }
        Map<UUID, Long> recordCountByVehicle = serviceRecords.stream().collect(Collectors.groupingBy(ServiceRecordResponse::getVehicleId, Collectors.counting()));
        User user = userService.getById(principal.getId());
        modelAndView.setViewName("vehicles");
        modelAndView.addObject("records", recordCountByVehicle);
        modelAndView.addObject("user", user);
        modelAndView.addObject("microserviceIsActive", microserviceIsActive);
        return modelAndView;
    }

    @GetMapping("/service-records")
    public ModelAndView getServiceRecords(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(principal.getId());
        modelAndView.setViewName("service-records");
        List<ServiceRecordResponse> serviceRecords = serviceRecordClient.getAllByUserId(user.getId());
        List<Vehicle> vehicles = user.getVehicles();
        Map<UUID, Vehicle> vehicleMap = new HashMap<>();
        for (Vehicle vehicle : vehicles) {
            vehicleMap.put(vehicle.getId(), vehicle);
        }
        modelAndView.addObject("vehiclesById", vehicleMap);
        modelAndView.addObject("records", serviceRecords);
        return modelAndView;
    }

    @GetMapping("/reminders")
    public ModelAndView getReminders(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(principal.getId());
        modelAndView.setViewName("reminders");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

}
