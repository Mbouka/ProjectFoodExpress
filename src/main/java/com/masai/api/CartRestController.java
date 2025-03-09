package com.masai.api;

import javax.validation.Valid;

import com.masai.exception.CustomerNotFound;
import com.masai.model.Customer;
import com.masai.repository.CartRepository;
import com.masai.repository.ItemRepository;
import com.masai.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.masai.model.FoodCart;
import com.masai.model.Item;
import com.masai.service.CartService;

@RestController("/cart")
public class CartRestController {

	
	@Autowired
	private CartService cartService;
	@Autowired
	private CustomerService customerService;

	private Customer getCurrentCustomer() throws CustomerNotFound {
		return customerService.getAuthenticatedCustomer(); // Implémentez l'authentification
	}

	@PostMapping("/add")
	public ResponseEntity<FoodCart> addToCart(@RequestParam Integer itemId, @RequestParam int quantity) throws CustomerNotFound {
		FoodCart cart = cartService.addItemToCart(getCurrentCustomer(), itemId, quantity);
		return ResponseEntity.ok(cart);
	}


	@PutMapping("/update")
	public ResponseEntity<FoodCart> updateCart(@RequestParam Integer itemId, @RequestParam int quantity) throws CustomerNotFound {
		FoodCart cart = cartService.updateItemQuantity(getCurrentCustomer(), itemId, quantity);
		return ResponseEntity.ok(cart);
	}

	@DeleteMapping("/remove")
	public ResponseEntity<FoodCart> removeFromCart(@RequestParam Integer itemId) throws CustomerNotFound {
		FoodCart cart = cartService.removeItemFromCart(getCurrentCustomer(), itemId);
		return ResponseEntity.ok(cart);
	}


	@DeleteMapping("/clear")
	public ResponseEntity<Void> clearCart() throws CustomerNotFound {
		cartService.clearCart(getCurrentCustomer());
		return ResponseEntity.ok().build();
	}




}

