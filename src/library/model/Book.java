package library.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Phase 2 - Commit 1: Book class FULLY REFACTORED
 * 
 * REFACTORINGS APPLIED:
 * 1. Replace Constructor with Builder Pattern
 * 2. Encapsulate Fields (private final with getters)
 * 3. Replace Type Code with Enum (BookStatus)
 * 4. Make Immutable (withStatus returns new instance)
 * 5. Extract Method (formatBookInfo)
 * 6. Improve Naming (descriptive variables)
 */
public class Book {
    // Refactoring: Encapsulate Fields - all private final
    private final String isbn;
    private final String title;
    private final String author;
    private final String category;
    private final BookStatus status;
    private final LocalDate publishDate;
    
    // Refactoring: Replace Type Code with Enum
    public enum BookStatus {
        AVAILABLE, BORROWED, RESERVED, MAINTENANCE
    }
    
    // Private constructor - use Builder
    private Book(Builder builder) {
        this.isbn = builder.isbn;
        this.title = builder.title;
        this.author = builder.author;
        this.category = builder.category;
        this.status = builder.status;
        this.publishDate = builder.publishDate;
    }
    
    // Getters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public BookStatus getStatus() { return status; }
    public LocalDate getPublishDate() { return publishDate; }
    
    // Refactoring: Immutability - return new instance
    public Book withStatus(BookStatus newStatus) {
        return new Builder()
            .isbn(this.isbn)
            .title(this.title)
            .author(this.author)
            .category(this.category)
            .status(newStatus)
            .publishDate(this.publishDate)
            .build();
    }
    
    // Refactoring: Builder Pattern
    public static class Builder {
        private String isbn;
        private String title;
        private String author;
        private String category;
        private BookStatus status = BookStatus.AVAILABLE;
        private LocalDate publishDate;
        
        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }
        
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        
        public Builder author(String author) {
            this.author = author;
            return this;
        }
        
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        
        public Builder status(BookStatus status) {
            this.status = status;
            return this;
        }
        
        public Builder publishDate(LocalDate publishDate) {
            this.publishDate = publishDate;
            return this;
        }
        
        public Book build() {
            Objects.requireNonNull(isbn, "ISBN cannot be null");
            Objects.requireNonNull(title, "Title cannot be null");
            Objects.requireNonNull(author, "Author cannot be null");
            return new Book(this);
        }
    }
    
    // Refactoring: Extract Method
    public String getFormattedInfo() {
        return formatBookInfo();
    }
    
    private String formatBookInfo() {
        StringBuilder info = new StringBuilder();
        info.append("ISBN: ").append(isbn).append("\n");
        info.append("Title: ").append(title).append("\n");
        info.append("Author: ").append(author).append("\n");
        info.append("Category: ").append(category).append("\n");
        info.append("Status: ").append(status).append("\n");
        if (publishDate != null) {
            info.append("Published: ").append(publishDate).append("\n");
        }
        return info.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
    
    @Override
    public String toString() {
        return String.format("Book{isbn='%s', title='%s', author='%s', status=%s}", 
            isbn, title, author, status);
    }
}