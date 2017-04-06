package sk.epholl.dissim.util;

/**
 * Created by Tomáš on 18.03.2017.
 */
public class StatisticCounter {
    public static final double CONFIDENCE_INTERVAL_90_PERCENT = 1.645;

    private double sum;
    private long count;

    private double sumSquared;

    public void addValue(double value) {
        sum += value;
        count++;
        sumSquared += value*value;
    }

    public double getSum() {
        return sum;
    }

    public double getMean() {
        return sum / count;
    }

    public double getVariance() {
        return Math.abs((sumSquared / count) - (getMean() * getMean()));
    }

    public double getDeviation() {
        return Math.sqrt(getVariance());
    }

    public double getLeftConfidenceInterval() {
        return getMean() - (CONFIDENCE_INTERVAL_90_PERCENT * getDeviation()) / Math.sqrt(count-1);
    }

    public double getRightConfidenceInterval() {
        return getMean() + (CONFIDENCE_INTERVAL_90_PERCENT * getDeviation()) / Math.sqrt(count-1);
    }

    public void clean() {
        sum = 0d;
        count = 0;
        sumSquared = 0d;
    }
}