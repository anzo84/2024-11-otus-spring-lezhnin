package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RoutingController {

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
        return "redirect:/home";
    }

    @GetMapping("genre")
    public String genreList() {
        return "genre";
    }

    @GetMapping("author")
    public String authorList() {
        return "author";
    }

    @GetMapping("book")
    public String bookList() {
        return "book";
    }

    @GetMapping("comment")
    public String commentList() {
        return "comment";
    }

    @GetMapping("user")
    public String userList() {
        return "user";
    }
}
