package library.observer;

import library.annotations.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Console Observer - Complete with annotations
 */
@DesignPattern(
    pattern = "Observer Pattern (Concrete Observer)",
    description = "Concrete implementation of observer for console logging"
)
@Author(
    name = "Library Team",
    date = "2025-01-15",
    version = "1.0"
)
public class ConsoleObserver implements LibraryObserver {
    
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    @PerformanceMonitor(
        operationName = "Log Event to Console",
        logExecution = false
    )
    public void update(String event, String details) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println(String.format("[%s] EVENT: %s - %s", 
            timestamp, event, details));
    }
}