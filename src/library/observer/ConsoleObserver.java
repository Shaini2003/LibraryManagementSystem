package library.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Phase 3 - Commit 1: Concrete Observer Implementation
 * Logs library events to console with timestamp
 */
public class ConsoleObserver implements LibraryObserver {
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void update(String event, String details) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println(String.format("[%s] EVENT: %s - %s", 
            timestamp, event, details));
    }
}