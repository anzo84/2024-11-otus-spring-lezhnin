package ru.otus.hw.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
            .map(Object::toString)
            .ifPresent(x -> model.addAttribute("statusCode", x));

        Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_MESSAGE))
            .map(Object::toString)
            .ifPresent(x -> model.addAttribute("errorMessage", x));

        return "error";
    }
}