package ca.ece.ubc.cpen221.mp5;

import static org.junit.Assert.*;

import org.junit.Test;

public class QueryTest {

	@Test
	public void testQuery() {
		RestaurantDB test =  new RestaurantDB("data/restaurants.json", "data/reviews.json", "data/users.json");
		//test.query("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\"))");
		//test.query("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && price(1)");
		//test.query("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && price(1..2)");
		test.query("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && price(2..3) && rating(3.0..3.2)");
		test.query("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && price(2..3)");
		test.query("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && price(2..3) && rating(3.0..3.2) && name(\"Lotus House\")");
		//test.query("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && price(3..4)");
		//test.query("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && rating(3.0)");
		//test.query("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && rating(3.0..3.5)");
		//test.query("(in(\"Telegraph Ave\") || in(\"UC Campus Area\")) && (category(\"Cafes\"))");
		//test.query("category(\"Cafes\") && in(\"Telegraph Ave\")"); 
	}

}
