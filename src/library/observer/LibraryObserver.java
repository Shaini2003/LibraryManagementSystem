package library.observer;

import library.annotations.DesignPattern;

/**
 * Observer Pattern Interface - Complete
 */
@DesignPattern(
    pattern = "Observer Pattern (Interface)",
    description = "Defines the observer contract for event notifications"
)
public interface LibraryObserver {
    void update(String event, String details);
}