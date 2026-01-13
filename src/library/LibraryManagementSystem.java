package library;

import library.model.Book;
import library.model.Member;
import library.service.LibraryService;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Phase 1: Basic Library Management System
 * Simple implementation with bad smells present
 */
public class LibraryManagementSystem {
    
    private static Scanner scanner = new Scanner(System.in);
    private static LibraryService service = LibraryService.instance;
    
    public static void main(String[] args) {
        System.out.println("=== Library Management System - Phase 1 ===");
        System.out.println("Basic Implementation (Contains Bad Smells)\n");
        
        // Initialize with sample data
        initializeSampleData();
        
        // Main menu loop
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
                System.out.println(book.getInfo());
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
        
        Book book = new Book(isbn, title, author, category);
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
                System.out.println(member + " - Books: " + 
                                 member.borrowedBookIsbns.size());
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
        String type = scanner.nextLine();
        
        Member member = new Member(id, name, email, type, LocalDate.now());
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
    
    private static void initializeSampleData() {
        // Add sample books
        service.addBook(new Book("978-0134685991", "Effective Java", 
                                "Joshua Bloch", "Programming"));
        service.addBook(new Book("978-0596007126", "Head First Design Patterns", 
                                "Eric Freeman", "Software Engineering"));
        service.addBook(new Book("978-0132350884", "Clean Code", 
                                "Robert C. Martin", "Software Engineering"));
        
        // Add sample members
        service.addMember(new Member("M001", "John Doe", "john@example.com", 
                                    "STUDENT", LocalDate.now()));
        service.addMember(new Member("M002", "Jane Smith", "jane@example.com", 
                                    "FACULTY", LocalDate.now()));
        
        System.out.println("Sample data initialized.");
    }
}