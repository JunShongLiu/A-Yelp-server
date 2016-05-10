package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

// TODO: Implement a server that will instantiate a database, 
// process queries concurrently, etc.

public class RestaurantDBServer {

	public static final int YELP_PORT = 4952;
	private final static String restaurantJSONfilename = "data/restaurants.json";
	private static String reviewsJSONfilename = "data/reviews.json";
	private static String usersJSONfilename = "data/users.json";
	private static RestaurantDB Database;


	private String randomReview = "randomReview";
	private String getRestaurant = "getRestaurant";
	private String addRestaurant = "addRestaurant";
	private String addUser = "addUser";
	private String addReview = "addReview";

	private ServerSocket serverSocket;

	/**
	 * Constructor
	 * 
	 * @param port
	 *            The port number to use RestaurantDB Server
	 * @param RestaurantDetails
	 *            Contains all the information on a restaurant found on Yelp in
	 *            JSON format
	 * @param UserReviews
	 *            Contains all the user's reviews for restaurants in JSON format
	 * @param UserDetials
	 *            Contains all the user's detail on Yelp in JSON format
	 */
	public RestaurantDBServer(int port, String RestaurantDetails, String UserReviews, String UserDetails)
			throws IOException {
		// TODO: See the problem statement for what the arguments are.
		// TODO: Rename the arguments suitably.
		serverSocket = new ServerSocket(port);
		Database = new RestaurantDB(RestaurantDetails, UserReviews, UserDetails);
	}

	/**
	 * Start a RestaurantDBServer running on the default port.
	 */
	public static void main(String[] args) {

		try {
			RestaurantDBServer server = new RestaurantDBServer(YELP_PORT, restaurantJSONfilename, reviewsJSONfilename,
					usersJSONfilename);
			server.serve();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Run the server, listening for connections and handling them.
	 * 
	 * @throws IOException
	 *             if the main server socket is broken
	 */
	public void serve() throws IOException {
		while (true) {
			// block until a client connects
			final Socket socket = serverSocket.accept();
			// create a new thread to handle that client
			Thread handler = new Thread(new Runnable() {
				public void run() {
					try {
						try {
							handle(socket);
						} finally {
							socket.close();
						}
					} catch (IOException ioe) {
						// this exception wouldn't terminate serve(),
						// since we're now on a different thread, but
						// we still need to handle it
						ioe.printStackTrace();
					}
				}
			});

			// start the thread
			handler.start();
		}
	}

	/**
	 * Handle one client connection. Returns when client disconnects.
	 * 
	 * @param socket
	 *            socket where client is connected
	 * @throws IOException
	 *             if connection encounters an error
	 */
	private void handle(Socket socket) throws IOException {
		System.err.println("client connected");

		// get the socket's input stream, and wrap converters around it
		// that convert it from a byte stream to a character stream,
		// and that buffer it so that we can read a line at a time
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// similarly, wrap character=>bytestream converter around the
		// socket output stream, and wrap a PrintWriter around that so
		// that we have more convenient ways to write Java primitive
		// types to it.
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

		try {
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				System.err.println("request: " + line);
				try {
					// Client request a random review
					if (line.startsWith(randomReview) == true) {
						String restaurant = line.substring(line.indexOf("(") + 1, line.length() - 1);
						String review = Database.randomReview(restaurant);
						System.err.println(review);
						out.println(review);
					}
					// Client request a restaurant
					else if (line.startsWith(getRestaurant) == true) {
						String id = line.substring(line.indexOf("(") + 1, line.length() - 1);
						String review = Database.getRestaurant(id);
						System.err.println(review);
						out.println(review);
					}
					// Client request to add a new restaurant
					else if (line.startsWith(addRestaurant) == true) {
						String restaurant = line.substring(line.indexOf("(") + 1, line.length() - 1);
						boolean result = Database.addRestaurant(restaurant);
						if (result == false) {
							out.println("Restaurant already in database");
							System.err.println("Failure");
						} else {
							out.println("Restaurant was succesfully added");
							System.err.println("success");
						}
					//Client request to add a User
					} else if (line.startsWith(addUser) == true) {
						String user = line.substring(line.indexOf("(") + 1, line.length() - 1);
						boolean result = Database.addUser(user);
						if (result == false) {
							out.println("User was already added");
							System.err.println("Failure");
						} else {
							out.println("User was succesfully added");
							System.err.println("success");
						}
					//Client request to add a Review
					} else if (line.startsWith(addReview) == true) {
						String review = line.substring(line.indexOf("(") + 1, line.length() - 1);
						boolean result = Database.addReview(review);
						if (result == false) {
							out.println("Review was already made");
							System.err.println("Failure");
						} else {
							out.println("Review was successully added");
							System.err.println("success");
						}
					//Client request to pass a query
					} else {
						Set<Restaurant> result = Database.query(line);
							out.println("There is(are) " + result.size() + " Restaurant(s) that matches that query");
					}
				} catch (NumberFormatException e) {
					// complain about ill-formatted request
					System.err.println("reply: Not Correct Format");
					out.println("Not Correct Format\n");
				}
				// important! our PrintWriter is auto-flushing, but if it were
				// not:
				// out.flush();
			}
		} finally {
			out.close();
			in.close();
		}
	}
}
