import java.text.SimpleDateFormat;

/**
 * Created by gadzhi on 23/05/16.
 */
public class DateFormatInjector {
    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
