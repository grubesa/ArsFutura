package grubi.arsfutura.util;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class TimeUtil {

    public static String getElapsedTime(String stringTime) {
        DateTime time = new DateTime(stringTime);
        DateTime now = new DateTime();
        Period period = new Period(time, now);

        if (period.getYears() > 0) {
            return period.getYears() + " years ago";
        } else if (period.getMonths() > 0) {
            return period.getMonths() + " months ago";
        } else if (period.getWeeks() > 0) {
            return period.getWeeks() + " weeks ago";
        } else if (period.getDays() > 0) {
            return period.getDays() + " days ago";
        } else if (period.getHours() > 0) {
            return period.getHours() + " hours ago";
        } else if (period.getMinutes() > 0) {
            return period.getMinutes() + " minutes ago";
        } else if (period.getSeconds() > 0) {
            return period.getSeconds() + " seconds ago";
        } else {
            return "";
        }
    }

}
