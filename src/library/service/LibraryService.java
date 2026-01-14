package library.service;

import library.model.Book;
import library.model.Member;
import library.model.Transaction;
import library.observer.LibraryObserver;
import library.strategy.SearchStrategy;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Phase 3 - Commit 3: LibraryService with ALL DESIGN PATTERNS
 * 
 * PATTERNS IMPLEMENTED:
 * 1. Singleton Pattern - Proper thread-safe implementation
 * 2. Observer Pattern - Event notification system
 * 3. Strategy Pattern - Flexible search algorithms
 * 4. Builder Pattern - Already in model classes
 */
public class LibraryService {
    // Singleton Pattern - thread-safe double-checked locking
    private static volatile LibraryService instance;
    private static final Object lock = new Object();
    
    private final Map<String, Book> bookCatalog;
    private final Map<String, Member> members;
    private final List<Transaction> transactions;
    
    // Observer Pattern - list of observers
    private final List<LibraryObserver> observers;
    
    // Private constructor (Singleton)
    private LibraryService() {
        this.bookCatalog = new HashMap<>();
        this.members = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.observers = new ArrayList<>();
    }
    
    // Singleton Pattern - thread-safe getInstance
    public static LibraryService getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LibraryService();
                }
            }
        }
        return instance;
    }
    
    // Observer Pattern - register observers
    public void registerObserver(LibraryObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(LibraryObserver observer) {
        observers.remove(observer);
    }
    
    // Observer Pattern - notify all observers
    private void notifyObservers(String event, String details) {
        for (LibraryObserver observer : observers) {
            observer.update(event, details);
        }
    }
    
    // Book Management with Observer notifications
    public void addBook(Book book) {
        bookCatalog.put(book.getIsbn(), book);
        notifyObservers("BOOK_ADDED", "Book added: " + book.getTitle());
    }
    
    public Optional<Book> getBook(String isbn) {
        return Optional.ofNullable(bookCatalog.get(isbn));
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(bookCatalog.values());
    }
    
    // Strategy Pattern - search with interchangeable algorithms
    public List<Book> searchBooks(SearchStrategy strategy, String query) {
        return strategy.search(new ArrayList<>(bookCatalog.values()), query);
    }
    
    // Member Management with Observer notifications
    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
        notifyObservers("MEMBER_ADDED", "Member registered: " + member.getName());
    }
    
    public Optional<Member> getMember(String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }
    
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }
    
    // Transaction Operations with Observer notifications
    public boolean borrowBook(String memberId, String isbn) {
        Optional<Member> memberOpt = getMember(memberId);
        Optional<Book> bookOpt = getBook(isbn);
        
        if (!memberOpt.isPresent() || !bookOpt.isPresent()) {
            notifyObservers("BORROW_FAILED", "Member or book not found");
            return false;
        }
        
        Member member = memberOpt.get();
        Book book = bookOpt.get();
        
        if (!member.canBorrowMore()) {
            notifyObservers("BORROW_FAILED", 
                "Borrowing limit reached for " + member.getName());
            return false;
        }
        
        if (book.getStatus() != Book.BookStatus.AVAILABLE) {
            notifyObservers("BORROW_FAILED", 
                "Book not available: " + book.getTitle());
            return false;
        }
        
        // Update book status (immutable)
        Book borrowedBook = book.withStatus(Book.BookStatus.BORROWED);
        bookCatalog.put(isbn, borrowedBook);
        
        // Update member
        List<String> borrowedBooks = new ArrayList<>(member.getBorrowedBookIsbns());
        borrowedBooks.add(isbn);
        Member updatedMember = new Member.Builder()
            .memberId(member.getMemberId())
            .name(member.getName())
            .email(member.getEmail())
            .memberType(member.getMemberType())
            .registrationDate(member.getRegistrationDate())
            .borrowedBookIsbns(borrowedBooks)
            .build();
        members.put(memberId, updatedMember);
        
        // Create transaction
        Transaction transaction = new Transaction.Builder()
            .transactionId(generateTransactionId())
            .memberId(memberId)
            .bookIsbn(isbn)
            .type(Transaction.TransactionType.BORROW)
            .transactionDate(LocalDateTime.now())
            .dueDate(LocalDateTime.now().plusDays(14))
            .build();
        transactions.add(transaction);
        
        // Notify observers
        notifyObservers("BOOK_BORROWED", 
            member.getName() + " borrowed " + book.getTitle());
        
        return true;
    }
    
    public boolean returnBook(String memberId, String isbn) {
        Optional<Member> memberOpt = getMember(memberId);
        Optional<Book> bookOpt = getBook(isbn);
        
        if (!memberOpt.isPresent() || !bookOpt.isPresent()) {
            notifyObservers("RETURN_FAILED", "Member or book not found");
            return false;
        }
        
        Member member = memberOpt.get();
        Book book = bookOpt.get();
        
        if (!member.getBorrowedBookIsbns().contains(isbn)) {
            notifyObservers("RETURN_FAILED", 
                "Member hasn't borrowed this book");
            return false;
        }
        
        // Update book status
        Book returnedBook = book.withStatus(Book.BookStatus.AVAILABLE);
        bookCatalog.put(isbn, returnedBook);
        
        // Update member
        List<String> borrowedBooks = new ArrayList<>(member.getBorrowedBookIsbns());
        borrowedBooks.remove(isbn);
        Member updatedMember = new Member.Builder()
            .memberId(member.getMemberId())
            .name(member.getName())
            .email(member.getEmail())
            .memberType(member.getMemberType())
            .registrationDate(member.getRegistrationDate())
            .borrowedBookIsbns(borrowedBooks)
            .build();
        members.put(memberId, updatedMember);
        
        // Create transaction
        Transaction transaction = new Transaction.Builder()
            .transactionId(generateTransactionId())
            .memberId(memberId)
            .bookIsbn(isbn)
            .type(Transaction.TransactionType.RETURN)
            .transactionDate(LocalDateTime.now())
            .build();
        transactions.add(transaction);
        
        // Notify observers
        notifyObservers("BOOK_RETURNED", 
            member.getName() + " returned " + book.getTitle());
        
        return true;
    }
    
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }
    
    // For testing
    public void reset() {
        bookCatalog.clear();
        members.clear();
        transactions.clear();
        observers.clear();
    }
}