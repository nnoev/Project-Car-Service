package com.example.car_service.web.controllers;

import com.example.car_service.exceptions.DuplicateException;
import com.example.car_service.exceptions.LimitException;
import com.example.car_service.exceptions.NothingFoundException;
import com.example.car_service.exceptions.UnauthorizedActionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DuplicateException.class)
    public ModelAndView handleException(DuplicateException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("status", HttpStatus.CONFLICT.value());
        modelAndView.addObject("title", HttpStatus.CONFLICT.getReasonPhrase());
        log.error("Duplicate Exception: {}", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(LimitException.class)
    public ModelAndView handleException(LimitException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("status", HttpStatus.INSUFFICIENT_STORAGE.value());
        modelAndView.addObject("title", HttpStatus.INSUFFICIENT_STORAGE.getReasonPhrase());
        log.error("Limit Exception: {}", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ModelAndView handleException(UnauthorizedActionException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("status", HttpStatus.UNAUTHORIZED.value());
        modelAndView.addObject("title", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        log.error("Unauthorized Action Exception: {}", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NothingFoundException.class)
    public ModelAndView handleException(NothingFoundException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("title", HttpStatus.NOT_FOUND.getReasonPhrase());
        log.error("Nothing Found Exception: {}", e.getMessage());
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
        log.error("Exception: {}", e.getMessage());
        return modelAndView;
    }

}
