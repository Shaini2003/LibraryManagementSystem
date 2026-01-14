package library.model;

import java.time.LocalDate;

/**
 * Phase 1: Book class with intentional bad smells
 * BAD SMELLS PRESENT:
 * 1. Long Parameter List (constructor)
 * 2. Data Clumps (multiple primitive parameters)
 * 3. Mutable Object (no encapsulation)
 * 4. No validation
 * 5. Magic strings for status
 * 6. Poor naming (single letter variables)
 */
public class Book {
    // BAD SMELL: Public fields (breaks encapsulation)
    public String isbn;
    public String title;
    public String author;
    public String category;
    public String status; // BAD SMELL: Magic strings instead of enum
    public LocalDate publishDate;
    
    // BAD SMELL: Long Parameter List
    public Book(String isbn, String title, String author, String category, 
                String status, LocalDate publishDate) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
        this.publishDate = publishDate;
    }
    
    // BAD SMELL: No validation constructor
    public Book(String i, String t, String a, String c) {
        this.isbn = i;
        this.title = t;
        this.author = a;
        this.category = c;
        this.status = "AVAILABLE";
    }
    
    // BAD SMELL: Setter allows mutation (not immutable)
    public void setStatus(String s) {
        this.status = s;
    }
    
    // BAD SMELL: Poor method - does too much
    public String getInfo() {
        String i = "";
        i += "ISBN: " + isbn + "\n";
        i += "Title: " + title + "\n";
        i += "Author: " + author + "\n";
        i += "Category: " + category + "\n";
        i += "Status: " + status + "\n";
        if (publishDate != null) {
            i += "Published: " + publishDate + "\n";
        }
        return i;
    }
    
    @Override
    public String toString() {
        return isbn + " - " + title;
    }
}