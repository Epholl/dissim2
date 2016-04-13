package sk.epholl.dissim.util;

/**
 * Created by Tomáš on 13.04.2016.
 */
public class Utils {

    public static double kmhToMs(double kmh) {
        return kmh / 3.6;
    }

    public static double hoursToSeconds(double hours) {
        return hours * 3600;
    }

    public static double minutesToSeconds(double minutes) {
        return minutes * 60;
    }
}
