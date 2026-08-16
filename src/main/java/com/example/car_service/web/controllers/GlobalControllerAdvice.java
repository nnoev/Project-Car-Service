package com.example.car_service.web.controllers;

import com.example.car_service.exceptions.DuplicateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DuplicateException.class)
    public ModelAndView handleException(DuplicateException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("status", HttpStatus.CONFLICT.value());
        modelAndView.addObject("title", HttpStatus.CONFLICT.getReasonPhrase());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleLeftExceptions(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.addObject("title", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return modelAndView;
    }

}
