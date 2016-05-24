import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by gadzhi on 22/05/16.
 */
public class Route {
    private List<Segment> segments;
    private boolean goesThroughAirport;
    private boolean noGaps;
    private static final double MAX_GAP = 300.0;

    private static double STARTING_FEE = 3.5;
    private static double KM_TAX = 1.71;

    public Route() {
        this.segments = new ArrayList<>();
        this.goesThroughAirport = false;
        this.noGaps = true;
    }

    public void addSegment(Segment s) {
        this.segments.add(s);
        if (!this.goesThroughAirport &&
                (Distance.isCloseToAirport(s.startLocation) || Distance.isCloseToAirport(s.finishLocation))) {
            this.goesThroughAirport = true;
        }
        checkGap();
    }

    private void checkGap() {
        if (!noGaps || segments.size() < 2) {
            return;
        }
        Segment last = segments.get(segments.size() - 1);
        Segment previous = segments.get(segments.size() - 2);
        if (TimeUnit.MILLISECONDS.toSeconds(last.startDate.getTime() - previous.finishDate.getTime()) > MAX_GAP)
            noGaps = false;
    }

    public boolean goesThroughAirport() {
        return this.goesThroughAirport;
    }

    public boolean isEmpty() {
        return segments.isEmpty();
    }

    public boolean isValid() {
        Segment first = segments.get(0);
        Segment last = segments.get(segments.size() - 1);
        return (noGaps &&
                first.isFirst() &&
                last.isLast());
    }

    public Location start() {
        if (segments.isEmpty())
            return null;
        return segments.get(0).startLocation;
    }

    public Location finish() {
        if (segments.isEmpty())
            return null;
        return segments.get(segments.size() - 1).finishLocation;
    }

    public Date getFinishDate() {
        if (segments.size() > 0) {
            return segments.get(segments.size() - 1).finishDate;
        }
        return null;
    }

    public long duration() {
        if (segments.isEmpty())
            return 0;
        Segment first = segments.get(0);
        Segment last = segments.get(segments.size() - 1);
        return TimeUnit.MILLISECONDS.toSeconds(last.finishDate.getTime() - first.startDate.getTime());
    }

    public int distance() {
        return (int) Math.floor(Distance.distance(start(), finish()));
    }

    public double revenue() {
        return STARTING_FEE + (KM_TAX * distance());
    }

    public List<Double> getRoutePoints() {
        List<Double> points = new ArrayList<>();
        for (Segment seg: segments) {
            points.add(seg.startLocation.latitude);
            points.add(seg.startLocation.longitude);
            points.add(seg.finishLocation.latitude);
            points.add(seg.finishLocation.longitude);
        }
        return points;
    }
}
