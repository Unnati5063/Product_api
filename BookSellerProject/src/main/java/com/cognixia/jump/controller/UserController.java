package com.cognixia.jump.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.AuthenticationRequest;
import com.cognixia.jump.model.AuthenticationResponse;
import com.cognixia.jump.model.Book;
import com.cognixia.jump.model.Cart;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "user", description = " for user")
public class UserController {

	@Autowired
	UserRepository repo;

	@Autowired
	UserService service;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	com.cognixia.jump.utils.JwtUtil jwtUtil;

	@Autowired
	PasswordEncoder encoder;

	@Operation(summary = "ADMIN from user table.", 
			   description = "Get users from table."
			)
	// ADMIN ONLY
	@GetMapping("/user")
	public List<User> getUsers() {
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

	@Operation(summary = "Get token for user.", 
	   description = "Get token for user to use api method."
	)
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password");
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

		final String jwt = jwtUtil.generateTokens(userDetails);

		return ResponseEntity.status(201).body(new AuthenticationResponse(jwt));
	}

	@Operation(summary = "Create user.", 
			   description = "Create user in database."
			)
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {

		user.setId(null);
		user.setRole(User.Role.ROLE_USER);

		user.setPassword(encoder.encode(user.getPassword()));

		User created = repo.save(user);

		return ResponseEntity.status(201).body(created);

	}
	
	@Operation(summary = "Updates user password", 
			   description = "user can update their password."
			)
	@PatchMapping("/change/password")
	public ResponseEntity<?> updatePassword(@PathParam(value = "password") String password) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();

		Optional<User> found = repo.findByUsername(username);
		
		if (!found.isEmpty()) {
			int id = found.get().getId();
			
			
			String hashed = encoder.encode(password);
			
			found.get().setPassword(hashed);
			//repo.save(found.get());
			
			int result = repo.updateUserPassword(id, hashed);
			
			if (result > 0) {
				return ResponseEntity.status(200).body("password updated  " + password + "  " + hashed);
			}
			else {
				return ResponseEntity.status(404).body("password not updated");
			}

			
		} else {
			return ResponseEntity.status(404).body("User not found");
		}
	}

	@Operation(summary = "ADMIN get single user from table.", 
			   description = "single user from table by their username."
			)
	// ADMIN ONLY
	@GetMapping("/user/{name}")
	public ResponseEntity<?> getUserByUsername(@PathVariable String name) {
		Optional<User> found = repo.findByUsername(name);

		if (found.isEmpty()) {
			return ResponseEntity.status(404).body("User not found");
		} else {
			return ResponseEntity.status(200).body(found.get());
		}
	}
	
	@Operation(summary = "specific user cart.", 
			   description = "specific user cart from table."
			)
	@GetMapping("/user/cart")
	public ResponseEntity<?> getUserCart() {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();

		Optional<User> found = repo.findByUsername(username);

		if (!found.isEmpty()) {
			HashMap<Integer, Book> userCart = service.getCart(found.get().getId());

			if (userCart.isEmpty()) {
				return ResponseEntity.status(200).body("Cart is Empty");
			} else {
				return ResponseEntity.status(200).body(userCart);
			}
		} else {
			return ResponseEntity.status(404).body("User not found");
		}
	}

	@Operation(summary = "Add book to specific user.", 
			   description = "user can add bood by specific id to their cart."
			)
	@PostMapping("/user/addToCart/{id}")
	public ResponseEntity<?> addBookToCartById(@PathVariable int id) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();

		Optional<User> found = repo.findByUsername(username);

		if (!found.isEmpty()) {
			Cart cart = service.addToCart(found.get(), id);

			if (cart.getId() == null) {
				return ResponseEntity.status(404).body("Book couldn't be added");
			} else {
				return ResponseEntity.status(200).body(cart);
			}
		} else {
			return ResponseEntity.status(404).body("User not found");
		}

	}
	
	@Operation(summary = "Remove book from user cart", 
			   description = "user can remove a book from their cart"
			)
	@DeleteMapping("/user/cart")
	public ResponseEntity<?> deleteCartItemByBook(@RequestBody BookController book) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();

		Optional<User> found = repo.findByUsername(username);

		if (!found.isEmpty()) {
			HashMap<Integer, Book> userCart = service.getCart(found.get().getId());

			if (userCart.isEmpty()) {
				return ResponseEntity.status(404).body("Cart is Empty");
			} else {
				int removed = -1;

				for (Entry<Integer, Book> e : userCart.entrySet()) {
					if ((book).getId().equals(((Book) e.getValue()).getId())) {
						removed = service.editCart(e.getKey());
						break;
					}
				}

				if (removed > 0) {
					return ResponseEntity.status(200).body("Book removed from cart");
				} else {
					return ResponseEntity.status(404).body("Book not found");
				}
			}
		} else {
			return ResponseEntity.status(404).body("User not found");
		}

	}

	@Operation(summary = "Remove book from user cart", 
			   description = "remove book form user cart by specific id"
			)
	@DeleteMapping("/user/cart/{id}")
	public ResponseEntity<?> deleteCartItemById(@PathVariable int id) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();

		Optional<User> found = repo.findByUsername(username);

		if (!found.isEmpty()) {
			HashMap<Integer, Book> userCart = service.getCart(found.get().getId());

			if (userCart.isEmpty()) {
				return ResponseEntity.status(404).body("Cart is Empty");
			} else {
				int removed = -1;

				for (Entry<Integer, Book> e : userCart.entrySet()) {
					if (e.getKey().equals(id)) {
						removed = service.editCart(e.getKey());
						break;
					}
				}

				if (removed > 0) {
					return ResponseEntity.status(200).body("Book removed from cart");
				} else {
					return ResponseEntity.status(404).body("Book not found");
				}
			}
		} else {
			return ResponseEntity.status(404).body("User not found");
		}
	}

}