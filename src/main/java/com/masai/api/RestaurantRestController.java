package com.masai.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masai.model.Item;
import com.masai.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.masai.exception.RestaurantException;
import com.masai.model.Restaurant;
import com.masai.service.RestaurantService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RestaurantRestController {
	
	@Autowired
	private RestaurantService resSer;
	@Autowired
	private ItemService itemService;
	
	@PostMapping("/restaurants")
	public ResponseEntity<Restaurant> addRestaurantHandler(@RequestParam("restaurantData") String restaurantData, @RequestParam("image") MultipartFile imageFile) throws RestaurantException{
		
		//Restaurant rest  = resSer.addRestaurant(res);
		//return new ResponseEntity<Restaurant>(rest,HttpStatus.OK);
		try {
			// Convert the JSON string to Restaurant object
			Restaurant restaurant = new ObjectMapper().readValue(restaurantData, Restaurant.class);

			// Save the restaurant along with the item image
			resSer.saveRestaurantWithItemImage(restaurant, imageFile);

			return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping("/restaurantsUpdate/{restaurantId}")
	public ResponseEntity<Restaurant> updateRestaurantHandler(@PathVariable Integer restaurantId,@RequestBody Restaurant res) throws RestaurantException, IOException {
		Restaurant rest = resSer.updateRestaurant(restaurantId,res);
		return new ResponseEntity<>(rest,HttpStatus.OK);

	}
	/*@PutMapping("/updateMultiple")
	public ResponseEntity<List<Restaurant>> updateMultipleRestaurantsHandler(@RequestBody List<Restaurant> restaurants) throws RestaurantException {
		List<Restaurant> updatedRestaurants = new ArrayList<>();

		for (Restaurant restaurant : restaurants) {
			Restaurant updated = resSer.updateRestaurant(restaurant);
			updatedRestaurants.add(updated);
		}

		return new ResponseEntity<>(updatedRestaurants, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Restaurant> viewRestaurantHandler(@PathVariable("id") Integer id) throws RestaurantException{
		
		Restaurant rest =  resSer.viewRestaurant(id);
		
		return new ResponseEntity<Restaurant>(rest,HttpStatus.OK);
		
	}*/

	@GetMapping("/all")
	public ResponseEntity<List<Restaurant>> getAllRestaurantsHandler() {
		try {
			List<Restaurant> restaurants = resSer.getAllRestaurants();
			return new ResponseEntity<>(restaurants, HttpStatus.OK);
		} catch (RestaurantException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/searchRes")
	public ResponseEntity<List<Restaurant>> searchRestaurants(@RequestParam String query) {
		List<Restaurant> restaurants = resSer.searchAll(query);
		//List<Item> items = itemService.searchItems(query);

		//Map<String, Object> response = new HashMap<>();
		//response.put("restaurants", restaurants);
		//response.put("items", items);
		if (restaurants.isEmpty()) {
			return ResponseEntity.noContent().build();  // Si aucune donnée n'est trouvée, retourne 204 No Content
		}
		return ResponseEntity.ok(restaurants);
	}

	@GetMapping("/items")
	public ResponseEntity<List<Item>> searchItems(@RequestParam String query) {
		List<Item> items = resSer.searchItems(query);
		return ResponseEntity.ok(items);
	}

	@GetMapping("/searchLocation")
	public ResponseEntity<List<Restaurant>> searchRestaurantsByLocation(@RequestParam String location) {
		List<Restaurant> restaurants = resSer.searchByLocation(location);
		return ResponseEntity.ok(restaurants);
	}

//	@GetMapping("/restaurant")
//	public ResponseEntity<List<Restaurant>> viewNearByRestaurantHandler(@PathVariable Address address) throws RestaurantException{
//		
//		List<Restaurant>  rest =resSer.viewNearByRestaurant(address);
//		
//		return new ResponseEntity<>(rest,HttpStatus.OK);
//		
//	}
//	
//	@GetMapping("/restaurant/{itemName}")
//	public ResponseEntity<List<Restaurant>> viweRestaurantByItemNameHandler(@PathVariable String itemName) throws RestaurantException{
//		
//		List<Restaurant> rest = resSer.viweRestaurantByItemName(itemName);
//		
//		return new ResponseEntity<>(rest,HttpStatus.OK);
//	}

	
	
}
