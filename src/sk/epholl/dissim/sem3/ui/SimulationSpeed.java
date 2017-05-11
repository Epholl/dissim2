package sk.epholl.dissim.sem3.ui;

/**
 * Created by Tomáš on 11.05.2017.
 */
public class SimulationSpeed {

    public static final String PREFIX = "1s = ";

    public static final double DAY = 60.0 * 60 * 24;
    public static final double HOUR = 60.0 * 60;
    public static final double MINUTE = 60.0;
    public static final double SECOND = 1.0;


    public static final double[] SPEEDS = {
            SECOND,
            SECOND * 5,
            SECOND * 10,
            SECOND * 30,
            MINUTE,
            MINUTE * 2,
            MINUTE * 5,
            MINUTE * 10,
            MINUTE * 30,
            HOUR,
            HOUR * 2,
            HOUR * 6,
            HOUR * 12,
            DAY,
    };

    public static final String[] UNITS_NAMES = {
            "d", "h", "m", "s"
    };

    public static final double[] UNITS_DURATIONS = {
            DAY, HOUR, MINUTE, SECOND
    };

    public static String getLabel(int i) {
        final double speed = SPEEDS[i];
        for (int i1 = 0; i1 < UNITS_DURATIONS.length; i1++) {
            double unit = UNITS_DURATIONS[i1];
            if (speed >= unit) {
                return PREFIX + formatSpeed(speed, unit) + UNITS_NAMES[i1];
            }
        }
        throw new AssertionError("Error while formatting speed label");
    }

    private static String formatSpeed(final double value, final double unit) {
        return String.format("%.1f", value / unit);
    }
}
