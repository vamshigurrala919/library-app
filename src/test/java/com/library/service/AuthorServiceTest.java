package com.library.service;

import com.library.entity.Author;
import com.library.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthorService Unit Tests")
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author sampleAuthor;

    @BeforeEach
    void setUp() {
        sampleAuthor = new Author("George Orwell", "British", 1903, "Famous author.");
        sampleAuthor.setId(1L);
    }

    // ── getAllAuthors ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAllAuthors returns a list of authors")
    void testGetAllAuthors() {
        Author orwell  = new Author("George Orwell",  "British",   1903, "...");
        Author tolkien = new Author("J.R.R. Tolkien", "British",   1892, "...");
        when(authorRepository.findAll()).thenReturn(Arrays.asList(orwell, tolkien));

        List<Author> result = authorService.getAllAuthors();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("George Orwell");
        verify(authorRepository, times(1)).findAll();
    }

    // ── getAuthorById ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAuthorById returns author when found")
    void testGetAuthorById_Found() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));

        Optional<Author> result = authorService.getAuthorById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("George Orwell");
    }

    @Test
    @DisplayName("getAuthorById returns empty when not found")
    void testGetAuthorById_NotFound() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Author> result = authorService.getAuthorById(99L);

        assertThat(result).isEmpty();
    }

    // ── saveAuthor ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("saveAuthor saves and returns the author successfully")
    void testSaveAuthor_Success() {
        Author newAuthor = new Author("Mark Twain", "American", 1835, "...");
        when(authorRepository.findByNameIgnoreCase("Mark Twain")).thenReturn(Optional.empty());
        when(authorRepository.save(newAuthor)).thenReturn(newAuthor);

        Author saved = authorService.saveAuthor(newAuthor);

        assertThat(saved.getName()).isEqualTo("Mark Twain");
        verify(authorRepository).save(newAuthor);
    }

    @Test
    @DisplayName("saveAuthor throws DataIntegrityViolationException for duplicate name")
    void testSaveAuthor_DuplicateName() {
        Author duplicate = new Author("George Orwell", "British", 1903, "...");
        // different ID simulates a truly different record
        Author existingWithId = new Author("George Orwell", "British", 1903, "...");
        existingWithId.setId(5L);

        when(authorRepository.findByNameIgnoreCase("George Orwell"))
            .thenReturn(Optional.of(existingWithId));

        assertThatThrownBy(() -> authorService.saveAuthor(duplicate))
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining("George Orwell");

        verify(authorRepository, never()).save(any());
    }

    // ── updateAuthor ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("updateAuthor updates fields and saves")
    void testUpdateAuthor_Success() {
        Author updates = new Author("Eric Blair", "British", 1903, "Updated bio.");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));
        when(authorRepository.findByNameIgnoreCase("Eric Blair")).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenAnswer(inv -> inv.getArgument(0));

        Author result = authorService.updateAuthor(1L, updates);

        assertThat(result.getName()).isEqualTo("Eric Blair");
        assertThat(result.getBio()).isEqualTo("Updated bio.");
    }

    @Test
    @DisplayName("updateAuthor throws IllegalArgumentException when author not found")
    void testUpdateAuthor_NotFound() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.updateAuthor(99L, sampleAuthor))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("99");
    }

    // ── deleteAuthor ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteAuthor deletes when author exists")
    void testDeleteAuthor_Success() {
        when(authorRepository.existsById(1L)).thenReturn(true);

        authorService.deleteAuthor(1L);

        verify(authorRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteAuthor throws when author not found")
    void testDeleteAuthor_NotFound() {
        when(authorRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> authorService.deleteAuthor(99L))
            .isInstanceOf(IllegalArgumentException.class);

        verify(authorRepository, never()).deleteById(anyLong());
    }
}
