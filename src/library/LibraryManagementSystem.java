package library;

import library.model.Book;
import library.model.Member;
import library.model.Transaction;
import library.service.LibraryService;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Phase 2 - Commit 5: Main Application UPDATED for Refactored Classes
 * Uses Builder Pattern and Enums
 */
public class LibraryManagementSystem {
    
    private static Scanner scanner = new Scanner(System.in);
    private static LibraryService service = LibraryService.getInstance();
    
    public static void main(String[] args) {
        System.out.println("=== Library Management System - Phase 2 ===");
        System.out.println("Refactored Implementation\n");
        
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
                    searchBooks();
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
        System.out.println("3. Search Books");
        System.out.println("4. View All Members");
        System.out.println("5. Add Member");
        System.out.println("6. Borrow Book");
        System.out.println("7. Return Book");
        System.out.println("8. View Transaction History");
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
        
        // Using Builder Pattern
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
    
    private static void searchBooks() {
        System.out.println("\n--- Search Books ---");
        System.out.println("1. By Title");
        System.out.println("2. By Author");
        System.out.print("Choice: ");
        int choice = getChoice();
        
        System.out.print("Search term: ");
        String term = scanner.nextLine();
        
        List<Book> results;
        if (choice == 1) {
            results = service.searchBooksByTitle(term);
        } else if (choice == 2) {
            results = service.searchBooksByAuthor(term);
        } else {
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
        
        // Using Builder Pattern
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
        
        service.borrowBook(memberId, isbn);
    }
    
    private static void returnBook() {
        System.out.println("\n--- Return Book ---");
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Book ISBN: ");
        String isbn = scanner.nextLine();
        
        service.returnBook(memberId, isbn);
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
    
    private static void initializeSampleData() {
        // Add sample books using Builder Pattern
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
        
        // Add sample members using Builder Pattern
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
        
        System.out.println("Sample data initialized.");
    }
}