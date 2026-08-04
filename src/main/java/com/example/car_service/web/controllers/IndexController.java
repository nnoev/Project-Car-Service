package com.example.car_service.web.controllers;

import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.client.dto.ServiceRecordResponse;
import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class IndexController {

    private final UserService userService;
    private final ServiceRecordClient serviceRecordClient;

    @Autowired
    public IndexController(UserService userService, ServiceRecordClient serviceRecordClient) {
        this.userService = userService;
        this.serviceRecordClient = serviceRecordClient;
    }

    @GetMapping("/")
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/dashboard")
    public ModelAndView getDashboard(@AuthenticationPrincipal UserData principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard");
        User user = userService.getById(principal.getId());
        List<ServiceRecordResponse> serviceRecords = serviceRecordClient.getAllByUserId(principal.getId());
        BigDecimal totalCost = serviceRecords.stream().map(ServiceRecordResponse::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        modelAndView.addObject("user", user);
        modelAndView.addObject("serviceRecords", serviceRecords.size());
        modelAndView.addObject("totalCost", totalCost);
        return modelAndView;
    }

    @GetMapping("/guest-login")
    public ModelAndView guestLogin(HttpSession session) {
        User guest = userService.getByUsername("guest");
        session.setAttribute("userId", guest.getId());
        return new ModelAndView("redirect:/dashboard");
    }

}
