package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class RestaurantClient implements Runnable {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	// Rep invariant: socket, in, out != null

	public RestaurantClient(String hostname, int port) throws IOException {
		socket = new Socket(hostname, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	/**
	 * Send a request to the server. Requires this is "open".
	 * 
	 * @param String
	 *            request A client's request
	 * @throws IOException
	 *             if network or server failure
	 */
	public void sendRequest(String request) throws IOException {
		out.print(request + "\n");// Don't know if need new line
		out.flush(); // important! make sure x actually gets sent
	}

	/**
	 * Get a reply from the next request that was submitted. Requires this is
	 * "open".
	 * 
	 * @return the requested string
	 * @throws IOException
	 *             if network or server failure
	 */
	public String getReply() throws IOException {
		String reply = in.readLine();
		if (reply == null) {
			throw new IOException("connection terminated unexpectedly");
		}
		try {
			return reply;
		} catch (NumberFormatException nfe) {
			throw new IOException("misformatted reply: " + reply);
		}
	}

	/**
	 * Closes the client's connection to the server. This client is now
	 * "closed". Requires this is "open".
	 * 
	 * @throws IOException
	 *             if close fails
	 */
	public void close() throws IOException {
		in.close();
		out.close();
		socket.close();
	}

	public void run() {
		try {
			RestaurantClient client2 = new RestaurantClient("localhost", RestaurantDBServer.YELP_PORT);
			client2.sendRequest("randomReview(Fondue Fred)");
			String reply = client2.getReply();
			System.out.println(reply);
			
			client2.sendRequest("getRestaurant(BJKIoQa5N2T_oDlLVf467Q)");
			String reply2 = client2.getReply();
			System.out.println(reply2);
			
			client2.sendRequest("addRestaurant({\"open\": true, \"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", \"longitude\": -122.260408, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"gclB3ED6uk6viWlolSb_uAZZ\", \"name\": \"Cafe 3\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 2.0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave Telegraph Ave Berkeley, CA 94701\", \"review_count\": 9, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1})");
			String reply3 = client2.getReply();
			System.out.println(reply3);
			
			client2.sendRequest("addReview({\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.Oh, and the pasta is even worse than the pizza.\", \"stars\": 2, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", \"date\": \"2006-07-26\"})");
			String reply5 = client2.getReply();
			System.out.println(reply5);
			
			client2.sendRequest("in(\"Telegraph Ave\") && (category(\"Chinese\") || category(\"Italian\")) && price(2..3)");
			String reply6 = client2.getReply();
			System.out.println(reply6);
			client2.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
		(new Thread(new RestaurantClient("localhost",RestaurantDBServer.YELP_PORT))).start();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
		
		try {
			RestaurantClient client = new RestaurantClient("localhost", RestaurantDBServer.YELP_PORT);

			client.sendRequest("randomReview(Cafe 33)");
			String reply = client.getReply();
			System.out.println(reply);
			
			client.sendRequest("getRestaurant(FWadSZw0G7HsgKXq7gHTnw)");
			String reply2 = client.getReply();
			System.out.println(reply2);
			
			client.sendRequest("addRestaurant({\"open\": true, \"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", \"longitude\": -122.260408, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"name\": \"Cafe 333\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 2.0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave Telegraph AveBerkeley, CA 94701\", \"review_count\": 9, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1})");
			String reply3 = client.getReply();
			System.out.println(reply3);
			
			client.sendRequest("addRestaurant({\"open\": true, \"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", \"longitude\": -122.260408, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"name\": \"Cafe 333\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 2.0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave Telegraph AveBerkeley, CA 94701\", \"review_count\": 9, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1})");
			String reply4 = client.getReply();
			System.out.println(reply4);
			
			client.sendRequest("addReview({\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.Oh, and the pasta is even worse than the pizza.\", \"stars\": 2, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", \"date\": \"2006-07-26\"})");
			String reply5 = client.getReply();
			System.out.println(reply5);
			
			client.sendRequest("addUser({\"url\": \"http://www.yelp.com/user_details?userid=754HGCLgGJLh1VU_WtGjsw\", \"votes\": {\"funny\": 21, \"useful\": 38, \"cool\": 21}, \"review_count\": 44, \"type\": \"user\", \"user_id\": \"754HGCLgGJLh1VU_WtGjsw\", \"name\": \"Steven C.\", \"average_stars\": 3.22727272727273})");
			String reply6 = client.getReply();
			System.out.println(reply6);
			
			client.sendRequest("addUser({\"url\": \"http://www.yelp.com/user_details?userid=754HGCLgGJLh1VU_WtGjsw\", \"votes\": {\"funny\": 21, \"useful\": 38, \"cool\": 21}, \"review_count\": 44, \"type\": \"user\", \"user_id\": \"754HGC548LgGJLh1VU_WtGjsw\", \"name\": \"Steven C.\", \"average_stars\": 3.22727272727273})");
			String reply7 = client.getReply();
			System.out.println(reply7);
			
			client.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		
	}
}
