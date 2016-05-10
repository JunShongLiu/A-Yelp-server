package ca.ece.ubc.cpen221.mp5;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// TODO: Use this class to represent a Yelp review.

public class Review {

	private String Type;
	

	private String Business_ID;
	private long Cool;
	private long Useful;
	private long Funny;
	private String Review_ID;
	private String Text;
	long Stars;
	private String User_ID;
	private String Date;
	
	public Review(JSONObject obj){
		Type = (String)obj.get("type");
		Business_ID = (String)obj.get("business_id");
		
		JSONObject votes = (JSONObject)obj.get("votes");
		Cool = (long)votes.get("cool");
		Useful = (long)votes.get("useful");
		Funny = (long)votes.get("funny");
		
		Review_ID = (String)obj.get("review_id");
		Text = (String)obj.get("text");
		Stars = (long)obj.get("stars");
		User_ID = (String)obj.get("user_id");
		Date = (String)obj.get("date");
	}

	public String getType() {
		return Type;
	}

	public String getBusiness_ID() {
		return Business_ID;
	}

	public long getCool() {
		return Cool;
	}

	public long getUseful() {
		return Useful;
	}

	public long getFunny() {
		return Funny;
	}

	public String getReview_ID() {
		return Review_ID;
	}

	public String getText() {
		return Text;
	}

	public long getStars() {
		return Stars;
	}

	public String getUser_ID() {
		return User_ID;
	}

	public String getDate() {
		return Date;
	}
	
	
	
}
