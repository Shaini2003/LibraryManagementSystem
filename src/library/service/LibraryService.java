package library.service;

import library.model.Book;
import library.model.Member;
import library.model.Transaction;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Phase 2 - Commit 4: LibraryService REFACTORED
 * 
 * REFACTORINGS APPLIED:
 * 1. Extract Method - broke down long methods
 * 2. Use Builder Pattern for object creation
 * 3. Use Enums instead of Strings
 * 4. Use Transaction objects instead of string logging
 * 5. Encapsulate Collections
 * 6. Remove Duplicate Code
 */
public class LibraryService {
    // Better singleton (will be improved in Phase 3)
    private static LibraryService instance;
    
    private final Map<String, Book> bookCatalog;
    private final Map<String, Member> members;
    private final List<Transaction> transactions;
    
    private LibraryService() {
        this.bookCatalog = new HashMap<>();
        this.members = new HashMap<>();
        this.transactions = new ArrayList<>();
    }
    
    public static synchronized LibraryService getInstance() {
        if (instance == null) {
            instance = new LibraryService();
        }
        return instance;
    }
    
    // Book Management - Refactored
    public void addBook(Book book) {
        bookCatalog.put(book.getIsbn(), book);
        System.out.println("Book added: " + book.getTitle());
    }
    
    public Optional<Book> getBook(String isbn) {
        return Optional.ofNullable(bookCatalog.get(isbn));
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(bookCatalog.values());
    }
    
    // Member Management - Refactored
    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
        System.out.println("Member registered: " + member.getName());
    }
    
    public Optional<Member> getMember(String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }
    
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }
    
    // Refactoring: Extracted validation methods
    private boolean validateMemberExists(String memberId) {
        return members.containsKey(memberId);
    }
    
    private boolean validateBookExists(String isbn) {
        return bookCatalog.containsKey(isbn);
    }
    
    private boolean validateBookAvailable(Book book) {
        return book.getStatus() == Book.BookStatus.AVAILABLE;
    }
    
    private boolean validateBorrowingLimit(Member member) {
        return member.canBorrowMore();
    }
    
    // Refactoring: Extract Method - borrowBook broken into smaller methods
    public boolean borrowBook(String memberId, String isbn) {
        if (!validateMemberExists(memberId)) {
            System.out.println("Error: Member not found");
            return false;
        }
        
        if (!validateBookExists(isbn)) {
            System.out.println("Error: Book not found");
            return false;
        }
        
        Member member = members.get(memberId);
        Book book = bookCatalog.get(isbn);
        
        if (!validateBookAvailable(book)) {
            System.out.println("Error: Book not available");
            return false;
        }
        
        if (!validateBorrowingLimit(member)) {
            System.out.println("Error: Borrowing limit reached");
            return false;
        }
        
        // Perform borrow operation
        executeBorrowOperation(member, book);
        return true;
    }
    
    // Refactoring: Extract Method
    private void executeBorrowOperation(Member member, Book book) {
        // Update book status using immutable pattern
        Book borrowedBook = book.withStatus(Book.BookStatus.BORROWED);
        bookCatalog.put(book.getIsbn(), borrowedBook);
        
        // Update member
        List<String> borrowedBooks = new ArrayList<>(member.getBorrowedBookIsbns());
        borrowedBooks.add(book.getIsbn());
        Member updatedMember = new Member.Builder()
            .memberId(member.getMemberId())
            .name(member.getName())
            .email(member.getEmail())
            .memberType(member.getMemberType())
            .registrationDate(member.getRegistrationDate())
            .borrowedBookIsbns(borrowedBooks)
            .build();
        members.put(member.getMemberId(), updatedMember);
        
        // Create transaction record
        createTransaction(member.getMemberId(), book.getIsbn(), 
                         Transaction.TransactionType.BORROW);
        
        System.out.println("Book borrowed successfully: " + book.getTitle());
    }
    
    // Refactoring: Extract Method
    private void createTransaction(String memberId, String isbn, 
                                   Transaction.TransactionType type) {
        Transaction transaction = new Transaction.Builder()
            .transactionId(generateTransactionId())
            .memberId(memberId)
            .bookIsbn(isbn)
            .type(type)
            .transactionDate(LocalDateTime.now())
            .dueDate(type == Transaction.TransactionType.BORROW ? 
                    LocalDateTime.now().plusDays(14) : null)
            .build();
        transactions.add(transaction);
    }
    
    // Refactoring: Simplified returnBook method
    public boolean returnBook(String memberId, String isbn) {
        if (!validateMemberExists(memberId)) {
            System.out.println("Error: Member not found");
            return false;
        }
        
        if (!validateBookExists(isbn)) {
            System.out.println("Error: Book not found");
            return false;
        }
        
        Member member = members.get(memberId);
        Book book = bookCatalog.get(isbn);
        
        if (!member.getBorrowedBookIsbns().contains(isbn)) {
            System.out.println("Error: Member hasn't borrowed this book");
            return false;
        }
        
        executeReturnOperation(member, book);
        return true;
    }
    
    // Refactoring: Extract Method
    private void executeReturnOperation(Member member, Book book) {
        // Update book status
        Book returnedBook = book.withStatus(Book.BookStatus.AVAILABLE);
        bookCatalog.put(book.getIsbn(), returnedBook);
        
        // Update member
        List<String> borrowedBooks = new ArrayList<>(member.getBorrowedBookIsbns());
        borrowedBooks.remove(book.getIsbn());
        Member updatedMember = new Member.Builder()
            .memberId(member.getMemberId())
            .name(member.getName())
            .email(member.getEmail())
            .memberType(member.getMemberType())
            .registrationDate(member.getRegistrationDate())
            .borrowedBookIsbns(borrowedBooks)
            .build();
        members.put(member.getMemberId(), updatedMember);
        
        // Create transaction
        createTransaction(member.getMemberId(), book.getIsbn(), 
                         Transaction.TransactionType.RETURN);
        
        System.out.println("Book returned successfully: " + book.getTitle());
    }
    
    // Refactoring: Unified search method (will be improved with Strategy in Phase 3)
    public List<Book> searchBooksByTitle(String title) {
        return searchBooks(book -> 
            book.getTitle().toLowerCase().contains(title.toLowerCase()));
    }
    
    public List<Book> searchBooksByAuthor(String author) {
        return searchBooks(book -> 
            book.getAuthor().toLowerCase().contains(author.toLowerCase()));
    }
    
    // Refactoring: Extract common search logic
    private List<Book> searchBooks(java.util.function.Predicate<Book> predicate) {
        List<Book> results = new ArrayList<>();
        for (Book book : bookCatalog.values()) {
            if (predicate.test(book)) {
                results.add(book);
            }
        }
        return results;
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
    }
}