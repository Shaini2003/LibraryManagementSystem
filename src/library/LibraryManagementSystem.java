package library;

import library.model.Book;
import library.model.Member;
import library.model.Transaction;
import library.observer.ConsoleObserver;
import library.service.LibraryService;
import library.strategy.AuthorSearchStrategy;
import library.strategy.ISBNSearchStrategy;
import library.strategy.TitleSearchStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Phase 3 - Commit 4: Main Application with Design Patterns
 * Demonstrates Observer and Strategy patterns in action
 */
public class LibraryManagementSystem {
    
    private static Scanner scanner = new Scanner(System.in);
    private static LibraryService service = LibraryService.getInstance();
    
    public static void main(String[] args) {
        System.out.println("=== Library Management System - Phase 3 ===");
        System.out.println("Design Patterns Implementation\n");
        
        // Observer Pattern - Register observer for event logging
        service.registerObserver(new ConsoleObserver());
        System.out.println("Observer registered for event notifications.\n");
        
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
                    demonstratePatterns();
                    break;
                case 0:
                    running = false;
                    System.out.println("Goodbye!");
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
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. View All Books");
        System.out.println("2. Add Book");
        System.out.println("3. Search Books (Strategy Pattern)");
        System.out.println("4. View All Members");
        System.out.println("5. Add Member");
        System.out.println("6. Borrow Book");
        System.out.println("7. Return Book");
        System.out.println("8. View Transaction History");
        System.out.println("9. Demonstrate Design Patterns");
        System.out.println("0. Exit");
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
        System.out.println("Book added successfully!");
    }
    
    // Strategy Pattern demonstration
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
        
        // Strategy Pattern - select strategy at runtime
        switch (choice) {
            case 1:
                results = service.searchBooks(new TitleSearchStrategy(), term);
                System.out.println("Using TitleSearchStrategy");
                break;
            case 2:
                results = service.searchBooks(new AuthorSearchStrategy(), term);
                System.out.println("Using AuthorSearchStrategy");
                break;
            case 3:
                results = service.searchBooks(new ISBNSearchStrategy(), term);
                System.out.println("Using ISBNSearchStrategy");
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("Found " + results.size() + " book(s):");
            for (Book book : results) {
                System.out.println("- " + book);
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
        System.out.println("Member added successfully!");
    }
    
    private static void borrowBook() {
        System.out.println("\n--- Borrow Book ---");
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Book ISBN: ");
        String isbn = scanner.nextLine();
        
        boolean success = service.borrowBook(memberId, isbn);
        if (success) {
            System.out.println("Success!");
        } else {
            System.out.println("Failed. Check the event log above.");
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
            System.out.println("Success!");
        } else {
            System.out.println("Failed. Check the event log above.");
        }
    }
    
    private static void viewTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        List<Transaction> transactions = service.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
        } else {
            for (Transaction txn : transactions) {
                System.out.println(txn);
            }
        }
    }
    
    private static void demonstratePatterns() {
        System.out.println("\n=== Design Patterns Demonstration ===");
        
        System.out.println("\n1. SINGLETON PATTERN:");
        LibraryService instance1 = LibraryService.getInstance();
        LibraryService instance2 = LibraryService.getInstance();
        System.out.println("Instance 1 == Instance 2: " + (instance1 == instance2));
        System.out.println("✓ Single instance guaranteed");
        
        System.out.println("\n2. BUILDER PATTERN:");
        System.out.println("Creating book with Builder:");
        Book demoBook = new Book.Builder()
            .isbn("DEMO-001")
            .title("Design Patterns Book")
            .author("Gang of Four")
            .category("Software Engineering")
            .build();
        System.out.println("✓ " + demoBook);
        
        System.out.println("\n3. OBSERVER PATTERN:");
        System.out.println("Events are logged automatically (see above)");
        System.out.println("✓ ConsoleObserver notified of all events");
        
        System.out.println("\n4. STRATEGY PATTERN:");
        System.out.println("Different search strategies can be used:");
        System.out.println("- TitleSearchStrategy");
        System.out.println("- AuthorSearchStrategy");
        System.out.println("- ISBNSearchStrategy");
        System.out.println("✓ Algorithm selected at runtime");
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
        
        System.out.println("Sample data initialized.\n");
    }
}