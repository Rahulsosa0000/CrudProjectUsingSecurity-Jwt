package com.crud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.model.Books;
import com.crud.repository.BookRepository;
import com.crud.service.BookServices;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@Autowired
    private BookRepository bookRepository;

//    @Autowired
//    private UsersRepo userRepository;
    
    @Autowired
    private BookServices bookServices;
    
//    @Autowired
//    private JWTService jwtUtil;
    

    
    @GetMapping("/getall")
    public List<Books> getAllBooks()
    {
        return bookServices.getAllBooks();
    }

    
 // Get a book by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {
        Books book = bookServices.getBookById(id);
        if (book == null) {
            return ResponseEntity.status(404).body("Book Id Not Found.");
        }
        return ResponseEntity.ok(book);
    }

    // Create or Update a book
    @PostMapping("/add")
    public ResponseEntity<String> saveOrUpdateBook(@RequestBody Books book) {
        bookServices.createBook(book);
        return ResponseEntity.ok("Book added or updated successfully!");
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable int id,
                                        @RequestBody Books bookRequest) {
        try {
            // Fetch the existing book
            Books book = bookRepository.findById(id).orElse(null);
            if (book == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with ID " + id + " not found.");
            }

            // Update book details
            book.setTitle(bookRequest.getTitle());
            book.setAuthor(bookRequest.getAuthor());
            book.setPrice(bookRequest.getPrice());

            // Save the updated book
            Books updatedBook = bookRepository.save(book);
            return ResponseEntity.ok(updatedBook);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update the book.");
        }
    }


    

    // Delete a book
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {
        bookServices.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully!");
    }

    // Search books by multiple criteria
    @GetMapping("/search")
    public ResponseEntity<List<Books>> searchBooks(@RequestParam Map<String, String> Search) {
        List<Books> books = bookServices.searchBooks(Search);
        return ResponseEntity.ok(books);
    }
    
    
    
    
    
    
    
    
    
    
    
    
//  // Get all books for any user (without filtering by username)
//  @GetMapping("/all")
//  public ResponseEntity<List<Books>> getAllBooks(@RequestHeader("Authorization") String token) {
//      // Extract the token prefix (e.g., "Bearer ")
//      String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
//
//      // You can extract the username from the token, but we're not using it to filter books
//      String username = jwtUtil.extractUserName(jwtToken);
//
//      // Fetch all books from the database (without filtering by user)
//      List<Books> books = bookRepository.findAll();
//
//      return ResponseEntity.ok(books);
//  }
    
    
    // Add a new book
//    @PostMapping("/add")
//    public ResponseEntity<String> addBook(@RequestBody Books book, @RequestHeader("Authorization") String token) {
//        // Extract the token prefix (e.g., "Bearer ")
//        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
//
//        // You can extract the username from the token, but we're not using it to filter books
//        String username = jwtUtil.extractUserName(jwtToken);
//
//        // Fetch user from the database (optional, for future use if needed)
//        Users user = userRepository.findByusername(username);
//        if (user == null) {
//            throw new RuntimeException("User not found");
//        }
//
//        // Just save the book, without assigning it to the user
//        bookRepository.save(book);
//
//        return ResponseEntity.ok("Book added successfully!");
//    }
}
