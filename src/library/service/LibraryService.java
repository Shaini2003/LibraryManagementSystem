package library.service;

import library.annotations.*;
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
 * LibraryService with Custom Annotations
 */
@DesignPattern(
    pattern = "Singleton Pattern",
    description = "Ensures single instance of library service",
    benefits = {"Centralized state", "Thread-safe", "Global access point"}
)
@Author(
    name = "Library Team",
    date = "2025-01-15",
    version = "3.0",
    modifications = {"Added Observer Pattern", "Added Strategy Pattern", "Added FP features"}
)
public class LibraryService {
    
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
    
    @PerformanceMonitor(operationName = "Get Singleton Instance")
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
    
    // Observer Pattern methods
    public void registerObserver(LibraryObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(LibraryObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers(String event, String details) {
        observers.forEach(observer -> observer.update(event, details));
    }
    
    // Book Management
    @PerformanceMonitor(operationName = "Add Book", logExecution = true)
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
    
    // Functional Programming
    public List<Book> filterBooks(Predicate<Book> predicate) {
        return bookCatalog.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
    
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
    @PerformanceMonitor(operationName = "Add Member", logExecution = true)
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
    
    public List<Member> getMembersByType(Member.MemberType type) {
        return members.values().stream()
            .filter(member -> member.getMemberType() == type)
            .collect(Collectors.toList());
    }
    
    public List<Member> getMembersWhoCanBorrowMore() {
        return members.values().stream()
            .filter(Member::canBorrowMore)
            .collect(Collectors.toList());
    }
    
    // Transaction Operations
    @PerformanceMonitor(
        operationName = "Borrow Book Operation",
        logExecution = true,
        expectedMaxTime = 500
    )
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
        
        Book borrowedBook = book.withStatus(Book.BookStatus.BORROWED);
        bookCatalog.put(isbn, borrowedBook);
        
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
    
    @PerformanceMonitor(
        operationName = "Return Book Operation",
        logExecution = true,
        expectedMaxTime = 500
    )
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
        
        Book returnedBook = book.withStatus(Book.BookStatus.AVAILABLE);
        bookCatalog.put(isbn, returnedBook);
        
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
    
    // Functional Programming Features
    public List<Transaction> getOverdueTransactions() {
        return transactions.stream()
            .filter(Transaction::isOverdue)
            .filter(t -> t.getType() == Transaction.TransactionType.BORROW)
            .collect(Collectors.toList());
    }
    
    public Map<String, Long> getBooksByCategory() {
        return bookCatalog.values().stream()
            .collect(Collectors.groupingBy(
                Book::getCategory,
                Collectors.counting()
            ));
    }
    
    public Map<Book.BookStatus, Long> getBooksByStatus() {
        return bookCatalog.values().stream()
            .collect(Collectors.groupingBy(
                Book::getStatus,
                Collectors.counting()
            ));
    }
    
    public List<String> getAllAuthors() {
        return bookCatalog.values().stream()
            .map(Book::getAuthor)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
    
    public List<String> getAllCategories() {
        return bookCatalog.values().stream()
            .map(Book::getCategory)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
    
    public long getTotalBorrowedBooksCount() {
        return members.values().stream()
            .mapToInt(Member::getBorrowedCount)
            .sum();
    }
    
    public Optional<Member> getMemberWithMostBorrows() {
        return members.values().stream()
            .max(Comparator.comparingInt(Member::getBorrowedCount));
    }
    
    public Optional<String> getMostPopularCategory() {
        return getBooksByCategory().entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
    }
    
    public List<Book> getBooksSortedByTitle() {
        return bookCatalog.values().stream()
            .sorted(Comparator.comparing(Book::getTitle))
            .collect(Collectors.toList());
    }
    
    public List<Book> getBooksSortedByAuthor() {
        return bookCatalog.values().stream()
            .sorted(Comparator.comparing(Book::getAuthor))
            .collect(Collectors.toList());
    }
    
    public List<Transaction> getRecentTransactions(int count) {
        return transactions.stream()
            .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
            .limit(count)
            .collect(Collectors.toList());
    }
    
    public boolean hasOverdueBooks() {
        return transactions.stream()
            .filter(t -> t.getType() == Transaction.TransactionType.BORROW)
            .anyMatch(Transaction::isOverdue);
    }
    
    public boolean allBooksAvailable() {
        return bookCatalog.values().stream()
            .allMatch(book -> book.getStatus() == Book.BookStatus.AVAILABLE);
    }
    
    public long countBooks(Predicate<Book> predicate) {
        return bookCatalog.values().stream()
            .filter(predicate)
            .count();
    }
    
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