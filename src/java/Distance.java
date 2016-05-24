/**
 * Created by gadzhi on 22/05/16.
 */
public class Distance {
    private final static double r = 6371.009;

    public static double distance(double latitude1,
                                  double longitude1,
                                  double latitude2,
                                  double longitude2) {
        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);
        longitude1 = Math.toRadians(longitude1);
        longitude2 = Math.toRadians(longitude2);
        double delta_phi = latitude2 - latitude1;
        double delta_lambda = longitude2 - longitude1;
        double phi_m = (latitude1 + latitude2) / 2;

        return r * Math.sqrt(Math.pow(delta_phi, 2) + Math.pow(Math.cos(phi_m) * delta_lambda, 2));
    }

    public static boolean isCloseToAirport(double latitude, double longitude) {
        return distance(latitude, longitude, Location.AIRPORT.latitude, Location.AIRPORT.longitude) <= 1.0;
    }

    public static double distance(Location l1, Location l2) {
        return distance(l1.latitude, l1.longitude, l2.latitude, l2.longitude);
    }

    public static boolean isCloseToAirport(Location loc) {
        return isCloseToAirport(loc.latitude, loc.longitude);
    }
}
