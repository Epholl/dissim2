package sk.epholl.dissim.generator;

/**
 * Created by Tomáš on 17.03.2017.
 */
public class ExponentialRandom extends RandomGenerator<Double> {

    private double lambda;

    public ExponentialRandom(double mean) {
        this.lambda = 1d/mean;
    }

    @Override
    public Double nextValue() {
        return Math.log(1-nextDoubleValue())/(-lambda);
    }
}
