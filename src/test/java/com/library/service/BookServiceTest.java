package com.library.service;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.BookAuthorDTO;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
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
@DisplayName("BookService Unit Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        author = new Author("George Orwell", "British", 1903, "...");
        author.setId(1L);

        book = new Book("1984", "978-0-452-28423-4", 1949, "Dystopia", 12.99, author);
        book.setId(10L);
    }

    // ── getAllBooks ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAllBooks returns all books")
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));

        List<Book> result = bookService.getAllBooks();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("1984");
    }

    // ── getAllBooksWithAuthorDetails (inner join query) ────────────────────────

    @Test
    @DisplayName("getAllBooksWithAuthorDetails returns DTO list from inner-join query")
    void testGetAllBooksWithAuthorDetails() {
        BookAuthorDTO dto = new BookAuthorDTO(
            10L, "1984", "978-0-452-28423-4", 1949, "Dystopia", 12.99,
            1L, "George Orwell", "British");

        when(bookRepository.findAllBooksWithAuthorDetails()).thenReturn(Arrays.asList(dto));

        List<BookAuthorDTO> result = bookService.getAllBooksWithAuthorDetails();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBookTitle()).isEqualTo("1984");
        assertThat(result.get(0).getAuthorName()).isEqualTo("George Orwell");
    }

    // ── getBookById ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("getBookById returns book when found")
    void testGetBookById_Found() {
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(10L);

        assertThat(result).isPresent();
        assertThat(result.get().getIsbn()).isEqualTo("978-0-452-28423-4");
    }

    @Test
    @DisplayName("getBookById returns empty when not found")
    void testGetBookById_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(bookService.getBookById(99L)).isEmpty();
    }

    // ── saveBook ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("saveBook saves successfully with valid data")
    void testSaveBook_Success() {
        Book newBook = new Book("Animal Farm", "978-0-451-52634-2", 1945, "Satire", 8.99, author);
        when(bookRepository.findByIsbn("978-0-451-52634-2")).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(newBook)).thenReturn(newBook);

        Book saved = bookService.saveBook(newBook);

        assertThat(saved.getTitle()).isEqualTo("Animal Farm");
        verify(bookRepository).save(newBook);
    }

    @Test
    @DisplayName("saveBook throws DataIntegrityViolationException on duplicate ISBN")
    void testSaveBook_DuplicateIsbn() {
        Book duplicate = new Book("Another Book", "978-0-452-28423-4", 2000, "Fiction", 9.99, author);
        // existing book has a different ID
        Book existing = new Book("1984", "978-0-452-28423-4", 1949, "Dystopia", 12.99, author);
        existing.setId(99L);

        when(bookRepository.findByIsbn("978-0-452-28423-4")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> bookService.saveBook(duplicate))
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining("978-0-452-28423-4");

        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("saveBook throws IllegalArgumentException when author not found")
    void testSaveBook_AuthorNotFound() {
        Book newBook = new Book("New Book", "999-0-000-00000-0", 2024, "Other", 5.00, author);
        when(bookRepository.findByIsbn("999-0-000-00000-0")).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.saveBook(newBook))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Author not found");
    }

    // ── updateBook ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("updateBook updates fields correctly")
    void testUpdateBook_Success() {
        Book updates = new Book("1984 – Revised", "978-0-452-28423-4", 1950, "Dystopia", 14.99, author);
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbnAndIdNot("978-0-452-28423-4", 10L)).thenReturn(false);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        Book result = bookService.updateBook(10L, updates);

        assertThat(result.getTitle()).isEqualTo("1984 – Revised");
        assertThat(result.getPrice()).isEqualTo(14.99);
    }

    @Test
    @DisplayName("updateBook throws when book not found")
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(99L, book))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("99");
    }

    // ── getBooksByGenre ───────────────────────────────────────────────────────

    @Test
    @DisplayName("getBooksByGenre returns correct subset")
    void testGetBooksByGenre() {
        when(bookRepository.findByGenre("Dystopia")).thenReturn(Arrays.asList(book));

        List<Book> result = bookService.getBooksByGenre("Dystopia");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGenre()).isEqualTo("Dystopia");
    }
}
