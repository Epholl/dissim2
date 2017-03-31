package sk.epholl.dissim.generator;

/**
 * Created by Tomáš on 17.03.2017.
 */
public class TriangularRandom extends RandomGenerator<Double> {

    private final double min;
    private final double max;
    private final double peak;

    private double f;

    public TriangularRandom(double min, double max, double peak) {
        this.min = min;
        this.max = max;
        this.peak = peak;

        this.f = (peak - min) / (max - min);
    }

    @Override
    public Double nextValue() {
        final double val = nextDoubleValue();

        if (val < f) {
            return min + Math.sqrt(val * (max - min) * (peak - min));
        } else {
            return max - Math.sqrt((1 - val) * (max - min) * (max - peak));
        }
    }
}
