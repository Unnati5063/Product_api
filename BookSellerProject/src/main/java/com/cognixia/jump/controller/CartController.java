package com.cognixia.jump.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.Cart;
import com.cognixia.jump.repository.CartRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "cart", description = "user cart")
public class CartController {
	
	@Autowired
	CartRepository cartRepo;
	
	@Operation(summary = "ADMIN get pending orders.", 
			   description = "Get entries from the cart."
			)
	// ADMIN ONLY
	@GetMapping("/cart")
	public List<Cart> getAllCarts() {
		return cartRepo.findAll();
	}
}