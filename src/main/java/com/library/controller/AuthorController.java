package com.library.controller;

import com.library.entity.Author;
import com.library.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * MVC Controller for Author CRUD operations.
 */
@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // ─── List All Authors ─────────────────────────────────────────────────────

    @GetMapping
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("pageTitle", "All Authors");
        return "authors/list";
    }

    // ─── Show Create Form ─────────────────────────────────────────────────────

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("pageTitle", "Add New Author");
        return "authors/form";
    }

    // ─── Handle Create Submission ─────────────────────────────────────────────

    @PostMapping("/save")
    public String saveAuthor(@Valid @ModelAttribute("author") Author author,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", author.getId() == null ? "Add New Author" : "Edit Author");
            return "authors/form";
        }

        try {
            authorService.saveAuthor(author);
            redirectAttrs.addFlashAttribute("successMessage",
                "Author '" + author.getName() + "' saved successfully!");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Add New Author");
            return "authors/form";
        }

        return "redirect:/authors";
    }

    // ─── Show Edit Form ───────────────────────────────────────────────────────

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        Author author = authorService.getAuthorById(id).orElse(null);
        if (author == null) {
            redirectAttrs.addFlashAttribute("errorMessage", "Author not found with id: " + id);
            return "redirect:/authors";
        }
        model.addAttribute("author", author);
        model.addAttribute("pageTitle", "Edit Author");
        return "authors/form";
    }

    // ─── Handle Update Submission ─────────────────────────────────────────────

    @PostMapping("/update/{id}")
    public String updateAuthor(@PathVariable Long id,
                               @Valid @ModelAttribute("author") Author author,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Author");
            return "authors/form";
        }

        try {
            authorService.updateAuthor(id, author);
            redirectAttrs.addFlashAttribute("successMessage",
                "Author updated successfully!");
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Edit Author");
            return "authors/form";
        }

        return "redirect:/authors";
    }

    // ─── View Single Author ───────────────────────────────────────────────────

    @GetMapping("/{id}")
    public String viewAuthor(@PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        Author author = authorService.getAuthorById(id).orElse(null);
        if (author == null) {
            redirectAttrs.addFlashAttribute("errorMessage", "Author not found.");
            return "redirect:/authors";
        }
        model.addAttribute("author", author);
        model.addAttribute("pageTitle", author.getName());
        return "authors/detail";
    }
}
