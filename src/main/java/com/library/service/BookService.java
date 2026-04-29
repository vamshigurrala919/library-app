package com.library.service;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.BookAuthorDTO;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Book-related business logic.
 */
@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // ─── Read Operations ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooksByTitle(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }

    /**
     * Returns a combined Book+Author DTO list via inner join.
     * Used by the "All Books" view so every row shows author data too.
     */
    @Transactional(readOnly = true)
    public List<BookAuthorDTO> getAllBooksWithAuthorDetails() {
        return bookRepository.findAllBooksWithAuthorDetails();
    }

    @Transactional(readOnly = true)
    public List<BookAuthorDTO> getBooksByGenreWithAuthor(String genre) {
        return bookRepository.findBooksByGenreWithAuthor(genre);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthorId(Long authorId) {
        return bookRepository.findBooksByAuthorId(authorId);
    }

    // ─── Create Operation ────────────────────────────────────────────────────────

    /**
     * Save a new book.
     * @throws DataIntegrityViolationException on duplicate ISBN.
     * @throws IllegalArgumentException        if the referenced author is missing.
     */
    public Book saveBook(Book book) {
        // Validate ISBN uniqueness
        Optional<Book> existingIsbn = bookRepository.findByIsbn(book.getIsbn());
        if (existingIsbn.isPresent() && !existingIsbn.get().getId().equals(book.getId())) {
            throw new DataIntegrityViolationException(
                "A book with ISBN '" + book.getIsbn() + "' already exists.");
        }

        // Make sure the author exists
        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new IllegalArgumentException("A valid author must be selected.");
        }
        Author author = authorRepository.findById(book.getAuthor().getId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Author not found with id: " + book.getAuthor().getId()));
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    // ─── Update Operation ────────────────────────────────────────────────────────

    /**
     * Update an existing book.
     */
    public Book updateBook(Long id, Book updatedBook) {
        Book existing = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));

        // Check ISBN collision with another book
        if (bookRepository.existsByIsbnAndIdNot(updatedBook.getIsbn(), id)) {
            throw new DataIntegrityViolationException(
                "Another book already has ISBN '" + updatedBook.getIsbn() + "'.");
        }

        Author author = authorRepository.findById(updatedBook.getAuthor().getId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Author not found with id: " + updatedBook.getAuthor().getId()));

        existing.setTitle(updatedBook.getTitle());
        existing.setIsbn(updatedBook.getIsbn());
        existing.setPublishedYear(updatedBook.getPublishedYear());
        existing.setGenre(updatedBook.getGenre());
        existing.setPrice(updatedBook.getPrice());
        existing.setAuthor(author);

        return bookRepository.save(existing);
    }

    // ─── Delete Operation ─────────────────────────────────────────────────────────

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
}
