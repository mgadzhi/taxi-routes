import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by gadzhi on 23/05/16.
 */
public class Utils {
    public static String join(String separator, List<String> list)
    {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String item : list)
        {
            if (first)
                first = false;
            else
                sb.append(separator);
            sb.append(item);
        }
        return sb.toString();
    }

    public static String stripQuotes(String quotedString) {
        return quotedString.replace("'", "");
    }

    public static Date roundToHour(Date d) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.HOUR, 1);

        return c.getTime();
    }
}
