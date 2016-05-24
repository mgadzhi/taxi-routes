import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by gadzhi on 23/05/16.
 */
public class GroupComparator extends WritableComparator {
    protected GroupComparator() {
        super(Text.class, true);
    }

    static final String keySeparator = KeySeparatorInjector.getKeySeparator();

    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
        Text t1 = (Text) w1;
        Text t2 = (Text) w2;

        if (t1 == t2) {
            return 0;
        }

        String[] t1Parts = t1.toString().split(keySeparator);
        String[] t2Parts = t2.toString().split(keySeparator);
        Integer taxi1ID = Integer.valueOf(t1Parts[0]);
        Integer taxi2ID = Integer.valueOf(t2Parts[0]);

        return taxi1ID.compareTo(taxi2ID);
    }
}
