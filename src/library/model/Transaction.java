package library.model;

import library.annotations.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transaction class - Complete with annotations
 */
@DesignPattern(
    pattern = "Builder Pattern",
    description = "Uses Builder pattern for object construction"
)
@Immutable(threadSafe = true)
@Author(
    name = "Library Team",
    date = "2025-01-15",
    version = "2.0"
)
public class Transaction {
    
    @Validatable(required = true, message = "Transaction ID is required")
    private final String transactionId;
    
    @Validatable(required = true, message = "Member ID is required")
    private final String memberId;
    
    @Validatable(required = true, message = "Book ISBN is required")
    private final String bookIsbn;
    
    private final TransactionType type;
    private final LocalDateTime transactionDate;
    private final LocalDateTime dueDate;
    
    public enum TransactionType {
        BORROW, RETURN, RENEW, RESERVE
    }
    
    private Transaction(Builder builder) {
        this.transactionId = builder.transactionId;
        this.memberId = builder.memberId;
        this.bookIsbn = builder.bookIsbn;
        this.type = builder.type;
        this.transactionDate = builder.transactionDate;
        this.dueDate = builder.dueDate;
    }
    
    public String getTransactionId() { return transactionId; }
    public String getMemberId() { return memberId; }
    public String getBookIsbn() { return bookIsbn; }
    public TransactionType getType() { return type; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    
    @PerformanceMonitor(operationName = "Check Overdue Status")
    public boolean isOverdue() {
        return dueDate != null && LocalDateTime.now().isAfter(dueDate);
    }
    
    @DesignPattern(pattern = "Builder Pattern (Inner Class)")
    public static class Builder {
        private String transactionId;
        private String memberId;
        private String bookIsbn;
        private TransactionType type;
        private LocalDateTime transactionDate = LocalDateTime.now();
        private LocalDateTime dueDate;
        
        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }
        
        public Builder memberId(String memberId) {
            this.memberId = memberId;
            return this;
        }
        
        public Builder bookIsbn(String bookIsbn) {
            this.bookIsbn = bookIsbn;
            return this;
        }
        
        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }
        
        public Builder transactionDate(LocalDateTime transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }
        
        public Builder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }
        
        public Transaction build() {
            Objects.requireNonNull(transactionId, "Transaction ID cannot be null");
            Objects.requireNonNull(memberId, "Member ID cannot be null");
            Objects.requireNonNull(bookIsbn, "Book ISBN cannot be null");
            Objects.requireNonNull(type, "Transaction type cannot be null");
            return new Transaction(this);
        }
    }
    
    @Override
    public String toString() {
        return String.format("Transaction{id='%s', member='%s', book='%s', type=%s, date=%s}", 
            transactionId, memberId, bookIsbn, type, transactionDate);
    }
}