package com.library.service;

import com.library.entity.Author;
import com.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Author-related business logic.
 */
@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // ─── Read Operations ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Author> getAuthorsByNationality(String nationality) {
        return authorRepository.findByNationality(nationality);
    }

    @Transactional(readOnly = true)
    public List<Author> searchAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

    /** Returns only authors that have at least one book (uses inner join). */
    @Transactional(readOnly = true)
    public List<Author> getAuthorsWithBooks() {
        return authorRepository.findAuthorsWithBooks();
    }

    // ─── Create Operation ────────────────────────────────────────────────────────

    /**
     * Save a new author.
     * @throws DataIntegrityViolationException if a duplicate name is detected.
     */
    public Author saveAuthor(Author author) {
        // Business rule: no two authors with the identical name
        Optional<Author> existing = authorRepository.findByNameIgnoreCase(author.getName());
        if (existing.isPresent() && !existing.get().getId().equals(author.getId())) {
            throw new DataIntegrityViolationException(
                "An author with the name '" + author.getName() + "' already exists.");
        }
        return authorRepository.save(author);
    }

    // ─── Update Operation ────────────────────────────────────────────────────────

    /**
     * Update an existing author.
     * @throws IllegalArgumentException if the author does not exist.
     */
    public Author updateAuthor(Long id, Author updatedAuthor) {
        Author existing = authorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Author not found with id: " + id));

        existing.setName(updatedAuthor.getName());
        existing.setNationality(updatedAuthor.getNationality());
        existing.setBirthYear(updatedAuthor.getBirthYear());
        existing.setBio(updatedAuthor.getBio());

        return authorRepository.save(existing);
    }

    // ─── Delete Operation ─────────────────────────────────────────────────────────

    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new IllegalArgumentException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return authorRepository.existsById(id);
    }
}
