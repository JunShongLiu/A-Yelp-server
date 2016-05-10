package ca.ece.ubc.cpen221.mp5;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// TODO: Use this class to represent a Yelp user.

public class User {
	private String URL;
	private long Funny;
	private long Useful;
	private long Cool;
	private long Review_Count;
	private String Type;
	private String User;
	private String User_ID;
	private String Name;
	private double Average_Stars;
	
	public User(JSONObject obj){
		URL = (String)obj.get("url");
		JSONObject votes = (JSONObject)obj.get("votes");
		Funny = (long)votes.get("funny");
		Useful = (long)votes.get("useful");
		Cool = (long)votes.get("cool");
		Review_Count = (long)obj.get("review_count");
		Type = (String)obj.get("type");
		User_ID = (String)obj.get("user_id");
		Name = (String)obj.get("name");
		Average_Stars = (double)obj.get("average_stars");
	}

	public String getURL() {
		return URL;
	}

	public long getFunny() {
		return Funny;
	}

	public long getUseful() {
		return Useful;
	}

	public long getCool() {
		return Cool;
	}

	public long getReview_Count() {
		return Review_Count;
	}

	public String getType() {
		return Type;
	}

	public String getUser() {
		return User;
	}

	public String getUser_ID() {
		return User_ID;
	}

	public String getName() {
		return Name;
	}

	public double getAverage_Stars() {
		return Average_Stars;
	}
	
	
}
