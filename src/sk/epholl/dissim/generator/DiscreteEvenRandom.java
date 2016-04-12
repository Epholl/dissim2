package sk.epholl.dissim.generator;

/**
 * Created by Tomáš on 09.03.2016.
 */
public class DiscreteEvenRandom extends RandomGenerator<Integer> {

    private int min;
    private int max;

    private int range;

    public DiscreteEvenRandom(int min, int max) {
        this.min = min;
        this.max = max + 1;

        this.range = (max + 1) - min;
    }

    @Override
    public Integer nextValue() {
        return min + nextIntValue(range);
    }
}
