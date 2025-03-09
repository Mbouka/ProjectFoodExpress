package com.masai.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.masai.exception.ItemNotFoundException;
import com.masai.exception.RestaurantException;
import com.masai.model.Category;
import com.masai.model.Item;
import com.masai.model.Restaurant;
import org.springframework.web.multipart.MultipartFile;

public interface ItemService {

	public Item addItem(Item item, MultipartFile imageFile) throws IOException;
	
	public Item updateItem(Integer itemId, Item item) throws ItemNotFoundException, IOException;

	public Item updateItemAndImage(Integer itemId, Item item, MultipartFile imageFile) throws ItemNotFoundException, IOException;

	public Item updateImage(Integer itemId, MultipartFile imageFile) throws ItemNotFoundException, IOException;

	public Item viewItem (Integer itemId) throws ItemNotFoundException;
	
	public Item removeItem(Integer itemId) throws ItemNotFoundException;
	
	public List<Item> viewAllItemsByCategory(Category category) throws ItemNotFoundException;
	
	public List<Item> viewAllItemsByRetaurant(Restaurant restaurant) throws ItemNotFoundException,RestaurantException;
	
	public List<Item> viewAllItemsByName(String name) throws ItemNotFoundException;

	public List<Item> searchItems(String query) ;

	}
