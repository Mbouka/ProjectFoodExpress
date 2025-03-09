package com.masai.service;

import java.io.IOException;
import java.util.List;

import com.masai.exception.RestaurantException;
import com.masai.model.Address;
import com.masai.model.Item;
import com.masai.model.Restaurant;
import org.springframework.web.multipart.MultipartFile;

public interface RestaurantService {
	
	public Restaurant addRestaurant(Restaurant res) throws RestaurantException;
	public void saveRestaurantWithItemImage(Restaurant restaurant, MultipartFile imageFile)throws IOException;
	public Restaurant updateRestaurant(Integer restaurantId,Restaurant res) throws RestaurantException , IOException;
	
	//public Restaurant viewRestaurant(Integer resId) throws RestaurantException;
	
	//public List<Restaurant> viewNearByRestaurant(Address address) throws RestaurantException;
	
	//public List<Restaurant> viweRestaurantByItemName(String itemName) throws RestaurantException;

	public List<Restaurant> getAllRestaurants() throws RestaurantException;

	public List<Restaurant> getTop10Restaurants() throws RestaurantException ;

	public Restaurant getRestaurantById(Integer restaurantId) throws RestaurantException;

	public List<Restaurant> searchRestaurants(String searchTerm);

	public List<Item> searchItems(String searchTerm) ;

	public List<Restaurant> searchAll(String query);

	public List<Restaurant> searchByLocation(String location);

	}
