package com.example.car_service.web.controllers;

import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.client.dto.ServiceRecordRequest;
import com.example.car_service.client.dto.ServiceRecordResponse;
import com.example.car_service.exceptions.LimitException;
import com.example.car_service.exceptions.UnauthorizedActionException;
import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.vehicle.service.VehicleService;
import com.example.car_service.web.dtos.ServiceRecordDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class ServiceRecordController {

    private final UserService userService;

    private final VehicleService vehicleService;

    private final ServiceRecordClient serviceRecordClient;

    @Autowired
    public ServiceRecordController(UserService userService, VehicleService vehicleService, ServiceRecordClient serviceRecordClient) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.serviceRecordClient = serviceRecordClient;
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

    @CacheEvict(cacheNames = "adminSummary", allEntries = true)
    @PostMapping("/service-records/add")
    public ModelAndView addServiceRecord(@Valid ServiceRecordDto serviceRecordDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserData principal) {
        User user = userService.getById(principal.getId());
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("service-record-form");
            modelAndView.addObject("isEdit", false);
            modelAndView.addObject("serviceRecord", serviceRecordDto);
            modelAndView.addObject("vehicles", user.getVehicles());
            return modelAndView;
        }
        Vehicle vehicle = vehicleService.getById(serviceRecordDto.getVehicleId());
        if (!vehicle.getOwner().equals(user)) {
            throw new UnauthorizedActionException("No permission");
        }
        checkGuestServiceRecordLimit(user);
        ServiceRecordRequest request = ServiceRecordRequest.builder()
                .vehicleId(vehicle.getId())
                .serviceType(serviceRecordDto.getServiceType())
                .userId(user.getId())
                .serviceDate(serviceRecordDto.getDate())
                .description(serviceRecordDto.getDescription())
                .cost(serviceRecordDto.getCost())
                .mileageAtService(serviceRecordDto.getMileageAtService())
                .build();
        serviceRecordClient.create(request);
        redirectAttributes.addFlashAttribute("message", "Service record added successfully");
        return new ModelAndView("redirect:/service-records");
    }

    @GetMapping("/service-records/edit/{id}")
    public ModelAndView editServiceRecord(@PathVariable UUID id, @AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("service-record-form");
        ServiceRecordResponse response = serviceRecordClient.getById(id, principal.getId());
        Vehicle vehicle = vehicleService.getById(response.getVehicleId());
        if (!vehicle.getOwner().getId().equals(principal.getId())) {
            throw new UnauthorizedActionException("No permission");
        }
        ServiceRecordDto serviceRecordDto = new ServiceRecordDto();
        serviceRecordDto.setId(response.getId());
        serviceRecordDto.setServiceType(response.getServiceType());
        serviceRecordDto.setDate(response.getServiceDate());
        serviceRecordDto.setDescription(response.getDescription());
        serviceRecordDto.setCost(response.getCost());
        serviceRecordDto.setMileageAtService(response.getMileageAtService());
        serviceRecordDto.setVehicleId(response.getVehicleId());
        modelAndView.addObject("serviceRecord", serviceRecordDto);
        modelAndView.addObject("vehicle", vehicle);
        modelAndView.addObject("isEdit", true);
        return modelAndView;
    }

    @PostMapping("/service-records/edit/{id}")
    public ModelAndView editServiceRecord(@PathVariable UUID id, @Valid ServiceRecordDto serviceRecordDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserData principal) {
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
        ServiceRecordRequest request = ServiceRecordRequest.builder()
                .serviceType(serviceRecordDto.getServiceType())
                .serviceDate(serviceRecordDto.getDate())
                .description(serviceRecordDto.getDescription())
                .cost(serviceRecordDto.getCost())
                .mileageAtService(serviceRecordDto.getMileageAtService())
                .build();
        serviceRecordClient.update(id, user.getId(), request);
        redirectAttributes.addFlashAttribute("message", "Service Record updated successfully");
        return new ModelAndView("redirect:/service-records");
    }

    @CacheEvict(cacheNames = "adminSummary", allEntries = true)
    @PostMapping("/service-records/delete/{id}")
    public ModelAndView deleteServiceRecord(@PathVariable UUID id, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserData principal) {
        User user = userService.getById(principal.getId());
        Vehicle vehicle = vehicleService.getById(serviceRecordClient.getById(id,principal.getId()).getVehicleId());
        if (!vehicle.getOwner().equals(user)) {
            throw new UnauthorizedActionException("No permission");
        }
        serviceRecordClient.delete(id,user.getId());
        redirectAttributes.addFlashAttribute("message", "Service Record deleted successfully");
        return new ModelAndView("redirect:/service-records");
    }

    private void checkGuestServiceRecordLimit(User user) {
        if (user.getRole() != UserRole.GUEST) {
            return;
        }
        List<ServiceRecordResponse> records =
                serviceRecordClient.getAllByUserId(user.getId());
        if (records.size() >= 2) {
            throw new LimitException(
                    "Guest has reached the maximum number of service records"
            );
        }
    }

}
