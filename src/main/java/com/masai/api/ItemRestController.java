package com.masai.api;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.masai.exception.ItemNotFoundException;
import com.masai.exception.RestaurantException;
import com.masai.model.Category;
import com.masai.model.Item;
import com.masai.model.Restaurant;
import com.masai.service.ItemService;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/item")
public class ItemRestController {

	
	@Autowired
	private ItemService itemService;
	
	
	
	
	@PostMapping("/add")
	public ResponseEntity<Item> addItemHandler(@Valid @RequestBody Item item, @RequestParam("image") MultipartFile imageFile)throws Exception{
		
		Item savedItem = itemService.addItem(item,imageFile);
		
		return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
		
	}
	
	
	
	@PutMapping("/update/{itemId}")
	public ResponseEntity<Item> updateItemHandler(@PathVariable Integer itemId,@RequestBody Item item)throws ItemNotFoundException, IOException {
		
		Item updatedItem = itemService.updateItem(itemId,item);
		
		return new ResponseEntity<>(updatedItem, HttpStatus.OK);
		
	}

	@PutMapping("/itemsUpdate/{itemId}")
	public ResponseEntity<Item> updateItemAndImageHandler(@PathVariable Integer itemId,
												  @RequestParam("item") String itemJson,
												  @RequestParam("image") MultipartFile imageFile) throws ItemNotFoundException, IOException {

		// Convert the item JSON string to an Item object (you may use a custom utility for this)
		ObjectMapper objectMapper = new ObjectMapper();
		Item item = objectMapper.readValue(itemJson, Item.class);

		// Call the service to update the item with the image file
		Item updatedItem = itemService.updateItemAndImage(itemId, item, imageFile);

		// Return the updated item in the response with status OK
		return new ResponseEntity<>(updatedItem, HttpStatus.OK);
	}
//update item images
	@PutMapping("/{itemId}/update-image")
	public ResponseEntity<Item> updateItemImage(@PathVariable Integer itemId,
												@RequestParam("image") MultipartFile imageFile) {
		try {
			Item updatedItem = itemService.updateImage(itemId, imageFile);
			return new ResponseEntity<>(updatedItem, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ItemNotFoundException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/view/{itemId}")
	public ResponseEntity<Item> viewItemHandler(@PathVariable Integer itemId){
		
		Item existedItem = itemService.viewItem(itemId);
		
		return new ResponseEntity<Item>(existedItem, HttpStatus.OK);
		
	}
	
	
	
	@DeleteMapping("/remove/{itemId}")
	public ResponseEntity<Item> removeItemHandler(@PathVariable Integer itemId){
		
		Item deletedItem = itemService.removeItem(itemId);
		
		return new ResponseEntity<Item>(deletedItem, HttpStatus.OK);
		
	}
	
	
	
	@PostMapping("/getByCategory")
	public ResponseEntity<List<Item>> viewAllItemsByCategoryHandler(@RequestBody Category category){
		
		List<Item> listItem = itemService.viewAllItemsByCategory(category);
		
		return new ResponseEntity<>(listItem, HttpStatus.ACCEPTED);
		
	}
	
	
	

	@PostMapping("/getByRestaurant")
	public ResponseEntity<List<Item>> viewAllItemsOfRestaurantHandler(@RequestBody Restaurant restaurant) throws ItemNotFoundException, RestaurantException{
		
		List<Item> listItem = itemService.viewAllItemsByRetaurant(restaurant);
		
		return new ResponseEntity<>(listItem, HttpStatus.ACCEPTED);
		
	}
	
	
	

	@PostMapping("/getByName/{name}")
	public ResponseEntity<List<Item>> viewAllItemsByNameHandler(@PathVariable("name") String itemName){
		
		List<Item> listItem = itemService.viewAllItemsByName(itemName);
		
		return new ResponseEntity<>(listItem, HttpStatus.ACCEPTED);
		
	}
	
	
	
	
}
