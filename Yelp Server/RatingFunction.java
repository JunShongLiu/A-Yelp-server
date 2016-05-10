package ca.ece.ubc.cpen221.mp5.statlearning;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantDB;

public class RatingFunction implements MP5Function {
    MP5Function function;
    double Sxx;
    double Syy;
    double Sxy;
    double meanX;
    double meanY;

    // constructor for ratingfunction
    RatingFunction(double Sxx, double Syy, double Sxy, double meanX, double meanY, MP5Function function) {
        this.Sxx = Sxx;
        this.Syy = Syy;
        this.Sxy = Sxy;
        this.meanX = meanX;
        this.meanY = meanY;
        this.function = function;
    }

    @Override
    // return the least square regression prediction
    public double f(Restaurant yelpRestaurant, RestaurantDB db) {
        double b = Sxy / Sxx;
        double a = meanY - b * meanX;
        return a * function.f(yelpRestaurant, db) + b;
    }

}
