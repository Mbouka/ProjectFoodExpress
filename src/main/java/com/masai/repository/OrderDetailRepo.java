package com.masai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.masai.model.FoodCart;
import com.masai.model.OrderDetails;


@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetails, Integer>{
	
	public List<OrderDetails >findByCart(Optional<FoodCart> cart);

}
