package com.crud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crud.model.Books;
import com.crud.repository.BookRepository;

@Service
public class BookServices {
	
    @Autowired
    private BookRepository bookRepository;

    // Create or Update Book
    public Books createBook(Books book) {
        return bookRepository.save(book);
    }

    // Get All Books
    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get Book By ID
    public Books getBookById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public Books updateBook(Integer id, String title, String author, Integer price) {
        Books book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

        // Update only provided fields
        if (title != null && !title.isEmpty()) {
            book.setTitle(title);
        }
        if (author != null && !author.isEmpty()) {
            book.setAuthor(author);
        }
        if (price != null) {
            book.setPrice(price);
        }

        // Save the updated book and return it
        return bookRepository.save(book);  // This saves the updated book in the database
    }



    // Delete Book
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);  
    }

    //Search
    public List<Books> searchBooks(Map<String, String> searchBy) {
        // If no search criteria are provided, return all books
        if (searchBy.isEmpty()) {
            return bookRepository.findAll();
        }

        // Start with finding all books and apply filters based on the provided criteria
        List<Books> filteredBooks = bookRepository.findAll();

        // Apply filters for title, author, and price if present in the search criteria
        if (searchBy.containsKey("title")) {
            String title = searchBy.get("title");
            filteredBooks = filteredBooks.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (searchBy.containsKey("author")) {
            String author = searchBy.get("author");
            filteredBooks = filteredBooks.stream()
                    .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (searchBy.containsKey("price")) {
            try {
                Integer price = Integer.parseInt(searchBy.get("price"));
                filteredBooks = filteredBooks.stream()
                        .filter(book -> book.getPrice().equals(price))
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                // Handle invalid price format, if necessary
                filteredBooks = new ArrayList<>();
            }
        }

        return filteredBooks;
    }

	

}
