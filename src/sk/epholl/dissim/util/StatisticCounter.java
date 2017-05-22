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
        if (count == 0) {
            return 0;
        }
        return sum / count;
    }

    public long getCount() {
        return count;
    }

    public double getVariance() {
        if (count == 0) {
            return 0;
        }
        return Math.abs((sumSquared / count) - (getMean() * getMean()));
    }

    public double getDeviation() {
        return Math.sqrt(getVariance());
    }

    public double getLeftConfidenceInterval() {
        return nonNaN(getMean() - (CONFIDENCE_INTERVAL_90_PERCENT * getDeviation()) / Math.sqrt(count-1));
    }

    public double getRightConfidenceInterval() {
        return nonNaN(getMean() + (CONFIDENCE_INTERVAL_90_PERCENT * getDeviation()) / Math.sqrt(count-1));
    }

    public void clear() {
        sum = 0d;
        count = 0;
        sumSquared = 0d;
    }

    public StatisticCounter copy() {
        StatisticCounter counter = new StatisticCounter();
        counter.sum = this.sum;
        counter.count = this.count;

        counter.sumSquared = this.sumSquared;
        return counter;
    }

    @Override
    public String toString() {
        return "Statistic counter: " + getMean();
    }

    public String formatMeanWithConfidenceInterval() {
        return f(getMean()) + ", \n <" + f(getLeftConfidenceInterval()) + "; " + f(getRightConfidenceInterval()) + ">";
    }

    private String f(double val) {
        return String.format("%.4f", val);
    }

    private double nonNaN(double d) {
        return Double.isNaN(d)? 0.0 : d;
    }
}