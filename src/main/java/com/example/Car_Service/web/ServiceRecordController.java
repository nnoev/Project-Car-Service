package com.example.Car_Service.web;

import com.example.Car_Service.web.dtos.ServiceRecordAddRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ServiceRecordController {

    @GetMapping("/service-records/add")
    public ModelAndView addServiceRecord() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("service-record-form");
        modelAndView.addObject("serviceRecord", new ServiceRecordAddRequest());
        return modelAndView;
    }

}
