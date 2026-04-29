package com.library.controller;

import com.library.entity.Book;
import com.library.entity.BookAuthorDTO;
import com.library.service.AuthorService;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * MVC Controller for Book CRUD operations.
 */
@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    // ─── List All Books (inner-join DTO view) ─────────────────────────────────

    @GetMapping
    public String listBooks(Model model) {
        List<BookAuthorDTO> books = bookService.getAllBooksWithAuthorDetails();
        model.addAttribute("books", books);
        model.addAttribute("pageTitle", "All Books");
        return "books/list";
    }

    // ─── Show Create Form ─────────────────────────────────────────────────────

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("pageTitle", "Add New Book");
        return "books/form";
    }

    // ─── Handle Create Submission ─────────────────────────────────────────────

    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("pageTitle", "Add New Book");
            return "books/form";
        }

        try {
            bookService.saveBook(book);
            redirectAttrs.addFlashAttribute("successMessage",
                "Book '" + book.getTitle() + "' added successfully!");
        } catch (DataIntegrityViolationException | IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("pageTitle", "Add New Book");
            return "books/form";
        }

        return "redirect:/books";
    }

    // ─── Show Edit Form ───────────────────────────────────────────────────────

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        Book book = bookService.getBookById(id).orElse(null);
        if (book == null) {
            redirectAttrs.addFlashAttribute("errorMessage", "Book not found with id: " + id);
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("pageTitle", "Edit Book");
        return "books/form";
    }

    // ─── Handle Update Submission ─────────────────────────────────────────────

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("pageTitle", "Edit Book");
            return "books/form";
        }

        try {
            bookService.updateBook(id, book);
            redirectAttrs.addFlashAttribute("successMessage", "Book updated successfully!");
        } catch (DataIntegrityViolationException | IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("pageTitle", "Edit Book");
            return "books/form";
        }

        return "redirect:/books";
    }

    // ─── View Single Book ─────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        Book book = bookService.getBookById(id).orElse(null);
        if (book == null) {
            redirectAttrs.addFlashAttribute("errorMessage", "Book not found.");
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        model.addAttribute("pageTitle", book.getTitle());
        return "books/detail";
    }
}
