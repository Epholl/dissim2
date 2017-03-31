package sk.epholl.dissim.generator;

import java.util.Random;

/**
 * Created by Tomáš on 09.03.2016.
 */
public abstract class RandomGenerator<T extends Number> {

    public static final Random seedGenerator = new Random();

    public static void setCustomGeneratorSeed(long customSeed) {
        seedGenerator.setSeed(customSeed);
    }

    private final Random random = new Random(seedGenerator.nextLong());

    protected final double nextDoubleValue() {
        return random.nextDouble();
    }

    protected final int nextIntValue(int max) {
        return random.nextInt(max);
    }

    public abstract T nextValue();
}
