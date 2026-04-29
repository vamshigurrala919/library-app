package com.library.controller;

import com.library.service.AuthorService;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the application home/dashboard page.
 */
@Controller
public class HomeController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public HomeController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalBooks", bookService.getAllBooks().size());
        model.addAttribute("totalAuthors", authorService.getAllAuthors().size());
        model.addAttribute("recentBooks", bookService.getAllBooksWithAuthorDetails());
        model.addAttribute("pageTitle", "Library Dashboard");
        return "home";
    }
}
