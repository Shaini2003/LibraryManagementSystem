package library;

import library.model.Book;
import library.model.Member;
import library.model.Transaction;
import library.observer.ConsoleObserver;
import library.reflection.ReflectionAnalyzer;
import library.service.LibraryService;
import library.strategy.AuthorSearchStrategy;
import library.strategy.ISBNSearchStrategy;
import library.strategy.TitleSearchStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Phase 5 - FINAL: Complete Library Management System
 * 
 * ALL 5 ASPECTS IMPLEMENTED:
 * ✓ Bad Smells - Eliminated
 * ✓ Refactoring - Applied
 * ✓ Design Patterns - 4 patterns implemented
 * ✓ Reflection - Full implementation
 * ✓ Functional Programming - Comprehensive features
 */
public class LibraryManagementSystem {
    
    private static Scanner scanner = new Scanner(System.in);
    private static LibraryService service = LibraryService.getInstance();
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║  Library Management System - COMPLETE IMPLEMENTATION   ║");
        System.out.println("║  All 5 Aspects: Bad Smells, Refactoring, Patterns,    ║");
        System.out.println("║                 Reflection, Functional Programming     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
        
        // Observer Pattern
        service.registerObserver(new ConsoleObserver());
        
        initializeSampleData();
        
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    viewAllBooks();
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    searchBooksWithStrategy();
                    break;
                case 4:
                    viewAllMembers();
                    break;
                case 5:
                    addMember();
                    break;
                case 6:
                    borrowBook();
                    break;
                case 7:
                    returnBook();
                    break;
                case 8:
                    viewTransactionHistory();
                    break;
                case 9:
                    viewStatistics();
                    break;
                case 10:
                    demonstrateReflection();
                    break;
                case 11:
                    demonstrateFunctionalProgramming();
                    break;
                case 0:
                    running = false;
                    System.out.println("\n✓ Thank you for using Library Management System!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void showMenu() {
        System.out.println("\n╔════════════════════ MAIN MENU ═══════════════════════╗");
        System.out.println("║  1. View All Books                                   ║");
        System.out.println("║  2. Add Book                                         ║");
        System.out.println("║  3. Search Books (Strategy Pattern)                  ║");
        System.out.println("║  4. View All Members                                 ║");
        System.out.println("║  5. Add Member                                       ║");
        System.out.println("║  6. Borrow Book                                      ║");
        System.out.println("║  7. Return Book                                      ║");
        System.out.println("║  8. View Transaction History                         ║");
        System.out.println("║  9. View Statistics (Functional Programming) ✨      ║");
        System.out.println("║ 10. Demonstrate Reflection 🔍                        ║");
        System.out.println("║ 11. Demonstrate Functional Programming 🚀            ║");
        System.out.println("║  0. Exit                                             ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.print("Choice: ");
    }
    
    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }
    
    private static void viewAllBooks() {
        System.out.println("\n--- All Books ---");
        List<Book> books = service.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            for (Book book : books) {
                System.out.println(book.getFormattedInfo());
                System.out.println("---");
            }
        }
    }
    
    private static void addBook() {
        System.out.println("\n--- Add Book ---");
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        
        Book book = new Book.Builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .category(category)
            .publishDate(LocalDate.now())
            .build();
        
        service.addBook(book);
        System.out.println("✓ Book added successfully!");
    }
    
    private static void searchBooksWithStrategy() {
        System.out.println("\n--- Search Books (Strategy Pattern) ---");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by ISBN");
        System.out.print("Choice: ");
        int choice = getChoice();
        
        System.out.print("Search term: ");
        String term = scanner.nextLine();
        
        List<Book> results;
        
        switch (choice) {
            case 1:
                results = service.searchBooks(new TitleSearchStrategy(), term);
                System.out.println("📖 Using TitleSearchStrategy");
                break;
            case 2:
                results = service.searchBooks(new AuthorSearchStrategy(), term);
                System.out.println("✍️  Using AuthorSearchStrategy");
                break;
            case 3:
                results = service.searchBooks(new ISBNSearchStrategy(), term);
                System.out.println("🔢 Using ISBNSearchStrategy");
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("\n✓ Found " + results.size() + " book(s):");
            for (Book book : results) {
                System.out.println("  - " + book);
            }
        }
    }
    
    private static void viewAllMembers() {
        System.out.println("\n--- All Members ---");
        List<Member> members = service.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members registered.");
        } else {
            for (Member member : members) {
                System.out.println(String.format("%s - Books: %d/%d",
                    member, member.getBorrowedCount(), 
                    member.getMemberType().getMaxBooksAllowed()));
            }
        }
    }
    
    private static void addMember() {
        System.out.println("\n--- Add Member ---");
        System.out.print("Member ID: ");
        String id = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.println("Type (STUDENT/FACULTY/GUEST): ");
        String typeStr = scanner.nextLine().toUpperCase();
        
        Member.MemberType type;
        try {
            type = Member.MemberType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid member type. Defaulting to STUDENT.");
            type = Member.MemberType.STUDENT;
        }
        
        Member member = new Member.Builder()
            .memberId(id)
            .name(name)
            .email(email)
            .memberType(type)
            .registrationDate(LocalDate.now())
            .build();
        
        service.addMember(member);
        System.out.println("✓ Member added successfully!");
    }
    
    private static void borrowBook() {
        System.out.println("\n--- Borrow Book ---");
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Book ISBN: ");
        String isbn = scanner.nextLine();
        
        boolean success = service.borrowBook(memberId, isbn);
        if (success) {
            System.out.println("✓ Success!");
        } else {
            System.out.println("✗ Failed. Check the event log above.");
        }
    }
    
    private static void returnBook() {
        System.out.println("\n--- Return Book ---");
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Book ISBN: ");
        String isbn = scanner.nextLine();
        
        boolean success = service.returnBook(memberId, isbn);
        if (success) {
            System.out.println("✓ Success!");
        } else {
            System.out.println("✗ Failed. Check the event log above.");
        }
    }
    
    private static void viewTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        List<Transaction> transactions = service.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
        } else {
            transactions.forEach(System.out::println);  // Method reference
        }
    }
    
    // NEW: Statistics using Functional Programming
    private static void viewStatistics() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║         LIBRARY STATISTICS (Functional Programming)   ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
        
        Map<String, Object> stats = service.getStatistics();
        
        System.out.println("📊 Overall Statistics:");
        System.out.println("   Total Books: " + stats.get("totalBooks"));
        System.out.println("   Total Members: " + stats.get("totalMembers"));
        System.out.println("   Total Transactions: " + stats.get("totalTransactions"));
        System.out.println("   Available Books: " + stats.get("availableBooks"));
        System.out.println("   Borrowed Books: " + stats.get("borrowedBooks"));
        System.out.println("   Overdue Books: " + stats.get("overdueBooks"));
        
        System.out.println("\n📚 Books by Category:");
        @SuppressWarnings("unchecked")
        Map<String, Long> byCategory = (Map<String, Long>) stats.get("booksByCategory");
        byCategory.forEach((category, count) -> 
            System.out.println("   " + category + ": " + count));
        
        System.out.println("\n📖 Books by Status:");
        @SuppressWarnings("unchecked")
        Map<Book.BookStatus, Long> byStatus = (Map<Book.BookStatus, Long>) stats.get("booksByStatus");
        byStatus.forEach((status, count) -> 
            System.out.println("   " + status + ": " + count));
        
        System.out.println("\n✍️  All Authors:");
        service.getAllAuthors().forEach(author -> 
            System.out.println("   - " + author));
        
        service.getMostPopularCategory().ifPresent(category ->
            System.out.println("\n🏆 Most Popular Category: " + category));
        
        service.getMemberWithMostBorrows().ifPresent(member ->
            System.out.println("🏆 Most Active Member: " + member.getName() + 
                             " (" + member.getBorrowedCount() + " books)"));
    }
    
    // Reflection Demonstration
    private static void demonstrateReflection() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          REFLECTION DEMONSTRATION 🔍                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
        
        Class<?>[] classesToAnalyze = {
            Book.class,
            Member.class,
            Transaction.class,
            LibraryService.class
        };
        
        for (Class<?> clazz : classesToAnalyze) {
            ReflectionAnalyzer.ClassInfo info = ReflectionAnalyzer.analyzeClass(clazz);
            System.out.println(info);
        }
        
        System.out.println("═══════════════════════════════════════");
        System.out.println("Dynamic Method Invocation:");
        System.out.println("═══════════════════════════════════════");
        
        try {
            Book testBook = new Book.Builder()
                .isbn("REF-001")
                .title("Reflection Test")
                .author("Test Author")
                .category("Testing")
                .build();
            
            Object title = ReflectionAnalyzer.invokeMethod(testBook, "getTitle");
            System.out.println("✓ getTitle() via reflection: " + title);
            
            Object isbn = ReflectionAnalyzer.getFieldValue(testBook, "isbn");
            System.out.println("✓ Field 'isbn' via reflection: " + isbn);
            
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    // Functional Programming Demonstration
    private static void demonstrateFunctionalProgramming() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║     FUNCTIONAL PROGRAMMING DEMONSTRATION 🚀           ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
        
        System.out.println("1️⃣  LAMBDA EXPRESSIONS & PREDICATES:");
        System.out.println("   Filter available books:");
        List<Book> availableBooks = service.filterBooks(
            book -> book.getStatus() == Book.BookStatus.AVAILABLE);
        System.out.println("   ✓ Found " + availableBooks.size() + " available books\n");
        
        System.out.println("2️⃣  STREAM API - map() operation:");
        System.out.println("   All book titles:");
        service.getAllBooks().stream()
            .map(Book::getTitle)  // Method reference
            .sorted()
            .limit(5)
            .forEach(title -> System.out.println("   - " + title));
        
        System.out.println("\n3️⃣  METHOD REFERENCES:");
        System.out.println("   All authors (using Book::getAuthor):");
        service.getAllAuthors().stream()
            .limit(5)
            .forEach(author -> System.out.println("   - " + author));
        
        System.out.println("\n4️⃣  COLLECTORS - groupingBy:");
        System.out.println("   Books grouped by category:");
        service.getBooksByCategory().forEach((category, count) ->
            System.out.println("   " + category + ": " + count + " books"));
        
        System.out.println("\n5️⃣  OPTIONAL - Safe null handling:");
        service.getMostPopularCategory().ifPresentOrElse(
            category -> System.out.println("   Most popular: " + category),
            () -> System.out.println("   No data available"));
        
        System.out.println("\n6️⃣  HIGHER-ORDER FUNCTIONS:");
        System.out.println("   Custom filter (books with 'Java' in title):");
        long javaBooks = service.countBooks(
            book -> book.getTitle().toLowerCase().contains("java"));
        System.out.println("   ✓ Found " + javaBooks + " Java books\n");
        
        System.out.println("7️⃣  IMMUTABILITY:");
        Book original = service.getAllBooks().get(0);
        Book modified = original.withStatus(Book.BookStatus.MAINTENANCE);
        System.out.println("   Original status: " + original.getStatus());
        System.out.println("   Modified status: " + modified.getStatus());
        System.out.println("   ✓ Original unchanged (immutable)\n");
        
        System.out.println("8️⃣  STREAM OPERATIONS - anyMatch, allMatch:");
        boolean hasOverdue = service.hasOverdueBooks();
        boolean allAvailable = service.allBooksAvailable();
        System.out.println("   Has overdue books: " + hasOverdue);
        System.out.println("   All books available: " + allAvailable);
        
        System.out.println("\n✅ All functional programming features demonstrated!");
    }
    
    private static void initializeSampleData() {
        service.addBook(new Book.Builder()
            .isbn("978-0134685991")
            .title("Effective Java")
            .author("Joshua Bloch")
            .category("Programming")
            .publishDate(LocalDate.of(2018, 1, 6))
            .build());
        
        service.addBook(new Book.Builder()
            .isbn("978-0596007126")
            .title("Head First Design Patterns")
            .author("Eric Freeman")
            .category("Software Engineering")
            .publishDate(LocalDate.of(2004, 10, 25))
            .build());
        
        service.addBook(new Book.Builder()
            .isbn("978-0132350884")
            .title("Clean Code")
            .author("Robert C. Martin")
            .category("Software Engineering")
            .publishDate(LocalDate.of(2008, 8, 1))
            .build());
        
        service.addBook(new Book.Builder()
            .isbn("978-0201633610")
            .title("Design Patterns")
            .author("Gang of Four")
            .category("Software Engineering")
            .publishDate(LocalDate.of(1994, 10, 31))
            .build());
        
        service.addBook(new Book.Builder()
            .isbn("978-0135957059")
            .title("The Pragmatic Programmer")
            .author("David Thomas")
            .category("Programming")
            .publishDate(LocalDate.of(2019, 9, 13))
            .build());
        
        service.addMember(new Member.Builder()
            .memberId("M001")
            .name("John Doe")
            .email("john@example.com")
            .memberType(Member.MemberType.STUDENT)
            .build());
        
        service.addMember(new Member.Builder()
            .memberId("M002")
            .name("Jane Smith")
            .email("jane@example.com")
            .memberType(Member.MemberType.FACULTY)
            .build());
        
        service.addMember(new Member.Builder()
            .memberId("M003")
            .name("Bob Wilson")
            .email("bob@example.com")
            .memberType(Member.MemberType.STUDENT)
            .build());
        
        System.out.println("✓ Sample data initialized.\n");
    }
}