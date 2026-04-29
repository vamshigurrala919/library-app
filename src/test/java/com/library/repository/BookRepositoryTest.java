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

@DataJpaTest
@DisplayName("BookRepository Integration Tests")
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Author orwell;
    private Book book1984;
    private Book animalFarm;

    @BeforeEach
    void setUp() {
        orwell = entityManager.persistAndFlush(
            new Author("George Orwell", "British", 1903, "Pen name of Eric Blair."));

        book1984 = entityManager.persistAndFlush(
            new Book("Nineteen Eighty-Four", "978-0-452-28423-4", 1949, "Dystopia", 12.99, orwell));
        animalFarm = entityManager.persistAndFlush(
            new Book("Animal Farm", "978-0-451-52634-2", 1945, "Satire", 8.99, orwell));
    }

    // ── findByIsbn ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findByIsbn returns correct book")
    void testFindByIsbn() {
        Optional<Book> result = bookRepository.findByIsbn("978-0-452-28423-4");
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Nineteen Eighty-Four");
    }

    @Test
    @DisplayName("findByIsbn returns empty for unknown ISBN")
    void testFindByIsbn_NotFound() {
        assertThat(bookRepository.findByIsbn("000-0-000-00000-0")).isEmpty();
    }

    // ── findByGenre ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("findByGenre returns books of that genre only")
    void testFindByGenre() {
        List<Book> satire = bookRepository.findByGenre("Satire");
        assertThat(satire).hasSize(1);
        assertThat(satire.get(0).getTitle()).isEqualTo("Animal Farm");
    }

    // ── findByTitleContainingIgnoreCase ───────────────────────────────────────

    @Test
    @DisplayName("title search is case-insensitive and partial")
    void testFindByTitleContaining() {
        List<Book> result = bookRepository.findByTitleContainingIgnoreCase("farm");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsbn()).isEqualTo("978-0-451-52634-2");
    }

    // ── findAllBooksWithAuthorDetails (custom inner join) ─────────────────────

    @Test
    @DisplayName("findAllBooksWithAuthorDetails returns DTO with both Book and Author data")
    void testFindAllBooksWithAuthorDetails() {
        List<BookAuthorDTO> dtos = bookRepository.findAllBooksWithAuthorDetails();

        assertThat(dtos).hasSizeGreaterThanOrEqualTo(2);

        // Every DTO must have an author name attached (proof the INNER JOIN worked)
        dtos.forEach(dto -> assertThat(dto.getAuthorName()).isNotBlank());

        assertThat(dtos).extracting(BookAuthorDTO::getBookTitle)
            .contains("Nineteen Eighty-Four", "Animal Farm");
    }

    // ── findBooksByAuthorId ───────────────────────────────────────────────────

    @Test
    @DisplayName("findBooksByAuthorId returns books only for that author")
    void testFindBooksByAuthorId() {
        List<Book> books = bookRepository.findBooksByAuthorId(orwell.getId());
        assertThat(books).hasSize(2);
        assertThat(books).extracting(Book::getTitle)
            .containsExactlyInAnyOrder("Nineteen Eighty-Four", "Animal Farm");
    }

    // ── existsByIsbnAndIdNot ──────────────────────────────────────────────────

    @Test
    @DisplayName("existsByIsbnAndIdNot returns true when another book has the same ISBN")
    void testExistsByIsbnAndIdNot_Collision() {
        boolean collision = bookRepository.existsByIsbnAndIdNot(
            "978-0-452-28423-4",   // ISBN of book1984
            animalFarm.getId());    // but we pretend it's for animalFarm

        assertThat(collision).isTrue();
    }

    @Test
    @DisplayName("existsByIsbnAndIdNot returns false for same book (no collision)")
    void testExistsByIsbnAndIdNot_NoCollision() {
        boolean collision = bookRepository.existsByIsbnAndIdNot(
            "978-0-452-28423-4",
            book1984.getId());      // same book → no collision

        assertThat(collision).isFalse();
    }
}
