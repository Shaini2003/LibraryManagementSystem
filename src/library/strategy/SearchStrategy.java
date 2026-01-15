package library.strategy;

import library.annotations.DesignPattern;
import library.model.Book;
import java.util.List;

/**
 * Strategy Pattern Interface - Complete
 */
@DesignPattern(
    pattern = "Strategy Pattern (Interface)",
    description = "Defines the contract for different search algorithms",
    benefits = {"Runtime algorithm selection", "Open/Closed principle", "Easy to extend"}
)
public interface SearchStrategy {
    List<Book> search(List<Book> books, String query);
}