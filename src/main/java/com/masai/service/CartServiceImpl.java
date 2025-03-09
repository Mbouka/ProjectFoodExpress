package com.masai.service;

import com.masai.model.Customer;
import com.masai.model.FoodCartItem;
import com.masai.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.FoodCartException;
import com.masai.model.FoodCart;
import com.masai.model.Item;
import com.masai.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {
	
	
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ItemRepository itemRepository;



	@Override
	public FoodCart addItemToCart(Customer customer, Integer itemId, int quantity) {
		FoodCart cart = getCartByCustomer(customer);
		Item item = itemRepository.findById(itemId)
				.orElseThrow(() -> new RuntimeException("Article non trouvé"));

		// Vérifier si l'article est déjà dans le panier
		FoodCartItem existingItem = cart.getItems().stream()
				.filter(cartItem -> cartItem.getItem().getItemId().equals(itemId))
				.findFirst()
				.orElse(null);

		if (existingItem != null) {
			existingItem.setQuantity(existingItem.getQuantity() + quantity);
		} else {
			FoodCartItem cartItem = new FoodCartItem();
			cartItem.setItem(item);
			cartItem.setQuantity(quantity);
			cart.addItem(cartItem);
		}

		return cartRepository.save(cart);
	}

	@Override
	public FoodCart updateItemQuantity(Customer customer, Integer itemId, int quantity) {
		FoodCart cart = getCartByCustomer(customer);

		FoodCartItem cartItem = cart.getItems().stream()
				.filter(item -> item.getItem().getItemId().equals(itemId))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Article non trouvé dans le panier"));

		cartItem.setQuantity(quantity);
		return cartRepository.save(cart);
	}

	@Override
	public FoodCart removeItemFromCart(Customer customer, Integer itemId) {
		FoodCart cart = getCartByCustomer(customer);
		cart.getItems().removeIf(item -> item.getItem().getItemId().equals(itemId));
		return cartRepository.save(cart);
	}

	@Override
	public void clearCart(Customer customer) {
		FoodCart cart = getCartByCustomer(customer);
		cart.clearCart();
		cartRepository.save(cart);
	}

	@Override
	public FoodCart getCartByCustomer(Customer customer) {
		return cartRepository.findByCustomer(customer)
				.orElseGet(() -> {
					FoodCart newCart = new FoodCart();
					newCart.setCustomer(customer);
					return cartRepository.save(newCart);
				});
	}
	


}
