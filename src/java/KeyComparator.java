import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by gadzhi on 23/05/16.
 */
public class KeyComparator extends WritableComparator {

    final static String keySeparator = KeySeparatorInjector.getKeySeparator();
    final static SimpleDateFormat dateFormat = DateFormatInjector.getDateFormat();

    protected KeyComparator() {
        super(Text.class, true);
    }

//        private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:m:s");

    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
        Text t1 = (Text) w1;
        Text t2 = (Text) w2;

        if (t1 == t2) {
            return 0;
        }

        String[] t1Parts = t1.toString().split(keySeparator);
        String[] t2Parts = t2.toString().split(keySeparator);
        String taxi1Time = Utils.stripQuotes(t1Parts[1]);
        String taxi2Time = Utils.stripQuotes(t2Parts[1]);
        try {
            if (dateFormat.parse(taxi1Time).before(dateFormat.parse(taxi2Time))) {
                return -1;
            }
            else {
                return 1;
            }
        }
        catch (ParseException e) {
            // We cannot do much here.
            // The signature of this method is defined in superclass, so we cannot throw an exception.
            // The meaning of the output of this method is also established so we cannot return an error status code.
            // Thus, clients of this method must make sure that objects w1 and w2 are valid.
            // I prefer to fail and see the reason rather than have invalid data on later stages ;)
            System.err.println(String.format("Impossible happened! %s and %s cannot be compared",
                    t1.toString(),
                    t2.toString()));
            System.exit(1);
        }

        // This point is unreachable, but Java cannot derive it for some reason
        System.err.println(String.format("Unreachable point reached while compairing %s and %s",
                t1.toString(),
                t2.toString()));
        return 0;
    }
}
