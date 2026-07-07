package com.example.Car_Service.web;

import com.example.Car_Service.service_record.service.ServiceRecordService;
import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.service.UserService;
import com.example.Car_Service.vehicle.model.Vehicle;
import com.example.Car_Service.vehicle.service.VehicleService;
import com.example.Car_Service.web.dtos.ServiceRecordAddRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ServiceRecordController {

    private final UserService userService;
    private final ServiceRecordService serviceRecordService;
    private final VehicleService vehicleService;

    @Autowired
    public ServiceRecordController(UserService userService, ServiceRecordService serviceRecordService, VehicleService vehicleService) {
        this.userService = userService;
        this.serviceRecordService = serviceRecordService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/service-records/add")
    public ModelAndView addServiceRecord(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("service-record-form");
        modelAndView.addObject("serviceRecord", new ServiceRecordAddRequest());
        User user = userService.getUserBySession(session);
        modelAndView.addObject("vehicles", user.getVehicles());
        modelAndView.addObject("isEdit",false);
        return modelAndView;
    }
    @PostMapping("/service-records/add")
    public ModelAndView  addServiceRecord(@Valid ServiceRecordAddRequest serviceRecordAddRequest, HttpSession session, BindingResult bindingResult ){
        if (bindingResult.hasErrors()) {
            return new ModelAndView("service-record-form");
        }
        Vehicle vehicle = vehicleService.getById(serviceRecordAddRequest.getVehicleId());
        User user = userService.getUserBySession(session);
        serviceRecordService.addService(serviceRecordAddRequest,vehicle,user);


        return new ModelAndView("redirect:/service-records");
    }
}
