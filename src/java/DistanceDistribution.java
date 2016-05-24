import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class DistanceDistribution {

    public static class DistanceMapper extends Mapper<Object, Text, IntWritable, IntWritable> {

        private final static IntWritable one = new IntWritable(1);

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split(" ");
            double phi1 = Math.toRadians(Double.parseDouble(parts[2]));
            double phi2 = Math.toRadians(Double.parseDouble(parts[5]));

            double lambda1 = Math.toRadians(Double.parseDouble(parts[3]));
            double lambda2 = Math.toRadians(Double.parseDouble(parts[6]));

            double distance = Distance.distance(phi1, phi2, lambda1, lambda2);
            int round_distance = (int)Math.ceil(distance);
            context.write(new IntWritable(round_distance), one);
        }

    }

    public static class DistributionReducer
            extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(IntWritable key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Distance Distribution");
        job.setJarByClass(DistanceDistribution.class);
        job.setMapperClass(DistanceMapper.class);
        job.setCombinerClass(DistributionReducer.class);
        job.setReducerClass(DistributionReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
