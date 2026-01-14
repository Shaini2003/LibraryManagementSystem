package library.observer;

/**
 * Phase 3 - Commit 1: Observer Pattern Interface
 * Allows objects to be notified of library events
 */
public interface LibraryObserver {
    void update(String event, String details);
}