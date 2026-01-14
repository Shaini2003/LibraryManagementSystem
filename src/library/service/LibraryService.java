package library.service;

import library.model.Book;
import library.model.Member;
import library.model.Transaction;
import library.observer.LibraryObserver;
import library.strategy.SearchStrategy;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Phase 5 - Commit 1: FINAL LibraryService with Functional Programming
 * 
 * ALL FEATURES IMPLEMENTED:
 * - Singleton Pattern
 * - Observer Pattern  
 * - Strategy Pattern
 * - Functional Programming (Streams, Lambdas, Method References)
 */
public class LibraryService {
    // Singleton Pattern
    private static volatile LibraryService instance;
    private static final Object lock = new Object();
    
    private final Map<String, Book> bookCatalog;
    private final Map<String, Member> members;
    private final List<Transaction> transactions;
    private final List<LibraryObserver> observers;
    
    private LibraryService() {
        this.bookCatalog = new HashMap<>();
        this.members = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.observers = new ArrayList<>();
    }
    
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
    
    // Observer Pattern
    public void registerObserver(LibraryObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(LibraryObserver observer) {
        observers.remove(observer);
    }
    
    // Functional Programming: forEach with lambda
    private void notifyObservers(String event, String details) {
        observers.forEach(observer -> observer.update(event, details));
    }
    
    // Book Management
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
    
    // Functional Programming: Higher-order function accepting Predicate
    public List<Book> filterBooks(Predicate<Book> predicate) {
        return bookCatalog.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
    
    // Functional Programming: Composed predicates
    public List<Book> getAvailableBooks() {
        return filterBooks(book -> book.getStatus() == Book.BookStatus.AVAILABLE);
    }
    
    public List<Book> getBooksByCategory(String category) {
        return filterBooks(book -> book.getCategory().equalsIgnoreCase(category));
    }
    
    public List<Book> getBooksByAuthor(String author) {
        return filterBooks(book -> book.getAuthor().toLowerCase()
                                        .contains(author.toLowerCase()));
    }
    
    // Strategy Pattern
    public List<Book> searchBooks(SearchStrategy strategy, String query) {
        return strategy.search(new ArrayList<>(bookCatalog.values()), query);
    }
    
    // Member Management
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
    
    // Functional Programming: Filter members
    public List<Member> getMembersByType(Member.MemberType type) {
        return members.values().stream()
            .filter(member -> member.getMemberType() == type)
            .collect(Collectors.toList());
    }
    
    // Functional Programming: Find members who can borrow more
    public List<Member> getMembersWhoCanBorrowMore() {
        return members.values().stream()
            .filter(Member::canBorrowMore)  // Method reference
            .collect(Collectors.toList());
    }
    
    // Transaction Operations
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
        
        // Update book (immutable)
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
        
        // Update book
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
        
        notifyObservers("BOOK_RETURNED", 
            member.getName() + " returned " + book.getTitle());
        
        return true;
    }
    
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }
    
    // ═══════════════════════════════════════════════════════════════
    // FUNCTIONAL PROGRAMMING FEATURES
    // ═══════════════════════════════════════════════════════════════
    
    /**
     * FP: Get overdue transactions using Stream API and method references
     */
    public List<Transaction> getOverdueTransactions() {
        return transactions.stream()
            .filter(Transaction::isOverdue)  // Method reference
            .filter(t -> t.getType() == Transaction.TransactionType.BORROW)
            .collect(Collectors.toList());
    }
    
    /**
     * FP: Group books by category with counting
     */
    public Map<String, Long> getBooksByCategory() {
        return bookCatalog.values().stream()
            .collect(Collectors.groupingBy(
                Book::getCategory,  // Method reference
                Collectors.counting()
            ));
    }
    
    /**
     * FP: Group books by status
     */
    public Map<Book.BookStatus, Long> getBooksByStatus() {
        return bookCatalog.values().stream()
            .collect(Collectors.groupingBy(
                Book::getStatus,
                Collectors.counting()
            ));
    }
    
    /**
     * FP: Get all unique authors (distinct + map)
     */
    public List<String> getAllAuthors() {
        return bookCatalog.values().stream()
            .map(Book::getAuthor)  // Method reference
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
    
    /**
     * FP: Get all unique categories
     */
    public List<String> getAllCategories() {
        return bookCatalog.values().stream()
            .map(Book::getCategory)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
    
    /**
     * FP: Calculate total borrowed books count
     */
    public long getTotalBorrowedBooksCount() {
        return members.values().stream()
            .mapToInt(Member::getBorrowedCount)
            .sum();
    }
    
    /**
     * FP: Get member with most borrowed books
     */
    public Optional<Member> getMemberWithMostBorrows() {
        return members.values().stream()
            .max(Comparator.comparingInt(Member::getBorrowedCount));
    }
    
    /**
     * FP: Get most popular category
     */
    public Optional<String> getMostPopularCategory() {
        return getBooksByCategory().entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
    }
    
    /**
     * FP: Get books sorted by title
     */
    public List<Book> getBooksSortedByTitle() {
        return bookCatalog.values().stream()
            .sorted(Comparator.comparing(Book::getTitle))
            .collect(Collectors.toList());
    }
    
    /**
     * FP: Get books sorted by author
     */
    public List<Book> getBooksSortedByAuthor() {
        return bookCatalog.values().stream()
            .sorted(Comparator.comparing(Book::getAuthor))
            .collect(Collectors.toList());
    }
    
    /**
     * FP: Get recent transactions (last N)
     */
    public List<Transaction> getRecentTransactions(int count) {
        return transactions.stream()
            .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
            .limit(count)
            .collect(Collectors.toList());
    }
    
    /**
     * FP: Check if any book is overdue
     */
    public boolean hasOverdueBooks() {
        return transactions.stream()
            .filter(t -> t.getType() == Transaction.TransactionType.BORROW)
            .anyMatch(Transaction::isOverdue);
    }
    
    /**
     * FP: Check if all books are available
     */
    public boolean allBooksAvailable() {
        return bookCatalog.values().stream()
            .allMatch(book -> book.getStatus() == Book.BookStatus.AVAILABLE);
    }
    
    /**
     * FP: Count books matching predicate
     */
    public long countBooks(Predicate<Book> predicate) {
        return bookCatalog.values().stream()
            .filter(predicate)
            .count();
    }
    
    /**
     * FP: Get statistics summary
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks", bookCatalog.size());
        stats.put("totalMembers", members.size());
        stats.put("totalTransactions", transactions.size());
        stats.put("availableBooks", getAvailableBooks().size());
        stats.put("borrowedBooks", getTotalBorrowedBooksCount());
        stats.put("overdueBooks", getOverdueTransactions().size());
        stats.put("booksByCategory", getBooksByCategory());
        stats.put("booksByStatus", getBooksByStatus());
        return stats;
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }
    
    public void reset() {
        bookCatalog.clear();
        members.clear();
        transactions.clear();
        observers.clear();
    }
}