import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Routes {

    private final static long MAX_DURATION = 300;
    private final static double MAX_SPEED = 200.0;

    private static class SegmentsMapper extends Mapper<Object, Text, Text, Text> {

        private static boolean isValid(Segment s) {
            return (s.speed <= MAX_SPEED &&
                    s.duration <= MAX_DURATION &&
                    (s.wasBusyStart() || s.wasBusyFinish()));
        }

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split(",");
            String outKey = parts[0] + "|" + parts[1];
            try {
                Segment segment = Segment.fromString(value.toString());
//                System.out.println(segment.toString() + "," + isValid(segment) + "," + segment.duration + "," + segment.speed);
                if (isValid(segment)) {
                    context.write(new Text(outKey), value);
                }
            }
            catch (ParseException e) {
                System.err.println(String.format("Invalid segment: %s", value.toString()));
            }
        }
    }

    private static class RoutesReducer extends Reducer<Text, Text, Text, Text> {

        private static final SimpleDateFormat dateFormat = DateFormatInjector.getDateFormat();

        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {
            Random random = new Random();
            int rid = random.nextInt(1000000);
            System.out.println(String.format("%d\t%s", rid, key.toString()));

//            IntWritable outKey = new IntWritable(Integer.parseInt(key.toString().split("\\|")[0]));

            int taxiId = Integer.parseInt(key.toString().split("\\|")[0]);

            List<Segment> segments = new ArrayList<>();
            for (Text v : values) {
                Segment seg;
                try {
                    seg = Segment.fromString(v.toString());
                    System.out.println(String.format("%d\t%s", rid, v.toString()));
                }
                catch (ParseException e) {
                    System.err.println(String.format("Invalid segment line: %s", v.toString()));
                    continue;
                }
                if (!seg.wasBusyStart() && !seg.wasBusyFinish()) {
                    continue;
                }
                segments.add(seg);
            }
            Route route = new Route();
            List<Route> routes = new ArrayList<>();
            for (Segment seg: segments) {
                if (seg.isLast()) {
                    route.addSegment(seg);

                    if (route.isValid()) {
                        routes.add(route);
                    }
                    route = new Route();
                    continue;
                }
                else {
                    route.addSegment(seg);
                }
            }
            for (Route r: routes) {
                List<String> routePoints = new ArrayList<>();
                for (Double rp: r.getRoutePoints()) {
                    routePoints.add(String.valueOf(rp));
                }
                Text outKey = new Text(String.format("%d|%s",
                        taxiId,
                        dateFormat.format(r.getFinishDate())));

                String outString = String.format("%s\t%s\t%s\t%s",
                        r.distance(),
                        r.goesThroughAirport(),
                        r.revenue(),
                        Utils.join(",", routePoints));
                context.write(outKey, new Text(outString));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Routes");
        job.setJarByClass(Routes.class);
        job.setMapperClass(SegmentsMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setPartitionerClass(TaxiIdPartitioner.class);
        job.setGroupingComparatorClass(GroupComparator.class);
        job.setSortComparatorClass(KeyComparator.class);

        job.setReducerClass(RoutesReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
