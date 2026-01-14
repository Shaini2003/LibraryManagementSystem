# Bad Smells Identified in Phase 1

## Book.java
1. **Long Parameter List** - Constructor has 6 parameters
2. **Data Clumps** - Multiple primitive parameters grouped together
3. **Public Fields** - No encapsulation (isbn, title, author, etc. are public)
4. **Magic Strings** - status uses strings ("AVAILABLE", "BORROWED")
5. **Mutable Object** - setStatus() allows modification
6. **Poor Naming** - Single letter variables (i, t, a, c, s)
7. **Long Method** - getInfo() does too much

## Member.java
1. **Long Parameter List** - Constructor has 5 parameters
2. **Public Mutable Fields** - All fields are public
3. **Magic Numbers** - Hard-coded 3, 5, 1 for borrowing limits
4. **Type Checking** - Using if/else for member types instead of polymorphism
5. **Exposed Mutable Collection** - borrowedBookIsbns can be modified externally
6. **Magic Strings** - memberType uses strings

## LibraryService.java
1. **God Class** - Does too many things (book management, member management, transactions, search)
2. **Long Methods** - borrowBook() and returnBook() are too long
3. **Duplicate Code** - borrowBook() and returnBook() have similar structure
4. **Duplicate Code** - searchBooksByTitle() and searchBooksByAuthor() are almost identical
5. **Poor Singleton** - Public static instance instead of proper singleton pattern
6. **Public Mutable Collections** - books, members, transactionLog are public
7. **No Design Patterns** - No strategy for search, no observer for events
8. **Tight Coupling** - Direct dependency on concrete classes
9. **Poor Error Handling** - Just prints to console
10. **Duplicate Logic** - Borrowing limit logic repeated from Member class

## Total Bad Smells Identified: 23+

All these will be addressed in subsequent phases through refactoring and design patterns.