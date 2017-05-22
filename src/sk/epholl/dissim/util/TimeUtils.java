package sk.epholl.dissim.util;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tomáš on 12.05.2017.
 */
public class TimeUtils {
        public static final double workDayDuration = TimeUnit.HOURS.toSeconds(8);

        public static double getTimeOfDay(final double simulationTime) {
            return simulationTime % workDayDuration;
        }

        public static int getDay(final double simulationTime) {
            return (int)(simulationTime / workDayDuration) + 1;
        }

        public static double getNextDayStart(final double simulationTime) {
            return getDay(simulationTime) * workDayDuration;
        }

        public static double getTimeRemainingToday(final double simulationTime) {
            return workDayDuration - getTimeOfDay(simulationTime);
        }

        public static boolean willEventFinishToday(final double simulationTime, final double eventDuration) {
            final double remainingTimeToday = getTimeRemainingToday(simulationTime);
            return remainingTimeToday >= eventDuration;
        }

        public static double getEventFinishTimeAtomicEvent(final double simulationTime, final double eventDuration) {
            if (willEventFinishToday(simulationTime, eventDuration)) {
                return simulationTime + eventDuration;
            } else {
                return getNextDayStart(simulationTime);
            }
        }

        public static String formatDayTime(double simulationTime) {
            StringBuilder builder = new StringBuilder();
            builder.append("Day ")
                    .append(getDay(simulationTime))
                    .append(", ");
            final long hours = 7 + TimeUnit.SECONDS.toHours( (int) simulationTime) % 8;
            final long minutes = TimeUnit.SECONDS.toMinutes( (int) simulationTime) % 60;
            final long seconds = (int) simulationTime % 60;
            builder.append(String.format("%02d", hours))
                    .append(":")
                    .append(String.format("%02d", minutes))
                    .append(":")
                    .append(String.format("%02d", seconds));
            return builder.toString();
        }

        public static String formatTimePeriod(double time) {
            StringBuilder builder = new StringBuilder();

            final long hours = TimeUnit.SECONDS.toHours( (int) time);
            final long minutes = TimeUnit.SECONDS.toMinutes( (int) time) % 60;
            final long seconds = (int) time % 60;
            builder.append(String.format("%02d", hours))
                    .append(":")
                    .append(String.format("%02d", minutes))
                    .append(":")
                    .append(String.format("%02d", seconds));
            return builder.toString();
        }

    public static String formatTimePeriodDecimalSeconds(double time) {
        StringBuilder builder = new StringBuilder();

        final long hours = TimeUnit.SECONDS.toHours( (int) time);
        final long minutes = TimeUnit.SECONDS.toMinutes( (int) time) % 60;
        final double seconds = time % 60;
        builder.append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%.4f", seconds));
        return builder.toString();
    }
}
