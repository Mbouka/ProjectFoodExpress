package com.masai.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.ItemNotFoundException;
import com.masai.exception.RestaurantException;
import com.masai.model.Category;
import com.masai.model.Item;
import com.masai.model.Restaurant;
import com.masai.repository.ItemRepository;
import com.masai.repository.RestaurantDao;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ItemServiceImpl implements ItemService {

	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private RestaurantDao ResDao;
	
	
	
	@Override
	public Item addItem(Item item, MultipartFile imageFile) throws IOException {
		
		if(item==null) throw new ItemNotFoundException("Please add valid Item Details...");
		// Définir le chemin de sauvegarde de l'image
		String imageName = imageFile.getOriginalFilename();
		String imagePath = "src/main/resources/static/images/" + imageName;

		// Sauvegarder l'image sur le serveur
		Path path = Paths.get(imagePath);
		Files.write(path, imageFile.getBytes());

		// Assigner l'URL de l'image à l'item
		item.setImageUrl("/images/" + imageName);
		Item savedItem = itemRepository.save(item);
		
		return savedItem;
	}
	
	

	@Override
	public Item updateItem(Integer itemId, Item item) throws ItemNotFoundException, IOException {
		
		/*Optional<Item> optional = itemRepository.findById(item.getItemId());

		if(optional.isPresent()) {
			
//			Item existedItem = optional.get();
			
			Item updatedItem = itemRepository.save(item);
			
			return updatedItem;
			
		}
		else {
			throw new ItemNotFoundException("There is no such item to be updated........Please add it first");
		}*/
		// First, check if the item exists by its ID
		Optional<Item> optional = itemRepository.findById(itemId);

		if (optional.isPresent()) {
			// Retrieve the existing item
			Item existingItem = optional.get();

			// Update the item's fields with the new data
			existingItem.setItemName(item.getItemName());
			existingItem.setCategory(item.getCategory());
			existingItem.setQuantity(item.getQuantity());
			existingItem.setCost(item.getCost());

			// Check if the image URL is provided and update accordingly
			if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
				existingItem.setImageUrl(item.getImageUrl());
			}

			// Save the updated item
			return itemRepository.save(existingItem);
		} else {
			throw new ItemNotFoundException("There is no such item to be updated. Please add it first.");
		}
	}

	@Override
	public Item updateItemAndImage(Integer itemId, Item item, MultipartFile imageFile) throws ItemNotFoundException, IOException {

		// Check if the item exists by its ID
		Optional<Item> optional = itemRepository.findById(itemId);

		if (optional.isPresent()) {
			// Retrieve the existing item
			Item existingItem = optional.get();

			// Update the item's fields with the new data
			existingItem.setItemName(item.getItemName());
			existingItem.setCategory(item.getCategory());
			existingItem.setQuantity(item.getQuantity());
			existingItem.setCost(item.getCost());

			// Check if the image file is present and update it
			if (imageFile != null && !imageFile.isEmpty()) {
				// Save the image to the server
				String imageName = imageFile.getOriginalFilename();
				String imagePath = "src/main/resources/static/images/" + imageName;

				// Create the path and save the file to disk
				Path path = Paths.get(imagePath);
				Files.write(path, imageFile.getBytes());

				// Set the image URL in the item
				existingItem.setImageUrl("/images/" + imageName);
			}

			// Save the updated item
			return itemRepository.save(existingItem);
		} else {
			throw new ItemNotFoundException("There is no such item to be updated. Please add it first.");
		}
	}

	@Override
	public Item updateImage(Integer itemId, MultipartFile imageFile) throws ItemNotFoundException, IOException {

		// Check if the item exists by its ID
		Optional<Item> optionalItem = itemRepository.findById(itemId);

		if (optionalItem.isEmpty()) {
			throw new ItemNotFoundException("Item with ID " + itemId + " not found.");
		}

		Item item = optionalItem.get();

		// Save image file to local folder (static/images/)
		String imageName = imageFile.getOriginalFilename();
		String imagePath = "src/main/resources/static/images/" + imageName;

		Path path = Paths.get(imagePath);
		Files.write(path, imageFile.getBytes());

		// Update item's imageUrl field
		item.setImageUrl("/images/" + imageName);

		// Save updated item
		return itemRepository.save(item);
	}

	@Override
	public Item viewItem(Integer itemId) throws ItemNotFoundException {

		Optional<Item> optional = itemRepository.findById(itemId);
		
		if(optional.isPresent()) {
			
			Item savedItem = optional.get();
			
			return savedItem;
			
		}
		else {
			throw new ItemNotFoundException("No such Item in List....");
		}
		
	}

	
	
	@Override
	public Item removeItem(Integer itemId) throws ItemNotFoundException {
		
		Optional<Item> optional = itemRepository.findById(itemId);
		
		if(optional.isPresent()) {
			
			Item existedItem = optional.get();
			
			itemRepository.delete(existedItem);
			
			return existedItem;
		}
		else {
			throw new ItemNotFoundException("No Such Item to be Removed...... Please add it First");
		}
	}
	
	

	@Override
	public List<Item> viewAllItemsByCategory(Category category) throws ItemNotFoundException {
		
		List<Item> listByCategory = new ArrayList<>();
		
		List<Item> itemList = itemRepository.findAll();

		for(Item item : itemList) {
			
			if(item.getCategory().equals(category)){
				listByCategory.add(item);
			}
			
		}
		
		if(listByCategory.isEmpty()) throw new ItemNotFoundException("No such Item with this category name = "+category.getCategoryName()+" exist");

		return listByCategory;
	}

	
	
	@Override
	public List<Item> viewAllItemsByRetaurant(Restaurant restaurant) throws ItemNotFoundException,RestaurantException{
		
		
		Optional<Restaurant> rest =ResDao.findById(restaurant.getRestaurantId());
		
		if(rest.isPresent()) {
			
			List<Item> items = rest.get().getItems();
			
			if(items.size()==0) {
				
				throw new ItemNotFoundException("Item List empty......");
				
			}
		
			return items;
			
		}
		else {
		
			throw new RestaurantException("Restaurant not found.....");
		}
		
	}


	
	@Override
	public List<Item> viewAllItemsByName(String name) throws ItemNotFoundException {
		
		List<Item> itemList = itemRepository.findByItemName(name);

		if(itemList.isEmpty()) throw new ItemNotFoundException("Item with this name not exist.....");
		
		return itemList;
	
	}
	@Override
	public List<Item> searchItems(String query) {
		return itemRepository.findByItemNameContainingIgnoreCase(query);  // Exemple d'une méthode de recherche
	}

}
