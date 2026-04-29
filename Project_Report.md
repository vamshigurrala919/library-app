# Project Report: Library Management System

## 1. Entity Relationship Design

The application manages two primary entities: **Author** and **Book**. The relationship between these entities is designed as a **One-to-Many** relationship, where one Author can have multiple Books, but each Book belongs to exactly one Author.

### Author Entity
* **Attributes**: `id` (Primary Key), `name`, `nationality`, `birthYear`, `bio`.
* **Relationship**: Uses `@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)` to map the collection of `Book` entities.

### Book Entity
* **Attributes**: `id` (Primary Key), `title`, `isbn` (Unique constraint), `publishedYear`, `genre`, `price`.
* **Relationship**: Uses `@ManyToOne(fetch = FetchType.LAZY)` and `@JoinColumn(name = "author_id", nullable = false)` to link each book to its corresponding Author.

This design ensures referential integrity, meaning a book cannot exist in the database without being associated with a valid author.

---

## 2. Implementation Details for Each Operation

The application uses a standard MVC architecture (Model-View-Controller) with Spring Boot, Spring Data JPA, and JSP for the view layer. 

### A. Populate Database
* **Code Implementation**: The application uses an H2 in-memory database (`jdbc:h2:mem:librarydb`) with Hibernate's `ddl-auto=create-drop` property configured in `application.properties`. 
* **Sample Data**: A `DataInitializer` component implements `CommandLineRunner` to populate the database with 10 sample Authors and 10 sample Books using the respective repository `saveAll()` methods upon application startup.
* *(Screenshot Placeholder: Insert a screenshot of your H2 Database console showing the populated tables)*

### B. Create Operation
* **Code Implementation**: 
  * **View**: `form.jsp` contains an HTML form bound to the Spring ModelAttribute.
  * **Controller**: The `@PostMapping("/save")` method in `BookController` handles the form submission. It uses the `@Valid` annotation to ensure field validation (e.g., ensuring ISBN is not blank).
  * **Exception Handling**: The `BookService` layer catches `DataIntegrityViolationException` if a user attempts to insert a Book with a duplicate ISBN, passing a user-friendly error message back to the JSP view without crashing the server.
* *(Screenshot Placeholder: Insert a screenshot of the "Add New Book" form and a screenshot showing the duplicate ISBN error message)*

### C. Read Operation
* **Code Implementation**: 
  * **Controller**: The `@GetMapping` method fetches data from the service layer and adds it to the model.
  * **Custom Query**: To avoid the N+1 select problem and efficiently load Book and Author data together, `BookRepository` includes a custom JPQL query that returns a custom DTO (`BookAuthorDTO`):
    ```java
    @Query("SELECT new com.library.entity.BookAuthorDTO(" +
           "b.id, b.title, b.isbn, b.publishedYear, b.genre, b.price, " +
           "a.id, a.name, a.nationality) " +
           "FROM Book b INNER JOIN b.author a " +
           "ORDER BY a.name, b.title")
    List<BookAuthorDTO> findAllBooksWithAuthorDetails();
    ```
  * **View**: `list.jsp` uses JSTL (`<c:forEach>`) to iterate over the DTOs and display the books and their respective authors in an HTML table.
* *(Screenshot Placeholder: Insert a screenshot of the "All Books" data table page)*

### D. Update Operation
* **Code Implementation**:
  * **Controller**: The `@GetMapping("/edit/{id}")` method retrieves the existing entity and populates the `form.jsp`. The `@PostMapping("/update/{id}")` handles saving the changes.
  * **Service**: The `BookService.updateBook()` method fetches the existing record by ID, verifies that updated fields (like ISBN) don't conflict with other existing records, applies the new data, and calls `bookRepository.save()`.
* *(Screenshot Placeholder: Insert a screenshot of the "Edit Book" form pre-filled with data, and the success page after saving)*

---

## 3. Challenges Faced and How I Overcame Them

1. **Environment Setup & Maven Compilation**:
   * **Challenge**: While attempting to run the Spring Boot application using the Maven wrapper (`.\mvnw spring-boot:run`), the build failed with the error: *"No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?"*
   * **Solution**: The system only had a Java Runtime Environment (JRE) configured, whereas Spring Boot requires a full Java Development Kit (JDK) to compile code. I overcame this by downloading the Microsoft OpenJDK 17 binary, extracting it to the local user directory, and updating the system's `JAVA_HOME` and `Path` environment variables via a PowerShell script to ensure the Maven compiler plugin had access to `javac`.

2. **PowerShell Brace Expansion Error**:
   * **Challenge**: When creating the package structure in the terminal using a command like `mkdir {entity,repository,service,controller}`, PowerShell treated the curly braces literally instead of expanding them as Bash does. This resulted in a broken package structure with a folder literally named `{entity,repository,service,controller}`.
   * **Solution**: I identified the misplaced directories using the `tree` command and removed the rogue folders using the `Remove-Item -LiteralPath` command, ensuring the proper individual directories were intact for Spring Boot component scanning.

3. **N+1 Query Problem in Read Operations**:
   * **Challenge**: Initially, loading all Books and their Authors caused an N+1 query issue, where Hibernate would issue one query for the books, and subsequent queries to fetch the author for each individual book.
   * **Solution**: I resolved this by writing a custom JPQL inner-join query in the `BookRepository` that maps the results directly into a `BookAuthorDTO`, retrieving all necessary data in a single, highly optimized SQL query.

---

## 4. Github URL of the Project

**Github Repository:** [Paste your Github URL here]
