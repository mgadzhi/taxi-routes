import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by gadzhi on 23/05/16.
 */
public class TaxiIdPartitioner extends Partitioner<Text, Text> {

    static final String keySeparator = KeySeparatorInjector.getKeySeparator();

    @Override
    public int getPartition(Text key, Text value, int partitionsNum) {
        String[] keyParts = key.toString().split(keySeparator);
        int taxiID = keyParts[0].hashCode();
        return taxiID % partitionsNum;
    }
}
