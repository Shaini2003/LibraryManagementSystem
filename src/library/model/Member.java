package library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Phase 1: Member class with intentional bad smells
 * BAD SMELLS PRESENT:
 * 1. Long Parameter List
 * 2. Public fields (no encapsulation)
 * 3. Magic numbers (3, 5, 1 for book limits)
 * 4. Mutable collections exposed
 * 5. Type checking instead of polymorphism
 */
public class Member {
    // BAD SMELL: Public mutable fields
    public String memberId;
    public String name;
    public String email;
    public LocalDate registrationDate;
    public String memberType; // BAD SMELL: String instead of enum
    public List<String> borrowedBookIsbns; // BAD SMELL: Exposed mutable collection
    
    // BAD SMELL: Long Parameter List
    public Member(String memberId, String name, String email, 
                  String memberType, LocalDate registrationDate) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.memberType = memberType;
        this.registrationDate = registrationDate;
        this.borrowedBookIsbns = new ArrayList<>();
    }
    
    // BAD SMELL: Magic Numbers and Type Checking
    public boolean canBorrowMore() {
        int limit;
        if (memberType.equals("STUDENT")) {
            limit = 3; // BAD SMELL: Magic number
        } else if (memberType.equals("FACULTY")) {
            limit = 5; // BAD SMELL: Magic number
        } else {
            limit = 1; // BAD SMELL: Magic number
        }
        return borrowedBookIsbns.size() < limit;
    }
    
    // BAD SMELL: Direct collection manipulation
    public void addBorrowedBook(String isbn) {
        borrowedBookIsbns.add(isbn);
    }
    
    public void removeBorrowedBook(String isbn) {
        borrowedBookIsbns.remove(isbn);
    }
    
    // BAD SMELL: Returns mutable collection
    public List<String> getBorrowedBooks() {
        return borrowedBookIsbns;
    }
    
    @Override
    public String toString() {
        return memberId + " - " + name + " (" + memberType + ")";
    }
}