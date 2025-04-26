package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.domain.model.Role;
import ru.otus.hw.security.SecurityHelper;

@Controller
@RequiredArgsConstructor
public class RoutingController {

    private final SecurityHelper securityHelper;

    @GetMapping("login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorExist", true);
        }
        return "login";
    }

    @GetMapping("home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/")
    public String rootPage() {
        return redirectHome();
    }

    @GetMapping("genre")
    public String genreList() {
        return securityHelper.hasRole(Role.AUTHOR) ? "genre" : redirectHome();
    }

    @GetMapping("author")
    public String authorList() {
        return securityHelper.hasRole(Role.AUTHOR) ? "author" : redirectHome();
    }

    @GetMapping("book")
    public String bookList() {
        return securityHelper.hasRole(Role.AUTHOR) ? "book" : redirectHome();
    }

    @GetMapping("comment")
    public String commentList() {
        return securityHelper.hasRole(Role.COMMENTATOR) ? "comment" : redirectHome();
    }

    @GetMapping("user")
    public String userList() {
        return securityHelper.hasRole(Role.ADMINISTRATOR) ? "user" : redirectHome();
    }

    private String redirectHome() {
        return "redirect:/home";
    }
}
