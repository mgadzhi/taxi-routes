import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by gadzhi on 22/05/16.
 */
public class Segment {
    public final int taxiId;
    public final Date startDate;
    public final Location startLocation;
    public final Status startStatus;
    public final Date finishDate;
    public final Location finishLocation;
    public final Status finishStatus;

    public final double speed;
    public final long duration;

    private enum Status {
        EMPTY,
        FULL
    }

    private static final SimpleDateFormat dateFormat = DateFormatInjector.getDateFormat();

    public Segment(int taxiId,
                   Date startDate,
                   Location startLocation,
                   String startStatus,
                   Date finishDate,
                   Location finishLocation,
                   String finishStatus) {
        this.taxiId = taxiId;
        this.startDate = startDate;
        this.startLocation = startLocation;
        this.startStatus = startStatus.equals("E") ? Status.EMPTY: Status.FULL;
        this.finishDate = finishDate;
        this.finishLocation = finishLocation;
        this.finishStatus = finishStatus.equals("E") ? Status.EMPTY: Status.FULL;

        double distance = Distance.distance(startLocation, finishLocation);
        long timeDiff = TimeUnit.MILLISECONDS.toSeconds(finishDate.getTime() - startDate.getTime());

        this.duration = timeDiff;
        this.speed = distance / (timeDiff / 3600.0);
    }

    public static String statusString(Status s) {
        return s == Status.EMPTY ? "E" : "M";
    }

    public static Segment fromString(String s) throws ParseException {
        String[] parts = s.split(",");
        int tid = Integer.parseInt(parts[0]);
        Date sd = dateFormat.parse(parts[1].replace("'", ""));
        Location sl = new Location(Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
        String ss = parts[4].replace("'", "");
        Date fd = dateFormat.parse(parts[5].replace("'", ""));
        Location fl = new Location(Double.parseDouble(parts[6]), Double.parseDouble(parts[7]));
        String fs = parts[8].replace("'", "");
        return new Segment(tid, sd, sl, ss, fd, fl, fs);
    }

    public boolean wasBusyStart() {
        return this.startStatus == Status.FULL;
    }

    public boolean wasBusyFinish() {
        return this.finishStatus == Status.FULL;
    }

    public boolean isFirst() {
        return startStatus == Status.EMPTY && finishStatus == Status.FULL;
    }

    public boolean isLast() {
        return startStatus == Status.FULL && finishStatus == Status.EMPTY;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                taxiId,
                dateFormat.format(startDate),
                startLocation.latitude,
                startLocation.longitude,
                statusString(startStatus),
                dateFormat.format(finishDate),
                finishLocation.latitude,
                finishLocation.longitude,
                statusString(finishStatus));
    }
}
