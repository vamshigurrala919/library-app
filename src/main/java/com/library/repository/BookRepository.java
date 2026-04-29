package com.library.repository;

import com.library.entity.Book;
import com.library.entity.BookAuthorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Book entity.
 * Includes a custom JPQL inner-join query returning a DTO.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Derived query – find book by ISBN.
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Derived query – find books by genre.
     */
    List<Book> findByGenre(String genre);

    /**
     * Derived query – find books published after a given year.
     */
    List<Book> findByPublishedYearGreaterThan(int year);

    /**
     * Derived query – find books whose title contains a keyword.
     */
    List<Book> findByTitleContainingIgnoreCase(String keyword);

    /**
     * ─── Custom Inner-Join Query ──────────────────────────────────────────────
     *
     * JPQL INNER JOIN between Book and Author.
     * Returns a list of BookAuthorDTO projections, combining data from both
     * tables in a single query without N+1 loading.
     */
    @Query("SELECT new com.library.entity.BookAuthorDTO(" +
           "b.id, b.title, b.isbn, b.publishedYear, b.genre, b.price, " +
           "a.id, a.name, a.nationality) " +
           "FROM Book b INNER JOIN b.author a " +
           "ORDER BY a.name, b.title")
    List<BookAuthorDTO> findAllBooksWithAuthorDetails();

    /**
     * Inner join filtered by genre – returns DTO rows for one genre.
     */
    @Query("SELECT new com.library.entity.BookAuthorDTO(" +
           "b.id, b.title, b.isbn, b.publishedYear, b.genre, b.price, " +
           "a.id, a.name, a.nationality) " +
           "FROM Book b INNER JOIN b.author a " +
           "WHERE b.genre = :genre ORDER BY b.publishedYear DESC")
    List<BookAuthorDTO> findBooksByGenreWithAuthor(@Param("genre") String genre);

    /**
     * Find all books for a specific author (by author ID).
     */
    @Query("SELECT b FROM Book b INNER JOIN b.author a WHERE a.id = :authorId ORDER BY b.publishedYear")
    List<Book> findBooksByAuthorId(@Param("authorId") Long authorId);

    /**
     * Check whether an ISBN is already taken by a different book.
     */
    @Query("SELECT COUNT(b) > 0 FROM Book b WHERE b.isbn = :isbn AND b.id <> :id")
    boolean existsByIsbnAndIdNot(@Param("isbn") String isbn, @Param("id") Long id);
}
