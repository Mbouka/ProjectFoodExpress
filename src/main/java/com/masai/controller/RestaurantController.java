package com.masai.controller;

import com.masai.exception.RestaurantException;
import com.masai.model.Restaurant;
import com.masai.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

   /* @GetMapping("/menu")
    public String showRestaurantMenu(Model model) throws RestaurantException {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "menu";
    }*/
}
