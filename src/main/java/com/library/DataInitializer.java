package com.library;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds the database with 10 authors and 10 books on startup.
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(AuthorRepository authorRepo, BookRepository bookRepo) {
        return args -> {
            if (authorRepo.count() > 0) {
                log.info("Database already seeded – skipping.");
                return;
            }

            log.info("Seeding database with sample data...");

            // ── 10 Authors ───────────────────────────────────────────────────────
            Author orwell   = authorRepo.save(new Author("George Orwell",    "British",   1903, "Eric Arthur Blair, known by his pen name George Orwell, was an English novelist and critic."));
            Author tolkien  = authorRepo.save(new Author("J.R.R. Tolkien",   "British",   1892, "John Ronald Reuel Tolkien was an English writer, poet, and professor."));
            Author rowling  = authorRepo.save(new Author("J.K. Rowling",     "British",   1965, "Joanne Rowling is a British author best known for the Harry Potter series."));
            Author dumas    = authorRepo.save(new Author("Alexandre Dumas",  "French",    1802, "Alexandre Dumas was a French novelist and playwright."));
            Author marquez  = authorRepo.save(new Author("Gabriel García Márquez", "Colombian", 1927, "A Colombian novelist, known for popularizing magical realism."));
            Author dostoevsky = authorRepo.save(new Author("Fyodor Dostoevsky", "Russian", 1821, "Russian novelist and philosopher, one of the greatest writers in world literature."));
            Author austen   = authorRepo.save(new Author("Jane Austen",      "British",   1775, "English novelist known for her six major novels."));
            Author dickens  = authorRepo.save(new Author("Charles Dickens",  "British",   1812, "English writer and social critic, widely regarded as the greatest Victorian era novelist."));
            Author twain    = authorRepo.save(new Author("Mark Twain",       "American",  1835, "Samuel Langhorne Clemens, known as Mark Twain, was an American writer."));
            Author hemingway= authorRepo.save(new Author("Ernest Hemingway", "American",  1899, "American novelist, short-story writer, and journalist."));

            // ── 10 Books ─────────────────────────────────────────────────────────
            bookRepo.save(new Book("Nineteen Eighty-Four",          "978-0-452-28423-4", 1949, "Dystopia",   12.99, orwell));
            bookRepo.save(new Book("Animal Farm",                   "978-0-451-52634-2", 1945, "Satire",      8.99, orwell));
            bookRepo.save(new Book("The Lord of the Rings",         "978-0-618-64015-7", 1954, "Fantasy",    24.99, tolkien));
            bookRepo.save(new Book("Harry Potter and the Philosopher's Stone", "978-0-7475-3269-9", 1997, "Fantasy", 10.99, rowling));
            bookRepo.save(new Book("The Count of Monte Cristo",     "978-0-14-044926-4", 1844, "Adventure",  14.99, dumas));
            bookRepo.save(new Book("One Hundred Years of Solitude", "978-0-06-088328-7", 1967, "Magical Realism", 13.99, marquez));
            bookRepo.save(new Book("Crime and Punishment",          "978-0-14-044913-4", 1866, "Literary Fiction", 11.99, dostoevsky));
            bookRepo.save(new Book("Pride and Prejudice",           "978-0-14-143951-8", 1813, "Romance",     9.99, austen));
            bookRepo.save(new Book("A Tale of Two Cities",          "978-0-14-143960-0", 1859, "Historical",  10.99, dickens));
            bookRepo.save(new Book("The Old Man and the Sea",       "978-0-684-80122-3", 1952, "Literary Fiction", 9.49, hemingway));

            log.info("✓ Seeded {} authors and {} books.", authorRepo.count(), bookRepo.count());
        };
    }
}
