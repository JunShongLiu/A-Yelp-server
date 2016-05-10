package ca.ece.ubc.cpen221.mp5.statlearning;

import java.util.Set;

import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ca.ece.ubc.cpen221.mp5.*;

public class Algorithms {

    /**
     * Use k-means clustering to compute k clusters for the restaurants in the
     * database.
     * 
     * @param db
     *            the restaurant database with all the information
     * @param k
     *            the integer amount of clusters
     * @return a List of the sets of Restaurants in each cluster
     */
    public static List<Set<Restaurant>> kMeansClustering(int k, RestaurantDB db) {
        List<Restaurant> listOfRest = new LinkedList<Restaurant>();
        List<List<Double>> listofKpoints = new LinkedList<List<Double>>();
        List<Set<Restaurant>> listofsetofr = new LinkedList<Set<Restaurant>>();
        Set<Restaurant> setofr;
        List<Double> kPoint;
        boolean atCentroid = false;
        listOfRest = db.restaurants;
        // declare all random points for centroids
        for (int n = 0; n < k; n++) {
            double x = randNum();
            double y = randNum();
            kPoint = new LinkedList<Double>();
            kPoint.add(x);
            kPoint.add(y);
            listofKpoints.add(kPoint);
        }
        // if the K points areat the centroid
        while (atCentroid == false) {
            // every centroid point
            for (int count = 0; count < k; count++) {
                double k_x = listofKpoints.get(count).get(0);
                double k_y = listofKpoints.get(count).get(1);
                setofr = new HashSet<Restaurant>();
                // compare every restaurant
                for (Restaurant r : listOfRest) {
                    boolean closest = true;
                    double r_x = r.getLatitude();
                    double r_y = r.getLongitude();
                    double disToK = getDistance(k_x, k_y, r_x, r_y);
                    double disToK2;
                    // compare every restaurant to all centroid 'k' points
                    for (int count2 = 0; count2 < k; count2++) {
                        k_x = listofKpoints.get(count2).get(0);
                        k_y = listofKpoints.get(count2).get(1);
                        disToK2 = getDistance(k_x, k_y, r_x, r_y);
                        if (disToK2 < disToK) {
                            closest = false;
                        }
                    }
                    // check if the distance is the closest of all K points
                    if (closest)
                        setofr.add(r);
                }
                listofsetofr.add(setofr);
            }
            atCentroid = true;
            // for all sets compare with centroid, if all the the same then
            // k-means is done
            for (Set<Restaurant> l : listofsetofr) {
                if (listofKpoints.remove(0) == centroid(l))
                    listofKpoints.add(centroid(l));
                else {
                    listofKpoints.add(centroid(l));
                    atCentroid = false;
                }
            }
        }
        return listofsetofr;
    }

    /**
     * Converts a List of Sets of Restaurants into String JSON Format
     * 
     * @param clusters
     *            List of Sets of Restaurants
     * @return the string form in JSON format of the List of Sets of Restaurants
     */
    public static String convertClustersToJSON(List<Set<Restaurant>> clusters) {
        // initiate new JSON Object
        JSONObject JSONcluster = new JSONObject();
        // for each set of restaurant
        for (Set<Restaurant> s : clusters) {
            // for each restaurant, place in JSON Format
            for (Restaurant r : s) {
                JSONcluster.put("open", r.getOpen());
                JSONcluster.put("url", r.getURL());
                JSONcluster.put("longitude", r.getLongitude());
                JSONcluster.put("neighborhoods", r.getNeighbourhoods());
                JSONcluster.put("business_id", r.getBusiness_id());
                JSONcluster.put("name", r.getName());
                JSONcluster.put("categories", r.getCategories());
                JSONcluster.put("state", r.getState());
                JSONcluster.put("type", r.getType());
                JSONcluster.put("stars", r.getStars());
                JSONcluster.put("city", r.getCity());
                JSONcluster.put("full_address", r.getFull_Address());
                JSONcluster.put("review_count", r.getReview_Count());
                JSONcluster.put("photo_url", r.getPhoto_Url());
                JSONcluster.put("schools", r.getSchools());
                JSONcluster.put("latitude", r.getLatitude());
                JSONcluster.put("price", r.getPrice());
            }
        }
        return JSONcluster.toJSONString();
    }

    /**
     * Uses linear regression to find a function to predict a user's next review
     * rating
     * 
     * @param u
     *            the user you want to predict for
     * @param db
     *            the database with all the restaurant, user, review data
     * @param FeatureFunction
     *            the Function that extracts the feature of a restaurant
     * @return the function used to predict the user's next review rating
     */
    public static MP5Function getPredictor(User u, RestaurantDB db, MP5Function FeatureFunction) {
        List<Review> listofre = new LinkedList<Review>();
        List<Review> reviewByU = new LinkedList<Review>();
        List<Restaurant> listofRest = new LinkedList<Restaurant>();
        List<Restaurant> restReviewed = new LinkedList<Restaurant>();
        listofRest = db.restaurants;
        listofre = db.reviews;

        // find all reviews made by the user
        for (Review R : listofre) {
            if (R.getUser_ID() == u.getUser_ID())
                reviewByU.add(R);
        }
        // find all restaurants reviewed by the user
        for (Review review : reviewByU) {
            for (Restaurant rest : listofRest) {
                if (review.getBusiness_ID() == rest.getBusiness_id())
                    restReviewed.add(rest);
            }
        }
        // find Sxx, Syy, Sxy values
        double Sxx = Sxx(restReviewed, db, FeatureFunction);
        double Syy = Syy(reviewByU);
        double Sxy = Sxy(restReviewed, db, FeatureFunction, reviewByU);
        // find means
        double meanX = meanX(restReviewed, db, FeatureFunction);
        double meanY = meanY(reviewByU);

        return new RatingFunction(Sxx, Syy, Sxy, meanX, meanY, FeatureFunction);
    }

    /**
     * Uses linear regression to predict the review rating of the next review of
     * the user u. Goes through multiple feature functions to find with has the
     * highest R Squared value.
     * 
     * @param u
     *            the user you want to predict for
     * @param db
     *            the database with all the information
     * @param featureFunctionList
     *            a list of all the Feature Functions you want to test for a
     *            more accurate prediction
     * @return
     */
    public static MP5Function getBestPredictor(User u, RestaurantDB db, List<MP5Function> featureFunctionList) {
        List<Review> listofrev = new LinkedList<Review>();
        List<Review> reviewByU = new LinkedList<Review>();
        List<Restaurant> listofRest = new LinkedList<Restaurant>();
        List<Restaurant> restReviewed = new LinkedList<Restaurant>();
        List<Double> listofSxx = new LinkedList<Double>();
        List<Double> listofSyy = new LinkedList<Double>();
        List<Double> listofSxy = new LinkedList<Double>();
        List<Double> listofmeanX = new LinkedList<Double>();
        List<Double> listofmeanY = new LinkedList<Double>();
        List<MP5Function> listoffunc = new LinkedList<MP5Function>();
        double highestR = 0.0;
        double R = 0.0;
        listofRest = db.restaurants;
        listofrev = db.reviews;
        int indexOfBestPrediction = 0;

        // find all reviews made by the user
        for (Review Rev : listofrev) {
            if (Rev.getUser_ID() == u.getUser_ID())
                reviewByU.add(Rev);
        }
        // find all restaurants reviewed by the user
        for (Review review : reviewByU) {
            for (Restaurant rest : listofRest) {
                if (review.getBusiness_ID() == rest.getBusiness_id())
                    restReviewed.add(rest);
            }
        }
        // find Sxx, Syy, Sxy for all Feature Functions
        for (MP5Function function : featureFunctionList) {
            double Sxx = Sxx(restReviewed, db, function);
            double Syy = Syy(reviewByU);
            double Sxy = Sxy(restReviewed, db, function, reviewByU);
            listofSxx.add(Sxx);
            listofSyy.add(Syy);
            listofSxy.add(Sxy);
            // find means
            double meanX = meanX(restReviewed, db, function);
            double meanY = meanY(reviewByU);
            listofmeanX.add(meanX);
            listofmeanY.add(meanY);
            listoffunc.add(function);
        }
        // Find R^2 for all FeatureFunctions and find highest R^2 value
        for (int count = 0; count < listofSxx.size(); count++) {
            R = Math.pow(listofSxy.get(count), 2) / (listofSxx.get(count) * listofSyy.get(count));
            if (R > highestR) {
                indexOfBestPrediction = count;
                highestR = R;
            }
        }

        return new RatingFunction(listofSxx.get(indexOfBestPrediction), listofSyy.get(indexOfBestPrediction),
                listofSxy.get(indexOfBestPrediction), listofmeanX.get(indexOfBestPrediction),
                listofmeanY.get(indexOfBestPrediction), listoffunc.get(indexOfBestPrediction));
    }

    /**
     * Returns a random double
     * 
     * @return random double
     */
    private static double randNum() {
        Random r = new Random();
        return 1000.0 * r.nextDouble();
    }

    /**
     * Returns the distance between two sets of coordinates
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return distance between corresponding set of coordinates
     */
    private static double getDistance(double x1, double y1, double x2, double y2) {
        double changeInX = x2 - x1;
        double changeInY = y2 - y1;
        return Math.pow(Math.pow(changeInX, 2) + Math.pow(changeInY, 2), 0.5);
    }

    /**
     * Finds the centroid (average position) of a Set of Restaurants
     * 
     * @param s
     *            set of restaurants to find centroid
     * @return List of X and Y - centroid of the set of restaurants
     */
    private static List<Double> centroid(Set<Restaurant> s) {
        List<Double> n = new LinkedList<Double>();
        double totalX = 0.0;
        double totalY = 0.0;
        for (Restaurant r : s) {
            totalX += r.getLatitude();
            totalY += r.getLongitude();
        }
        double AVGX = totalX / (double) s.size();
        double AVGY = totalY / (double) s.size();
        n.add(AVGX);
        n.add(AVGY);
        return n;
    }

    /**
     * Determines to Sxx value 
     * 
     * @param listrest
     *      list of restaurants
     * @param db
     *      restaurant database
     * @param FeatureFunction
     *      the specific function which returns the feature of a restaurant
     * @return
     *      the Sxx value
     */
    private static double Sxx(List<Restaurant> listrest, RestaurantDB db, MP5Function FeatureFunction) {
        double total = 0.0;
        double Sxx = 0.0;
        double mean;
        for (Restaurant rest : listrest) {
            total += FeatureFunction.f(rest, db);
        }
        mean = total / (double) listrest.size();
        for (Restaurant rest2 : listrest) {
            Sxx += Math.pow((FeatureFunction.f(rest2, db) - mean), 2);
        }
        return Sxx;
    }

    /**
     * Determines the Syy value
     * 
     * @param listrev
     *       list of reviews
     * @return
     *      the Syy value
     */
    private static double Syy(List<Review> listrev) {
        double total = 0.0;
        double Syy = 0.0;
        double mean;
        for (Review rev : listrev) {
            total += rev.getStars();
        }
        mean = total / (double) listrev.size();
        for (Review rev2 : listrev) {
            Syy += Math.pow((rev2.getStars() - mean), 2);
        }
        return Syy;
    }

    /**
     * determines the Sxy value
     * 
     * @param listrest
     *      list of restaurants
     * @param db
     *      restaurant database
     * @param FeatureFunction
     *      the certain function that return of specific feature of a restaurant
     * @param listrev
     *      list of reviews
     * @return
     *      the Sxy value
     */
    private static double Sxy(List<Restaurant> listrest, RestaurantDB db, MP5Function FeatureFunction,
            List<Review> listrev) {
        double totalX = 0.0;
        double totalY = 0.0;
        double Sxy = 0.0;
        double meanY;
        double meanX;
        for (Review rev : listrev) {
            totalY += rev.getStars();
        }
        for (Restaurant rest : listrest) {
            totalX += FeatureFunction.f(rest, db);
        }
        meanY = totalY / (double) listrev.size();
        meanX = totalX / (double) listrest.size();
        for (int count = 0; count < listrev.size(); count++) {
            Sxy += ((listrev.get(count).getStars() - meanY) * (FeatureFunction.f(listrest.get(count), db) - meanX));
        }
        return Sxy;
    }
    /**
     * determines the mean of the rating of the list of reviews
     * 
     * @param listrev
     *      list of reviews
     * @return
     *      returns the average rating of the reviews
     */
    private static double meanY(List<Review> listrev) {
        double total = 0.0;
        double mean;
        for (Review rev : listrev) {
            total += rev.getStars();
        }
        mean = total / (double) listrev.size();
        return mean;
    }

    /**
     * Calulates the mean of a set of values according to their featurefunction
     * 
     * @param listrest
     *      list of restaurants 
     * @param db
     *      restaurant database
     * @param FeatureFunction
     *      function that returns the certain feature of the restaurant
     * @return
     *      mean of the feature 
     */
    private static double meanX(List<Restaurant> listrest, RestaurantDB db, MP5Function FeatureFunction) {
        double total = 0.0;
        double mean;
        for (Restaurant rest : listrest) {
            total += FeatureFunction.f(rest, db);
        }
        mean = total / (double) listrest.size();
        return mean;
    }
}