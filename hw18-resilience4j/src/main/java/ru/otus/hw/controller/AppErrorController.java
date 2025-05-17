package ru.otus.hw.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class AppErrorController implements ErrorController {


    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request, Model model) {
        Optional<String> statusCode = Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
            .map(Object::toString);

        String errorRequestUri = Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI))
            .map(Object::toString).orElse("/");
        if (statusCode.isPresent()) {
            if (statusCode.get().equals(Integer.toString(HttpStatus.NOT_FOUND.value()))
                && errorRequestUri.startsWith("/api/")) {
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.setStatus(HttpStatus.NOT_FOUND);
                modelAndView.setViewName("empty.pug");
                return modelAndView;
            }
            model.addAttribute("statusCode", statusCode.get());
        }

        Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_MESSAGE))
            .map(Object::toString)
            .ifPresent(x -> model.addAttribute("errorMessage", x));

        return new ModelAndView("error.pug", model.asMap());
    }
}