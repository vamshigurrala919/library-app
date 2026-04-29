package com.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Author entity - represents a book author.
 * Has a One-to-Many relationship with Book.
 */
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Nationality is required")
    @Column(name = "nationality", nullable = false, length = 100)
    private String nationality;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "bio", length = 500)
    private String bio;

    // One author can have many books
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    // ─── Constructors ───────────────────────────────────────────────────────────

    public Author() {}

    public Author(String name, String nationality, Integer birthYear, String bio) {
        this.name = name;
        this.nationality = nationality;
        this.birthYear = birthYear;
        this.bio = bio;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }

    @Override
    public String toString() {
        return "Author{id=" + id + ", name='" + name + "', nationality='" + nationality + "'}";
    }
}
