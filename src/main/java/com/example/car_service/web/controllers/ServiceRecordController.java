package com.example.car_service.web.controllers;

import com.example.car_service.exceptions.UnauthorizedActionException;
import com.example.car_service.security.UserData;
import com.example.car_service.service_record.model.ServiceRecord;
import com.example.car_service.service_record.service.ServiceRecordService;
import com.example.car_service.user.model.User;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.vehicle.service.VehicleService;
import com.example.car_service.web.dtos.ServiceRecordDto;
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
public class ServiceRecordController {

    private final UserService userService;
    private final ServiceRecordService serviceRecordService;
    private final VehicleService vehicleService;

    @Autowired
    public ServiceRecordController(UserService userService,
                                   ServiceRecordService serviceRecordService,
                                   VehicleService vehicleService) {
        this.userService = userService;
        this.serviceRecordService = serviceRecordService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/service-records/add")
    public ModelAndView addServiceRecord(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("service-record-form");
        modelAndView.addObject("serviceRecord", new ServiceRecordDto());
        User user = userService.getById(principal.getId());
        modelAndView.addObject("vehicles", user.getVehicles());
        modelAndView.addObject("isEdit", false);
        return modelAndView;
    }

    @PostMapping("/service-records/add")
    public ModelAndView addServiceRecord(@Valid @ModelAttribute("serviceRecord") ServiceRecordDto serviceRecordDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserData principal) {
        User user = userService.getById(principal.getId());
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("service-record-form");
            modelAndView.addObject("isEdit", false);
            modelAndView.addObject("serviceRecord", serviceRecordDto);
            modelAndView.addObject("vehicles", user.getVehicles());
            return modelAndView;
        }
        Vehicle vehicle = vehicleService.getById(serviceRecordDto.getVehicleId());
        serviceRecordService.addService(serviceRecordDto, vehicle, user);
        redirectAttributes.addFlashAttribute("message", "Service record added successfully");
        return new ModelAndView("redirect:/service-records");
    }

    @GetMapping("/service-records/edit/{id}")
    public ModelAndView editServiceRecord(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("service-record-form");
        ServiceRecord serviceRecord = serviceRecordService.getById(id);
        ServiceRecordDto serviceRecordDto = new ServiceRecordDto();
        serviceRecordDto.setId(serviceRecord.getId());
        serviceRecordDto.setVehicleId(serviceRecord.getVehicle().getId());
        serviceRecordDto.setServiceType(serviceRecord.getServiceType());
        serviceRecordDto.setDate(serviceRecord.getDate());
        serviceRecordDto.setDescription(serviceRecord.getDescription());
        serviceRecordDto.setCost(serviceRecord.getCost());
        serviceRecordDto.setMileageAtService(serviceRecord.getMileageAtService());
        modelAndView.addObject("serviceRecord", serviceRecordDto);
        modelAndView.addObject("vehicle", serviceRecord.getVehicle());
        modelAndView.addObject("isEdit", true);
        return modelAndView;
    }

    @PostMapping("/service-records/edit/{id}")
    public ModelAndView editServiceRecord(@PathVariable UUID id, @Valid @ModelAttribute("serviceRecord") ServiceRecordDto serviceRecordDto, BindingResult bindingResult, @AuthenticationPrincipal UserData principal, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("service-record-form");
            modelAndView.addObject("serviceRecord", serviceRecordDto);
            modelAndView.addObject("isEdit", true);
            Vehicle vehicle = vehicleService.getById(serviceRecordDto.getVehicleId());
            modelAndView.addObject("vehicle", vehicle);
            return modelAndView;
        }
        User user = userService.getById(principal.getId());
        Vehicle vehicle = vehicleService.getById(serviceRecordDto.getVehicleId());
        if (!vehicle.getOwner().equals(user)) {
            throw new UnauthorizedActionException("No permission");
        }
        ServiceRecord serviceRecord = serviceRecordService.getById(id);
        serviceRecord.setServiceType(serviceRecordDto.getServiceType());
        serviceRecord.setDate(serviceRecordDto.getDate());
        serviceRecord.setDescription(serviceRecordDto.getDescription());
        serviceRecord.setCost(serviceRecordDto.getCost());
        serviceRecord.setMileageAtService(serviceRecordDto.getMileageAtService());
        serviceRecordService.save(serviceRecord);
        redirectAttributes.addFlashAttribute("message", "Service Record updated successfully");
        return new ModelAndView("redirect:/service-records");
    }

    @PostMapping("/service-records/delete/{id}")
    public ModelAndView deleteServiceRecord(@PathVariable UUID id, RedirectAttributes redirectAttributes,@AuthenticationPrincipal UserData principal) {
        ServiceRecord serviceRecord = serviceRecordService.getById(id);
        Vehicle vehicle = serviceRecord.getVehicle();
        User user = userService.getById(principal.getId());
        if (!vehicle.getOwner().equals(user)) {
            throw new UnauthorizedActionException("No permission");
        }
        serviceRecordService.deleteServiceRecord(serviceRecord);
        redirectAttributes.addFlashAttribute("message", "Service Record deleted successfully");
        return new ModelAndView("redirect:/service-records");
    }

}
