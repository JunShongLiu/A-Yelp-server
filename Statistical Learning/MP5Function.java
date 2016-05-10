package ca.ece.ubc.cpen221.mp5.statlearning;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;

public interface MP5Function {

	/**
	 * Compute a feature function given a restaurant
	 * 
	 * @param yelpRestaurant
	 * @return the value of the feature function
	 */
	public double f(Restaurant yelpRestaurant, RestaurantDB db);
	
}
