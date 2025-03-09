package com.masai.service;


import com.masai.exception.FoodCartException;
import com.masai.model.Customer;
import com.masai.model.FoodCart;
import com.masai.model.Item;

public interface CartService {

	public FoodCart addItemToCart(Customer customer, Integer itemId, int quantity);
	public FoodCart updateItemQuantity(Customer customer, Integer itemId, int quantity);

	public FoodCart removeItemFromCart(Customer customer, Integer itemId);

	public void clearCart(Customer customer);
	

	public FoodCart getCartByCustomer(Customer customer);

	}
