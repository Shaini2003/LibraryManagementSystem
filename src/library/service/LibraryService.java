package library.service;

import library.model.Book;
import library.model.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Phase 1: LibraryService with intentional bad smells
 * BAD SMELLS PRESENT:
 * 1. God Class (does everything)
 * 2. Long Methods
 * 3. Duplicate Code
 * 4. No design patterns
 * 5. Poor error handling
 * 6. Tight coupling
 */
public class LibraryService {
    // BAD SMELL: Public static instance (not proper singleton)
    public static LibraryService instance = new LibraryService();
    
    // BAD SMELL: Public mutable collections
    public Map<String, Book> books;
    public Map<String, Member> members;
    public List<String> transactionLog;
    
    public LibraryService() {
        books = new HashMap<>();
        members = new HashMap<>();
        transactionLog = new ArrayList<>();
    }
    
    // BAD SMELL: Long Method with multiple responsibilities
    public boolean borrowBook(String memberId, String isbn) {
        // Check member exists
        if (!members.containsKey(memberId)) {
            System.out.println("Member not found");
            return false;
        }
        
        Member m = members.get(memberId);
        
        // Check book exists
        if (!books.containsKey(isbn)) {
            System.out.println("Book not found");
            return false;
        }
        
        Book b = books.get(isbn);
        
        // Check book availability
        if (!b.status.equals("AVAILABLE")) {
            System.out.println("Book not available");
            return false;
        }
        
        // Check borrowing limit - BAD SMELL: Duplicate logic from Member class
        int limit;
        if (m.memberType.equals("STUDENT")) {
            limit = 3;
        } else if (m.memberType.equals("FACULTY")) {
            limit = 5;
        } else {
            limit = 1;
        }
        
        if (m.borrowedBookIsbns.size() >= limit) {
            System.out.println("Borrowing limit reached");
            return false;
        }
        
        // Update book status
        b.status = "BORROWED";
        
        // Update member
        m.borrowedBookIsbns.add(isbn);
        
        // Log transaction - BAD SMELL: Poor logging
        transactionLog.add("BORROW," + memberId + "," + isbn + "," + 
                          java.time.LocalDateTime.now());
        
        System.out.println("Book borrowed successfully");
        return true;
    }
    
    // BAD SMELL: Duplicate code structure from borrowBook
    public boolean returnBook(String memberId, String isbn) {
        // Check member exists
        if (!members.containsKey(memberId)) {
            System.out.println("Member not found");
            return false;
        }
        
        Member m = members.get(memberId);
        
        // Check book exists
        if (!books.containsKey(isbn)) {
            System.out.println("Book not found");
            return false;
        }
        
        Book b = books.get(isbn);
        
        // Check if member has this book
        if (!m.borrowedBookIsbns.contains(isbn)) {
            System.out.println("Member hasn't borrowed this book");
            return false;
        }
        
        // Update book status
        b.status = "AVAILABLE";
        
        // Update member
        m.borrowedBookIsbns.remove(isbn);
        
        // Log transaction
        transactionLog.add("RETURN," + memberId + "," + isbn + "," + 
                          java.time.LocalDateTime.now());
        
        System.out.println("Book returned successfully");
        return true;
    }
    
    // BAD SMELL: Simple search with no flexibility
    public List<Book> searchBooksByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book b : books.values()) {
            if (b.title.toLowerCase().contains(title.toLowerCase())) {
                results.add(b);
            }
        }
        return results;
    }
    
    // BAD SMELL: Duplicate search logic
    public List<Book> searchBooksByAuthor(String author) {
        List<Book> results = new ArrayList<>();
        for (Book b : books.values()) {
            if (b.author.toLowerCase().contains(author.toLowerCase())) {
                results.add(b);
            }
        }
        return results;
    }
    
    public void addBook(Book book) {
        books.put(book.isbn, book);
    }
    
    public void addMember(Member member) {
        members.put(member.memberId, member);
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
    
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }
}