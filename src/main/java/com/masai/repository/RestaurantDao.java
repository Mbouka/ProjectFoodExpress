package com.masai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.masai.model.Address;
import com.masai.model.Restaurant;

@Repository
public interface RestaurantDao extends JpaRepository<Restaurant, Integer>{

	public List<Restaurant> findByAddress(Address address);

	public Restaurant findByMobileNumber(String mobileNo);

	List<Restaurant> findByRestaurantNameContainingIgnoreCase(String restaurantName);


	List<Restaurant> findByAddressCityContainingIgnoreCaseOrAddressStateContainingIgnoreCaseOrAddressCountryContainingIgnoreCaseOrAddressPinCodeContainingIgnoreCase(
			String city, String state, String country, String pinCode);

}
