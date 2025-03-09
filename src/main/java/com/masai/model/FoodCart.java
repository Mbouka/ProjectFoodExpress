package com.masai.model;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Service

@Table(name = "food_cart")
public class FoodCart {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer cartId;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Customer customer;
	
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<FoodCartItem> items = new ArrayList<>();

	public void addItem(FoodCartItem item) {
		this.items.add(item);
	}

	public void removeItem(FoodCartItem item) {
		this.items.remove(item);
	}

	public void clearCart() {
		this.items.clear();
	}
}
