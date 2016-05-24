/**
 * Created by gadzhi on 22/05/16.
 */
public class Location {
    public final double latitude;
    public final double longitude;

    public static final Location AIRPORT = new Location(37.62131, -122.37896);

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", latitude, longitude);
    }
}
