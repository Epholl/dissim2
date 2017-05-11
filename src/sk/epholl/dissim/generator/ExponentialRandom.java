package sk.epholl.dissim.generator;

/**
 * Created by Tomáš on 17.03.2017.
 */
public class ExponentialRandom extends RandomGenerator<Double> {

    private double min;
    private double lambda;

    public ExponentialRandom(double min, double mean) {
        this.min = min;
        this.lambda = 1d/mean;
    }

    public ExponentialRandom(double mean) {
        this.min = 0d;
        this.lambda = 1d/mean;
    }

    @Override
    public Double nextValue() {
        return min + Math.log(1-nextDoubleValue())/(-lambda);
    }
}
