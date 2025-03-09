package com.masai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "food_cart_items")
public class FoodCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "food_cart_id")  // Clé étrangère vers FoodCart
    private FoodCart foodCart;

    @ManyToOne
    @JoinColumn(name = "item_id")  // Clé étrangère vers Item
    private Item item;

    private int quantity;

    public double getTotalPrice() {
        return this.quantity * this.item.getCost();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
