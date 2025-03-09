package com.masai.repository;


import java.util.List;

import com.masai.model.Category;
import com.masai.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.masai.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	public List<Item> findByItemName(String itemName);

	 List<Item> findByItemNameContainingIgnoreCase(String itemName);

	List<Item> findByCategoryNameContainingIgnoreCase(String categoryName);


}
