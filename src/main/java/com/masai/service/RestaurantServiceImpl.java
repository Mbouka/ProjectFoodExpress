package com.masai.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.masai.model.Item;
import com.masai.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.RestaurantException;
import com.masai.model.Address;
import com.masai.model.Restaurant;
import com.masai.repository.RestaurantDao;
import org.springframework.web.multipart.MultipartFile;


@Service
public class RestaurantServiceImpl implements RestaurantService{

	@Autowired
	private RestaurantDao resDao;

	@Autowired
	ItemRepository itemRepository;
	
	@Override
	public Restaurant addRestaurant(Restaurant res) throws RestaurantException {
		// TODO Auto-generated method stub
		
		Restaurant rest = resDao.save(res);
		
		
		if(rest!=null) {
			return rest;
		}
		else
		throw new RestaurantException("Something went wrong......");
	}

	@Override
	public void saveRestaurantWithItemImage(Restaurant restaurant, MultipartFile imageFile) throws IOException {
		// Save restaurant data
		Restaurant savedRestaurant = resDao.save(restaurant);

		// Save the item and image URL
		for (Item item : restaurant.getItems()) {
			String imageName = imageFile.getOriginalFilename();
			String imagePath = "src/main/resources/static/images/" + imageName;

			// Save the image to the server
			Path path = Paths.get(imagePath);
			Files.write(path, imageFile.getBytes());

			// Update the item with the image URL
			item.setImageUrl("/images/" + imageName);
			item.getResturants().add(savedRestaurant);

			// Save the item
			itemRepository.save(item);
		}
	}
	@Override
	public Restaurant updateRestaurant(Integer restaurantId,Restaurant res) throws RestaurantException , IOException{
		// TODO Auto-generated method stub
		
		/*Optional<Restaurant> opt = resDao.findById(res.getRestaurantId());
		
		if(opt.isPresent()) {
			return resDao.save(res);
		}
		else
			throw new RestaurantException("Restaurant not found.....");*/
		// Retrieve the restaurant by its ID
		Optional<Restaurant> opt = resDao.findById(restaurantId);

		if(opt.isPresent()) {
			// Restaurant exists, so update its details
			Restaurant existingRestaurant = opt.get();

			// Update restaurant fields
			existingRestaurant.setRestaurantName(res.getRestaurantName());
			existingRestaurant.setManager(res.getManager());
			existingRestaurant.setMobileNumber(res.getMobileNumber());
			existingRestaurant.setPassword(res.getPassword());
			existingRestaurant.setAddress(res.getAddress()); // Assuming Address is also updated

			// Now handle the items, including images
			for (Item item : res.getItems()) {
				// Check if the item exists in the restaurant
				Optional<Item> existingItemOpt = itemRepository.findById(item.getItemId());

				if (existingItemOpt.isPresent()) {
					Item existingItem = existingItemOpt.get();

					// Update item fields (itemName, category, etc.)
					existingItem.setItemName(item.getItemName());
					existingItem.setCategory(item.getCategory());
					existingItem.setQuantity(item.getQuantity());
					existingItem.setCost(item.getCost());

					// Handle image update
					if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
						existingItem.setImageUrl(item.getImageUrl());
					}

					// Save the updated item
					itemRepository.save(existingItem);
				}
			}

			// Save the updated restaurant
			return resDao.save(existingRestaurant);
		} else {
			throw new RestaurantException("Restaurant not found.....");
		}
		
	}

	/*@Override
	public Restaurant viewRestaurant(Integer resId) throws RestaurantException {
		// TODO Auto-generated method stub
		Optional<Restaurant> opt = resDao.findById(resId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		else
			throw new RestaurantException("Restaurant not found.....");
		
		
	}

	@Override
	public List<Restaurant> viewNearByRestaurant(Address address) throws RestaurantException {
		// TODO Auto-generated method stub
		
		List<Restaurant> rests = resDao.findByAddress(address);
		
		
		if(rests.size()>0)
		return rests;
		else
			throw new RestaurantException("Restaurant not found with this address :"+address);
	}

	@Override
	public List<Restaurant> viweRestaurantByItemName(String itemName) throws RestaurantException {
		return null;
	}*/

	@Override
	public List<Restaurant> getAllRestaurants() throws RestaurantException {
		List<Restaurant> restaurants = resDao.findAll();

		if (restaurants.isEmpty()) {
			throw new RestaurantException("Aucun restaurant trouvé.");
		}

		return restaurants;
	}
    @Override
	public List<Restaurant> getTop10Restaurants() throws RestaurantException {
		List<Restaurant> allRestaurants = resDao.findAll();

		if (allRestaurants.isEmpty()) {
			throw new RestaurantException("Aucun restaurant trouvé.");
		}

		// Ne garder que les 10 premiers
		return allRestaurants.stream().limit(10).toList();
	}
    @Override
	public Restaurant getRestaurantById(Integer restaurantId) throws RestaurantException {
		return resDao.findById(restaurantId)
				.orElseThrow(() -> new RestaurantException("Restaurant not found with id: " + restaurantId));
	}

	@Override
	public List<Restaurant> searchRestaurants(String searchTerm) {
		// Rechercher par nom de restaurant ou adresse
		List<Restaurant> restaurants = resDao.findByRestaurantNameContainingIgnoreCase(searchTerm);

		return restaurants;
	}

	@Override
	public List<Item> searchItems(String searchTerm) {
		// Rechercher par nom de produit (itemName)
		List<Item> items = itemRepository.findByItemNameContainingIgnoreCase(searchTerm);

		return items;
	}

	//recherche selection multiple
	@Override
	public List<Restaurant> searchAll(String query) {

		Set<Restaurant> result = new HashSet<>();
		// Recherche par nom de restaurant
		result.addAll(resDao.findByRestaurantNameContainingIgnoreCase(query));

		// Recherche par nom de produit (itemName)
		List<Item> itemsByName = itemRepository.findByItemNameContainingIgnoreCase(query);

		// Recherche par catégorie de produit (category)
		List<Item> itemsByCategory = itemRepository.findByCategoryNameContainingIgnoreCase(query);

		// Récupérer tous les restaurants associés aux items trouvés par nom
		result.addAll(
				itemsByName.stream()
						.flatMap(item -> item.getResturants().stream())
						.collect(Collectors.toSet())
		);

		// Récupérer tous les restaurants associés aux items trouvés par catégorie
		result.addAll(
				itemsByCategory.stream()
						.flatMap(item -> item.getResturants().stream())
						.collect(Collectors.toSet())
		);
		return new ArrayList<>(result);

	}

	public List<Restaurant> searchByLocation(String location) {
		return resDao.findByAddressCityContainingIgnoreCaseOrAddressStateContainingIgnoreCaseOrAddressCountryContainingIgnoreCaseOrAddressPinCodeContainingIgnoreCase(
				location, location, location, location);
	}


	}
