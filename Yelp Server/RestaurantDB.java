package ca.ece.ubc.cpen221.mp5;

import java.io.FileReader;
import java.io.BufferedReader;

import java.util.HashMap;
import java.util.HashSet;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.Set;

// TODO: This class represents the Restaurant Database.
// Define the internal representation and 
// state the rep invariant and the abstraction function.

public class RestaurantDB {
	// RI: No string in any of the three files should be identical
	// All string in the files should be in JSON format
	// The List of review, user and restaurant should never decrease
	// The addRestaurant method should not run concurrently with the query or
	// getRestaurant method
	// The addReview method should not run concurrently with the randomReview
	// method
	//
	// AF A list of all the information within the file
	// A suitable return to all the different client methods that follows the
	// rep inv
	//
	Object lock1 =new Object();
	Object lock2 = new Object();
	public static List<User> users;
	public static List<Review> reviews;
	public static List<Restaurant> restaurants;

	/**
	 * Create a database from the Yelp dataset given the names of three files:
	 * <ul>
	 * <li>One that contains data about the restaurants;</li>
	 * <li>One that contains reviews of the restaurants;</li>
	 * <li>One that contains information about the users that submitted reviews.
	 * </li>
	 * </ul>
	 * The files contain data in JSON format.
	 * 
	 * @param restaurantJSONfilename
	 *            the filename for the restaurant data
	 * @param reviewsJSONfilename
	 *            the filename for the reviews
	 * @param usersJSONfilename
	 *            the filename for the users
	 */
	public RestaurantDB(String restaurantJSONfilename, String reviewsJSONfilename, String usersJSONfilename) {
		// TODO: Implement this method

		restaurants = new LinkedList<Restaurant>();
		reviews = new LinkedList<Review>();
		users = new LinkedList<User>();

		JSONParser parser = new JSONParser();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(restaurantJSONfilename));
			String line;
			while ((line = reader.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				Restaurant RestaurantData = new Restaurant(jsonObject);
				restaurants.add(RestaurantData);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(reviewsJSONfilename));
			String line;
			while ((line = reader.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				Review ReviewData = new Review(jsonObject);
				reviews.add(ReviewData);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(usersJSONfilename));
			String line;
			while ((line = reader.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				User UserData = new User(jsonObject);
				users.add(UserData);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param restaurant
	 *            The restaurant that you want a review in
	 * @return A string of the review or a suitable response if there is no
	 *         review to the restaurant
	 */
	public String randomReview(String restaurant) {
		synchronized (lock2) {
			List<String> businessIDs = new LinkedList<String>();
			// Grabs all businessIDs for the restaurant
			for (int x = 0; x < restaurants.size(); x++) {
				if (restaurants.get(x).getName().equals(restaurant)) {
					businessIDs.add(restaurants.get(x).getBusiness_id());
				}
			}
			// If no businessID to the restaurant then reply that no review
			if (businessIDs.size() == 0) {
				return ("No reviews for this restaurant: " + restaurant);
			}

			// Grab a review for the businessID
			for (int x = 0; x < reviews.size(); x++) {
				if (reviews.get(x).getBusiness_ID().equals(businessIDs.get(0))) {
					Map<String, String> Result = new HashMap<String, String>();
					Result.put("text", reviews.get(x).getText());
					return JSONObject.toJSONString(Result);
				}
			}
			return ("No reviews for this restaurant: " + restaurant);
		}
	}

	/**
	 * 
	 * @param businessID
	 *            The business ID that you want a restaurant for
	 * @return The restaurant for the corresponding business ID or a suitable
	 *         response if there is no restaurant to that business ID
	 */
	public String getRestaurant(String businessID) {
		// Grab a restaurant with the businessID
		synchronized (lock1) {
			for (int x = 0; x < restaurants.size(); x++) {
				if (restaurants.get(x).getBusiness_id().equals(businessID)) {
					Map<String, String> Result = new HashMap<String, String>();
					Result.put("name", restaurants.get(x).getName());
					return JSONObject.toJSONString(Result);
				}
			}
			return ("No restaurant that correlates to this ID: " + businessID);
		}
	}

	/**
	 * 
	 * @param RestaurantDetails
	 *            A new restaurant detail in JSON format
	 * @return A boolean on whether a restaurant was successfully added or was
	 *         not. Will return false if client tries to add restaurant with the
	 *         same name that is in the database
	 */
	public boolean addRestaurant(String RestaurantDetails) {
		synchronized (lock1) {
			JSONParser parser = new JSONParser();
			try {
				Object restaurantobj = parser.parse(RestaurantDetails);
				JSONObject newRestaurant = (JSONObject) restaurantobj;
				// Checks to see if there is already a restaurant name in the
				// database for that restaurant
				for (int x = 0; x < restaurants.size(); x++) {
					if (restaurants.get(x).getName().equals((String) newRestaurant.get("name"))) {
						return false;
					}
				}
				Restaurant newResData = new Restaurant(newRestaurant);
				restaurants.add(newResData);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * 
	 * @param UserDetail
	 *            A new user detail in JSON format
	 * @return A boolean on whether a user was successfully added. Will return
	 *         false if there is already a user in the database with the same
	 *         UserID
	 */
	public boolean addUser(String UserDetail) {
		JSONParser parser = new JSONParser();
		try {
			Object userobj = parser.parse(UserDetail);
			JSONObject newUser = (JSONObject) userobj;
			// Checks to see if the userDetail has the same UserID to any user
			// in the database
			for (int x = 0; x < users.size(); x++) {
				if (users.get(x).getUser_ID().equals((String) newUser.get("user_id"))) {
					return false;
				}
			}
			User newUserData = new User(newUser);
			users.add(newUserData);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param ReviewDetail
	 *            A new review detail in JSON format
	 * @return A boolean on whether a review was successfully added or not. Will
	 *         return false if there already a reviewID that is equal to the
	 *         ReviewID in the reviewDetail
	 */
	public boolean addReview(String ReviewDetail) {
		synchronized (lock2) {
			JSONParser parser = new JSONParser();
			try {
				Object Reviewobj = parser.parse(ReviewDetail);
				JSONObject newReview = (JSONObject) Reviewobj;
				// Checks to see if there is already a reviewID equal to the one
				// in
				// reviewDetail
				for (int x = 0; x < reviews.size(); x++) {
					if (reviews.get(x).getReview_ID().equals((String) newReview.get("review_id"))) {
						return false;
					}
				}
				Review newReviewData = new Review(newReview);
				reviews.add(newReviewData);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * 
	 * @param queryString
	 *            A string that is a combination of restaurant names,
	 *            neighborhoods, categories, ratings and price level
	 * @return A set of all restaurant that that falls under the combination of
	 *         the queryString
	 */
	public Set<Restaurant> query(String queryString) {
		synchronized (lock1) {
			// Adding restaurants into a set
			Set<Restaurant> answer = new HashSet<Restaurant>();
			Queue<Set<Restaurant>> RestaurantSets = new LinkedList<Set<Restaurant>>();
			// Parses through the queryString and look at the first word, if its
			// any
			// of the keywords
			// then modify the restaurant set accordingly. Then the keyword and
			// word
			// associated is removed from
			// queryString and repeats until queryString is empty.
			while (queryString.length() > 0) {

				// If the first word of the query starts with "in", should add
				// all
				// restaurants that is in that area
				// to the set answers
				if (queryString.startsWith("in")) {
					String word = queryString.substring(queryString.indexOf("(") + 2, queryString.indexOf(")") - 1);
					for (Restaurant r : restaurants) {
						// Iterates through restaurants database and adds all
						// restaurant in the neighborhood
						if (r.getNeighbourhoods().contains(word)) {
							answer.add(r);
						}
					}
					queryString = queryString.substring(queryString.indexOf((")")) + 1);

					// If the first word is an AND symbol then push the set onto
					// the
					// queue
					// and create a new set.
					// This way I will just have to find the intersection of all
					// the
					// sets on the queue
				} else if (queryString.startsWith("&&")) {
					RestaurantSets.add(answer);
					answer = new HashSet<Restaurant>();
					queryString = queryString.substring(2);

					// If the first word is an OR symbol then do nothing. I want
					// to
					// keep adding restaurants into a set
					// until I see a AND which i would then push that set onto
					// the
					// queue and make a new set to add restaurants
				} else if (queryString.startsWith("||")) {
					queryString = queryString.substring(2);

					// If the first word of the query starts with "category",
					// should
					// add all
					// restaurants that with that category
					// to the set answers
				} else if (queryString.startsWith("category")) {
					String word = queryString.substring(queryString.indexOf("(", 1) + 2, queryString.indexOf(")") - 1);
					for (Restaurant r : restaurants) {
						if (r.getCategories().contains(word)) {
							answer.add(r);
						}
					}
					queryString = queryString.substring(queryString.indexOf((")")) + 1);

					// If the first word of the query starts with "price",
					// should
					// add all
					// restaurants that is in that price range
					// to the set answers
				} else if (queryString.startsWith("price")) {
					String word = queryString.substring(queryString.indexOf("(") + 1, queryString.indexOf(")"));
					// If price is a number then just add restaurant with that
					// price
					if (word.length() == 1) {
						for (Restaurant r : restaurants) {
							if (r.getPrice() == Long.parseLong(word.substring(0))) {
								answer.add(r);
							}
						}
					}
					// If price a range then add restaurants in the range
					else {
						for (Restaurant r : restaurants) {
							if (r.getPrice() >= Long.parseLong(word.substring(0, 1))
									&& r.getPrice() <= Long.parseLong(word.substring(word.length() - 1))) {
								answer.add(r);
							}
						}

					}
					queryString = queryString.substring(queryString.indexOf((")")) + 1);
				}

				// If the first word of the query starts with "rating", should
				// add
				// all
				// restaurants that is in that rating range
				// to the set answers
				else if (queryString.startsWith("rating")) {
					String word = queryString.substring(queryString.indexOf("(") + 1, queryString.indexOf(")"));
					// If rating is just a double then add restaurants with that
					// rating
					if (word.length() == 3) {
						for (Restaurant r : restaurants) {
							if (r.getStars() == Double.parseDouble(word.substring(0))) {
								answer.add(r);
							}
						}
					}
					// If rating is a range then add restaurants in that range
					else {
						for (Restaurant r : restaurants) {
							if (r.getStars() >= Double.parseDouble(word.substring(0, 3))
									&& r.getStars() <= Double.parseDouble(word.substring(word.length() - 3))) {
								answer.add(r);
							}
						}
					}
					queryString = queryString.substring(queryString.indexOf((")")) + 1);

					// If the first word of the query starts with "name", should
					// add
					// all
					// restaurants with the same name
					// to the set answers
				} else if (queryString.startsWith("name")) {
					// Add restaurants with the an equal name
					String word = queryString.substring(queryString.indexOf("(", 1) + 2, queryString.indexOf(")") - 1);
					for (Restaurant r : restaurants) {
						if (r.getName().equals(word)) {
							answer.add(r);
						}
					}
					queryString = queryString.substring(queryString.indexOf((")")) + 1);
				}
				// If first word is not a keyword than go the next letter
				else {
					queryString = queryString.substring(1);
				}
			}
			// Add last set to the stack
			RestaurantSets.add(answer);

			// Go through the queue and find intersection points for all of the
			// sets
			// in the queue
			while (RestaurantSets.size() > 1) {
				Set<Restaurant> r = RestaurantSets.poll();
				r.retainAll(RestaurantSets.poll());
				RestaurantSets.add(r);
			}
			return (RestaurantSets.poll());
		}
	}

}
