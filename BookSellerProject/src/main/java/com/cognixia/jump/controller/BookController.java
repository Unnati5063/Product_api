package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.Book;
import com.cognixia.jump.repository.BookRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "book", description = "manage books")
public class BookController {
	
	@Autowired
	BookRepository repo;
	
	@Operation(summary = "Get all books table.", 
			   description = "Gets all books from the table in the database with details."
			)
	@GetMapping("/books")
	public List<Book> getBooks() {
		return repo.findAll();
	}
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Book found", 
						 content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookController.class) ) ),
			@ApiResponse(responseCode = "201", description = "Book created",
						 content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookController.class))),
			@ApiResponse(responseCode = "404", description = "Book not found", 
			 			 content = @Content)
		}
	)
	
	@Operation(summary = "Get a single book from table.", 
	   description = "Get book by its ID from table."
	)
	@GetMapping("/books/{id}")
	public ResponseEntity<?> getBookById(@PathVariable int id) {
		Optional<Book> found = repo.findById(id);
		
		if(found.isEmpty()) {
			return ResponseEntity.status(404).body("Book id " + id + " not found");
		}
		else {
			return ResponseEntity.status(200).body(found.get());
		}
	}
	
	@Operation(summary = "ADMIN ONLY - Add a book.", 
			   description = "Insert a book with all details."
			)
	// ADMIN ONLY
	@PostMapping("/books/add")
	public ResponseEntity<?> createBook(@Valid @RequestBody BookController book) {
		
		book.setId(null);
		
		BookController created = repo.saveAll(book);
		
		return ResponseEntity.status(201).body(created);
	}
	
	private void setId(Object object) {
	}

	public Object getId() {
		return null;
	}
	
}