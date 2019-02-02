package parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Converters {
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static final String timeDelim = ":";


    /**
     * This method takes a formatted date string e.g. yyyy-MM-dd
     * and returns the unix timestamp
     *
     * @return
     */
    public static Converter getDateConverter() {
        return (String value) -> {
            if (value == null || value.isEmpty()) {
                return 0;
            }
            try {
                Date temp = format.parse(value);
                return new Long(temp.getTime()).intValue();
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        };
    }


    /**
     * This method takes a formatted time string e.g. hh:mm
     * and returns the time in minutes
     *
     * @return
     */
    public static Converter getTimeConverter() {
        return Converters::timeToMinutes;
    }


    /**
     * This method takes a formatted float string and returns
     * the float value
     *
     * @return
     */
    public static Converter getFloatConverter() {
        return (String value) -> {
            if (!Utils.isValidFloat(value)) {
                return -1;
            }
            return Objects.hashCode(Float.valueOf(value));
        };
    }

    /**
     * This method takes a string trims the whitespace and returns a unique hash.
     *
     * @return
     */
    public static Converter getStringConverter() {
        return (String value) -> {
            if (value == null || value.isEmpty()) return -1;
            return Objects.hashCode(value.trim());
        };
    }


    /**
     * This method takes a formatted time string e.g. 02:14
     * and returns an int of the total minutes
     * For example if the input was `02:14` the output would be 134 or
     * (hours * 60) + minutes
     *
     * @param time
     * @return
     */
    private static int timeToMinutes(String time) {
        if (time == null || time.isEmpty()) {
            return -1;
        }
        String[] segments = time.split(timeDelim);
        if (segments.length != 2) {
            return -1;
        }

        String hours = segments[0];
        String minutes = segments[1];
        if (!Utils.isValidInteger(hours) || !Utils.isValidInteger(minutes)) {
            return -1;
        }
        return (Integer.valueOf(hours) * 60) + Integer.valueOf(minutes);
    }
}
