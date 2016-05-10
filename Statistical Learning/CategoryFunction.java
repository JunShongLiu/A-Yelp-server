package ca.ece.ubc.cpen221.mp5.statlearning;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;

public class CategoryFunction implements MP5Function {

    @Override
    public double f(Restaurant yelpRestaurant, RestaurantDB db) {
        Map<String, Double> map = new HashMap<String, Double>();
        double count = 0.0;
        List<String> categories = new LinkedList<String>();
        List<List<String>> listOfCateg = new LinkedList<List<String>>();
        List<Restaurant> listOfRest = new LinkedList<Restaurant>();
        listOfRest = db.restaurants;
        categories = yelpRestaurant.getCategories();
        
        for (Restaurant r : listOfRest) {
            listOfCateg.add(r.getCategories());
        }
        
        for (List<String> list : listOfCateg) {
            for (String categ : list) {
                if (!map.containsKey(categ)) {
                    count++;
                    map.put(categ, count);
                }
            }
        }
        
        double total = 0.0;
        for (String s : categories) {
            total += map.get(s);
        }
        return total;
    }

}
