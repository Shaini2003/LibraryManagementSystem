package library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Phase 2 - Commit 2: Member class FULLY REFACTORED
 * 
 * REFACTORINGS APPLIED:
 * 1. Replace Constructor with Builder Pattern
 * 2. Encapsulate Fields
 * 3. Replace Type Code with Enum + Eliminate Magic Numbers
 * 4. Encapsulate Collection
 * 5. Extract Method
 */
public class Member {
    private final String memberId;
    private final String name;
    private final String email;
    private final LocalDate registrationDate;
    private final MemberType memberType;
    private final List<String> borrowedBookIsbns;
    
    // Refactoring: Replace Type Code with Enum (eliminates magic numbers)
    public enum MemberType {
        STUDENT(3), FACULTY(5), GUEST(1);
        
        private final int maxBooksAllowed;
        
        MemberType(int maxBooksAllowed) {
            this.maxBooksAllowed = maxBooksAllowed;
        }
        
        public int getMaxBooksAllowed() {
            return maxBooksAllowed;
        }
    }
    
    private Member(Builder builder) {
        this.memberId = builder.memberId;
        this.name = builder.name;
        this.email = builder.email;
        this.registrationDate = builder.registrationDate;
        this.memberType = builder.memberType;
        this.borrowedBookIsbns = new ArrayList<>(builder.borrowedBookIsbns);
    }
    
    // Getters
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public MemberType getMemberType() { return memberType; }
    
    // Refactoring: Encapsulate Collection
    public List<String> getBorrowedBookIsbns() { 
        return Collections.unmodifiableList(borrowedBookIsbns); 
    }
    
    // Refactoring: Extract Method
    public boolean canBorrowMore() {
        return getBorrowedCount() < getMaxAllowedBooks();
    }
    
    public int getBorrowedCount() {
        return borrowedBookIsbns.size();
    }
    
    private int getMaxAllowedBooks() {
        return memberType.getMaxBooksAllowed();
    }
    
    // Builder Pattern
    public static class Builder {
        private String memberId;
        private String name;
        private String email;
        private LocalDate registrationDate = LocalDate.now();
        private MemberType memberType = MemberType.STUDENT;
        private List<String> borrowedBookIsbns = new ArrayList<>();
        
        public Builder memberId(String memberId) {
            this.memberId = memberId;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder registrationDate(LocalDate registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }
        
        public Builder memberType(MemberType memberType) {
            this.memberType = memberType;
            return this;
        }
        
        public Builder borrowedBookIsbns(List<String> borrowedBookIsbns) {
            this.borrowedBookIsbns = new ArrayList<>(borrowedBookIsbns);
            return this;
        }
        
        public Member build() {
            Objects.requireNonNull(memberId, "Member ID cannot be null");
            Objects.requireNonNull(name, "Name cannot be null");
            return new Member(this);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return memberId.equals(member.memberId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
    
    @Override
    public String toString() {
        return String.format("Member{id='%s', name='%s', type=%s, borrowed=%d}", 
            memberId, name, memberType, borrowedBookIsbns.size());
    }
}