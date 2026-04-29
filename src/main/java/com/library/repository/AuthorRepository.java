package com.library.repository;

import com.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Author entity.
 * Extends JpaRepository to get built-in CRUD operations.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Find authors by nationality (derived query).
     */
    List<Author> findByNationality(String nationality);

    /**
     * Find authors whose name contains the search term (case-insensitive).
     */
    List<Author> findByNameContainingIgnoreCase(String name);

    /**
     * Check whether an author with the given name already exists.
     */
    Optional<Author> findByNameIgnoreCase(String name);

    /**
     * Custom JPQL query: Inner join Author with Book to get only authors
     * who have at least one book, along with their book count.
     */
    @Query("SELECT DISTINCT a FROM Author a INNER JOIN a.books b WHERE SIZE(a.books) > 0")
    List<Author> findAuthorsWithBooks();

    /**
     * Custom native query: count books per author.
     */
    @Query(value = "SELECT a.id, a.name, COUNT(b.id) AS book_count " +
                   "FROM authors a INNER JOIN books b ON a.id = b.author_id " +
                   "GROUP BY a.id, a.name ORDER BY book_count DESC",
           nativeQuery = true)
    List<Object[]> findAuthorsWithBookCount();

    /**
     * Find author by nationality and birth year.
     */
    @Query("SELECT a FROM Author a WHERE a.nationality = :nationality AND a.birthYear >= :minYear")
    List<Author> findByNationalityAndMinBirthYear(@Param("nationality") String nationality,
                                                   @Param("minYear") int minYear);
}
