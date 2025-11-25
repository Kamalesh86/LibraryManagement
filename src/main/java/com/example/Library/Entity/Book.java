package com.example.Library.Entity;

import jakarta.persistence.*;

@Entity // Marks this class as a JPA entity
@Table(name = "books")
public class Book {
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    @Column(name = "book_id") // Maps to the 'book_id' column in the database
    private Long bookId;

    @Column(name = "title", nullable = false) // Maps to 'title' column, cannot be null
    private String title;

    @Column(name = "author", nullable = false) // Maps to 'author' column, cannot be null
    private String author;

    @Column(name = "genre") // Maps to 'genre' column
    private String genre;

    @Column(name = "isbn", unique = true, nullable = false, length = 10) // Maps to 'isbn' column, must be unique and not null, length 10
    private String isbn;

    @Column(name = "year_published") // Maps to 'year_published' column
    private Integer yearPublished;

    @Column(name = "available_copies", nullable = false) // Maps to 'available_copies' column, cannot be null
    private Integer availableCopies;

    // Default constructor (required by JPA)
    public Book() {
    }

    // Constructor with all fields (optional, but good for testing/convenience)
    public Book(String title, String author, String genre, String isbn, Integer yearPublished, Integer availableCopies) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.yearPublished = yearPublished;
        this.availableCopies = availableCopies;
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    // equals() method (for object comparison)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId != null && bookId.equals(book.bookId);
    }

    // hashCode() method (for use in collections like HashMap, HashSet)
    @Override
    public int hashCode() {
        return bookId != null ? bookId.hashCode() : 0;
    }

    // toString() method (for logging and debugging)
    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", isbn='" + isbn + '\'' +
                ", yearPublished=" + yearPublished +
                ", availableCopies=" + availableCopies +
                '}';
    }
}
