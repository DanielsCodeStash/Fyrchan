package util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil
{
    public static String getDate()
    {
        DateTime dateTime = new DateTime();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("HHmmss");
        return dateTime.toString(dtf);

    }
}
