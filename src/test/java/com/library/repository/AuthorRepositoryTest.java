package com.library.repository;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.BookAuthorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository slice tests – uses an in-memory H2 database.
 * Only the JPA layer is loaded (no web/service context).
 */
@DataJpaTest
@DisplayName("AuthorRepository Integration Tests")
class AuthorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private Author orwell;
    private Author tolkien;

    @BeforeEach
    void setUp() {
        orwell  = entityManager.persistAndFlush(
            new Author("George Orwell",  "British", 1903, "Pen name of Eric Blair."));
        tolkien = entityManager.persistAndFlush(
            new Author("J.R.R. Tolkien", "British", 1892, "Creator of Middle-earth."));

        // Give orwell one book so inner-join queries return him
        entityManager.persistAndFlush(
            new Book("1984", "978-0-452-28423-4", 1949, "Dystopia", 12.99, orwell));
    }

    // ── findAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findAll returns all persisted authors")
    void testFindAll() {
        List<Author> result = authorRepository.findAll();
        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
    }

    // ── findByNationality ─────────────────────────────────────────────────────

    @Test
    @DisplayName("findByNationality returns correct authors")
    void testFindByNationality() {
        List<Author> british = authorRepository.findByNationality("British");
        assertThat(british).extracting(Author::getName)
            .contains("George Orwell", "J.R.R. Tolkien");
    }

    // ── findByNameIgnoreCase ──────────────────────────────────────────────────

    @Test
    @DisplayName("findByNameIgnoreCase is case-insensitive")
    void testFindByNameIgnoreCase() {
        Optional<Author> result = authorRepository.findByNameIgnoreCase("GEORGE ORWELL");
        assertThat(result).isPresent();
        assertThat(result.get().getNationality()).isEqualTo("British");
    }

    // ── findByNameContainingIgnoreCase ────────────────────────────────────────

    @Test
    @DisplayName("findByNameContainingIgnoreCase finds partial matches")
    void testFindByNameContaining() {
        List<Author> result = authorRepository.findByNameContainingIgnoreCase("orwell");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("George Orwell");
    }

    // ── findAuthorsWithBooks (inner join) ─────────────────────────────────────

    @Test
    @DisplayName("findAuthorsWithBooks returns only authors that have books")
    void testFindAuthorsWithBooks() {
        // orwell has a book; tolkien does not
        List<Author> result = authorRepository.findAuthorsWithBooks();
        assertThat(result).extracting(Author::getName).contains("George Orwell");
        assertThat(result).extracting(Author::getName).doesNotContain("J.R.R. Tolkien");
    }
}
