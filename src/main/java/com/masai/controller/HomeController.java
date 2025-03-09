package com.masai.controller;

import com.masai.exception.CustomerNotFound;
import com.masai.exception.ItemNotFoundException;
import com.masai.exception.RestaurantException;
import com.masai.model.Customer;
import com.masai.model.FoodCart;
import com.masai.model.Item;
import com.masai.model.Restaurant;
import com.masai.repository.RestaurantDao;
import com.masai.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private RestaurantServiceImpl restaurantService;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private CartServiceImpl cartService;

   @GetMapping("/")
    public String home(Model model){
       try {
           List<Restaurant> top10Restaurants = restaurantService.getTop10Restaurants();
           model.addAttribute("restaurants", top10Restaurants);
       } catch (RestaurantException e) {
           model.addAttribute("error", e.getMessage());
       }
       return "index";
   }
    @GetMapping("/restaurantUser")
    public String restaurantForm(){
        return "restaurant";
    }

   @GetMapping("/login")
   public String login(){
       return "login";
   }
   @GetMapping("/menu")
   public String menu(Model model){
       try {
           List<Restaurant> allRestaurants = restaurantService.getAllRestaurants();
           model.addAttribute("restaurants", allRestaurants);
       } catch (RestaurantException e) {
           model.addAttribute("error", e.getMessage());
       }
       return "menu";
   }
    @GetMapping("/book")
    public String book(){
        return "book";
    }
    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/restaurants/{restaurantId}")
    public String getRestaurantDetails(@PathVariable Integer restaurantId, Model model) throws RestaurantException {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "detail";
    }
   /*@GetMapping("/cart/{itemId}")
    public String cartPage(@PathVariable Integer itemId, Model model) throws ItemNotFoundException {
       Item item = itemService.viewItem(itemId);
       model.addAttribute("item",item);
        return "panier";  // Une page cart.html pour afficher le contenu du panier
    }*/
    @GetMapping("/cart")
    public String cartPage(Model model) throws CustomerNotFound {
        Customer customer = customerService.getAuthenticatedCustomer();
        FoodCart cart = cartService.getCartByCustomer(customer);
        model.addAttribute("cart", cart);
        return "panier";  // Une page cart.html pour afficher le contenu du panier
    }

    @GetMapping("/checkout")
    public String billPage() {
        return "checkout";  // Une page cart.html pour afficher le contenu du panier
    }

    @GetMapping("/payment-success")
    public String paymentSuccess() {
        return "payment-success"; // Affiche la page de confirmation
    }

    @GetMapping("/searchResult")
    public String searchRestaurants(@RequestParam("query") String query, Model model) {
        List<Restaurant> restaurants = restaurantService.searchAll(query);
       // List<Item> items = restaurantService.searchItems(query);
        //model.addAttribute("query", query);
        model.addAttribute("restaurants", restaurants);
     //  model.addAttribute("items", items);

        return "searchResult";  // Le nom de la vue Thymeleaf
    }

    @GetMapping("/searchByLocation")
    public String searchRestaurantsByLocation(@RequestParam String location, Model model) {
        List<Restaurant> restaurants = restaurantService.searchByLocation(location);
        model.addAttribute("restaurants", restaurants);
        return "searchResult";  // Affiche les résultats dans la page searchResult.html
    }
}

