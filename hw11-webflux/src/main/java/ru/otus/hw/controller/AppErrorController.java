package ru.otus.hw.controller;

import org.springframework.boot.web.servlet.error.ErrorController;

//@Controller
public class AppErrorController implements ErrorController {

/*
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

        return new ModelAndView("error.pug.txt", model.asMap());
    }*/
}