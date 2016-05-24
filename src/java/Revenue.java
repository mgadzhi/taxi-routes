import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gadzhi on 23/05/16.
 */
public class Revenue {

    final static String keySeparator = KeySeparatorInjector.getKeySeparator();
    final static SimpleDateFormat dateFormat = DateFormatInjector.getDateFormat();

    private static class AirportRoutesFilter extends Mapper<Object, Text, Text, DoubleWritable> {
        private static DoubleWritable outValue = new DoubleWritable();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split("\t");
            String[] outKeyPats = parts[0].split(keySeparator);
            Date tripFinishDate;
            Date tripHourDate;
            try {
                tripFinishDate = dateFormat.parse(outKeyPats[1]);
                tripHourDate = Utils.roundToHour(tripFinishDate);

                Text outKey = new Text(dateFormat.format(tripHourDate));

                if (parts[2].equals("true")) {
                    double revenue = Double.parseDouble(parts[3]);
                    outValue.set(revenue);
                    context.write(outKey, outValue);
                }
            }
            catch (ParseException e) {
                System.err.println(String.format("Invalid date in key: %s", key.toString()));
                // If we come here, it definitely means bug in the code, because invalid data is filtered out earlier.
                // So, we should fail immediately.
                System.exit(1);
            }
        }
    }

    private static class RevenueReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

        private DoubleWritable sum = new DoubleWritable();

        public void reduce(Text key, Iterable<DoubleWritable> values, Context context)
                throws IOException, InterruptedException {
            double revenue = 0.0;
            for (DoubleWritable value: values) {
                revenue += value.get();
            }
            sum.set(revenue);
            context.write(key, sum);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Airport routes revenue");
        job.setJarByClass(Revenue.class);
        job.setMapperClass(AirportRoutesFilter.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        job.setCombinerClass(RevenueReducer.class);

//        job.setPartitionerClass(TaxiIdPartitioner.class);
//        job.setGroupingComparatorClass(GroupComparator.class);
//        job.setSortComparatorClass(KeyComparator.class);

        job.setReducerClass(RevenueReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
