package com.example.Car_Service.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReminderController {

    public ModelAndView getReminder(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("reminders");
        return modelAndView;
    }

}
