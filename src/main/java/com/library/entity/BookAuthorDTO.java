package com.library.entity;

/**
 * DTO returned by the custom inner-join JPQL query.
 * Carries Book and Author data together without lazy-loading issues.
 */
public class BookAuthorDTO {

    private Long bookId;
    private String bookTitle;
    private String isbn;
    private Integer publishedYear;
    private String genre;
    private Double price;
    private Long authorId;
    private String authorName;
    private String authorNationality;

    public BookAuthorDTO(Long bookId, String bookTitle, String isbn,
                         Integer publishedYear, String genre, Double price,
                         Long authorId, String authorName, String authorNationality) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.genre = genre;
        this.price = price;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorNationality = authorNationality;
    }

    // Getters
    public Long getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public String getIsbn() { return isbn; }
    public Integer getPublishedYear() { return publishedYear; }
    public String getGenre() { return genre; }
    public Double getPrice() { return price; }
    public Long getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public String getAuthorNationality() { return authorNationality; }
}
