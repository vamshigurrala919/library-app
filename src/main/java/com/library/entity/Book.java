package com.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Book entity - represents a library book.
 * Has a Many-to-One relationship with Author.
 */
@Entity
@Table(name = "books",
       uniqueConstraints = @UniqueConstraint(columnNames = "isbn", name = "uk_books_isbn"))
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank(message = "ISBN is required")
    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @NotNull(message = "Published year is required")
    @Min(value = 1000, message = "Year must be valid")
    @Max(value = 2100, message = "Year must be valid")
    @Column(name = "published_year", nullable = false)
    private Integer publishedYear;

    @NotBlank(message = "Genre is required")
    @Column(name = "genre", nullable = false, length = 50)
    private String genre;

    @DecimalMin(value = "0.0", message = "Price must be non-negative")
    @Column(name = "price")
    private Double price;

    // Many books belong to one author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull(message = "Author is required")
    private Author author;

    // ─── Constructors ───────────────────────────────────────────────────────────

    public Book() {}

    public Book(String title, String isbn, Integer publishedYear, String genre, Double price, Author author) {
        this.title = title;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.genre = genre;
        this.price = price;
        this.author = author;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPublishedYear() { return publishedYear; }
    public void setPublishedYear(Integer publishedYear) { this.publishedYear = publishedYear; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', isbn='" + isbn + "'}";
    }
}
