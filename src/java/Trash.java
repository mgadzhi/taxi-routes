import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gadzhi on 22/05/16.
 */
public class Trash {
    public static void main(String[] args) {
        System.out.println("Hello");

        final double r = 6371.009;
        String value = "9 1267402225.0 37.79076 -122.40255 1267402400.0 37.78538 -122.40024";

        String[] parts = value.split(" ");
        double phi1 = Math.toRadians(Double.parseDouble(parts[2]));
        double phi2 = Math.toRadians(Double.parseDouble(parts[5]));

        double lambda1 = Math.toRadians(Double.parseDouble(parts[3]));
        double lambda2 = Math.toRadians(Double.parseDouble(parts[6]));

        double delta_phi = phi2 - phi1;
        double delta_lambda = lambda2 - lambda1;
        double phi_m = (phi1 + phi2) / 2;

        double distance = r * Math.sqrt(Math.pow(delta_phi, 2) + Math.pow(Math.cos(phi_m) * delta_lambda, 2));
        int round_distance = (int)Math.ceil(distance);
        System.out.println(distance);
        System.out.println(round_distance);

        String ds1 = "2010-02-28 23:46:08";
        String ds2 = "2010-03-01 04:02:28";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:m:s");

        String testKey = "706|'2010-03-11 12:10:20'";
        String ds3 = testKey.split("\\|")[1].replace("'", "");
        try {
            System.out.println(sdf.parse(ds3));
            System.out.println(sdf.parse(ds3).getClass());
        }
        catch (ParseException e) {
            System.out.println("Nope... " + ds3);
        }
        try {
            System.out.println(sdf.parse(ds1).before(sdf.parse(ds2)));
        }
        catch (ParseException e) {

        }

        String suspiciousString0 = "706,2010-03-04 2:28:26,37.61605,-122.38832,M,2010-03-04 2:29:26,37.61703,-122.39835,M";
        String suspiciousString = "706,2010-03-01 12:0:40,37.75769,-122.42118,M,2010-03-01 12:1:33,37.75973,-122.42133,M";
        try {
            Segment s = Segment.fromString(suspiciousString);
            System.out.println(s);
            System.out.println(Distance.distance(s.startLocation, s.finishLocation));
            System.out.println(Distance.distance(s.startLocation.latitude,
                    s.startLocation.longitude,
                    s.finishLocation.latitude,
                    s.finishLocation.longitude));
            System.out.println(s.speed);
        }
        catch (ParseException e) {
            System.out.println(e.getStackTrace());
        }

        System.out.println("True? " + true);

        try {
            Date d1 = sdf.parse("2010-03-01 12:0:40");
            Date d2 = sdf.parse("2010-03-01 12:0:0");
            Date d3 = sdf.parse("2010-03-01 11:0:59");
            Date d4 = sdf.parse("2010-03-01 23:59:59");

            System.out.println(Utils.roundToHour(d1));
            System.out.println(Utils.roundToHour(d2));
            System.out.println(Utils.roundToHour(d3));
            System.out.println(Utils.roundToHour(d4));
        }
        catch (ParseException e) {

        }
    }
}
