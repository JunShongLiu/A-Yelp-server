package ca.ece.ubc.cpen221.mp5;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// TODO: Use this class to represent a restaurant.
// State the rep invariant and abs

public class Restaurant {
	// Rep Invariant: This file is only set once and after never modified (WHAT
	// ABOUT CHECK REP METHOD)
	// Abs Function: Represent the complete data on a restaurant for Yelp
	private boolean Open;
	private String URL;
	private double Longitude;
	private List<String> Neighbourhoods = new LinkedList<String>();
	private String business_id;
	private String Name;
	private List<String> Categories = new LinkedList<String>();
	private String State;
	private String Type;
	private double Stars;
	private String City;
	private String Full_Address;
	private long Review_Count;
	private String Photo_Url;
	private List<String> Schools = new LinkedList<String>();
	private double Latitude;
	private long price;

	public Restaurant(JSONObject obj) {
		Open = (boolean) obj.get("open");
		URL = (String) obj.get("url");
		Longitude = (Double) obj.get("longitude");
		
		JSONArray neighbourhoods = (JSONArray) obj.get("neighborhoods");
		Iterator<String> iterator = neighbourhoods.iterator();
		while (iterator.hasNext()) {
			Neighbourhoods.add(iterator.next());
		}
		
		business_id = (String)obj.get("business_id");
		Name = (String)obj.get("name");
		
		JSONArray categories = (JSONArray) obj.get("categories");
		Iterator<String> iterator2 = categories.iterator();
		while (iterator2.hasNext()) {
			Categories.add(iterator2.next());
		}
		
		State = (String)obj.get("state");
		Type = (String)obj.get("type");
		Stars = (double)obj.get("stars");
		City = (String)obj.get("city");
		Full_Address = (String)obj.get("full_address");
		Review_Count = (long)obj.get("review_count");
		Photo_Url = (String)obj.get("photo_url");
		
		JSONArray schools = (JSONArray) obj.get("schools");
		Iterator<String> iterator3 = schools.iterator();
		while (iterator3.hasNext()) {
			Schools.add(iterator3.next());
		}
		
		Latitude = (double)obj.get("latitude");
		price = (long)obj.get("price");

	}

	public boolean getOpen() {
		return Open;
	}

	public String getURL() {
		return URL;
	}

	public double getLongitude() {
		return Longitude;
	}

	public List<String> getNeighbourhoods() {
		return Neighbourhoods;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public String getName() {
		return Name;
	}

	public List<String> getCategories() {
		return Categories;
	}

	public String getState() {
		return State;
	}

	public String getType() {
		return Type;
	}

	public double getStars() {
		return Stars;
	}

	public String getCity() {
		return City;
	}

	public String getFull_Address() {
		return Full_Address;
	}

	public long getReview_Count() {
		return Review_Count;
	}

	public String getPhoto_Url() {
		return Photo_Url;
	}

	public List<String> getSchools() {
		return Schools;
	}

	public double getLatitude() {
		return Latitude;
	}

	public long getPrice() {
		return price;
	}

	/*
	 * public void setOpen(boolean Open){ this.Open = Open; }
	 * 
	 * 
	 * public void setURL(String URL){ this.URL= URL; }
	 * 
	 * public void setLongtitude(double Longtitude){ this.Longtitude =
	 * Longtitude; }
	 * 
	 * public void addNeighbourhoods(String Neighbourhoods){
	 * this.Neighbourhoods.add(Neighbourhoods); }
	 * 
	 * public void setBusinessID(String Business_ID){ this.business_id =
	 * Business_ID; }
	 * 
	 * public void setName(String Name){ this.Name = Name; }
	 * 
	 * public void addCategories(String Categories){
	 * this.Categories.add(Categories); }
	 * 
	 * public void setState(String State){ this.State = State; }
	 * 
	 * public void setType(String Type){ this.Type = Type; }
	 * 
	 * public void setStars(Double Stars){ this.Stars = Stars; }
	 * 
	 * public void setCity(String City){ this.City = City; }
	 * 
	 * public void setFull_Address(String Full_Address){ this.Full_Address =
	 * Full_Address; }
	 * 
	 * public void setReview_Count(int Review_Count){ this.Review_Account =
	 * Review_Count; }
	 * 
	 * public void setPhoto_URL(String Photo_URL){ this.Photo_Url = Photo_URL; }
	 * 
	 * public void setSchools(String Schools){ this.Schools = Schools; }
	 * 
	 * public void setLatitude(double Latitude){ this.Latitude = Latitude; }
	 * 
	 * public void setPrice(int Price){ this.price = Price; }
	 */

}
