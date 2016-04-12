package sk.epholl.dissim.generator;

/**
 * Created by Tomáš on 09.03.2016.
 */
public class ContinuousEvenRandom extends RandomGenerator<Double> {

    private double min;
    private double max;

    private double range;

    public ContinuousEvenRandom(double min, double max) {
        this.min = min;
        this.max = max;

        this.range = max - min;
    }


    @Override
    public Double nextValue() {
        return min + (nextDoubleValue() * range);
    }
}
