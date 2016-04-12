package sk.epholl.dissim.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomáš on 09.03.2016.
 */
public class EmpiricRandom<T extends Number> extends RandomGenerator<T> {

    public static class Builder<T extends Number> {

        private EmpiricRandom<T> random;
        private double cumulativeProbability;

        public Builder() {
            random = new EmpiricRandom<T>();
            cumulativeProbability = 0.0;
        }

        public Builder<T> addGenerator(double probability, RandomGenerator<T> generator) {

            for (RandomPair<T> pair: random.randoms) {
                if (pair.generator == generator) {
                    throw new IllegalArgumentException("A generator that is already used within the random was inserted again.");
                }
            }

            RandomPair<T> pair = new RandomPair<>(cumulativeProbability + probability, generator);
            random.randoms.add(pair);

            cumulativeProbability += probability;

            return this;
        }

        public EmpiricRandom<T> get() {

            if (Double.compare(cumulativeProbability, 1.0) != 0) {
                throw new IllegalStateException("The total cumulative probability within an empiric random generator did not equal 1: " + cumulativeProbability);
            }

            return random;
        }
    }

    private final List<RandomPair<T>> randoms;

    private EmpiricRandom() {
        randoms = new ArrayList<>();
    }


    @Override
    public T nextValue() {
        double val = nextDoubleValue();

        for (RandomPair<T> pair : randoms) {
            if (val <pair.getProbability()) {
                return pair.getValue();
            }
        }

        throw new IllegalStateException("No generator was chosen to return a value for " + val);
    }

    private static class RandomPair<T extends Number> {
        private double probability;
        private RandomGenerator<T> generator;

        public RandomPair(double probability, RandomGenerator<T> generator) {
            this.probability = probability;
            this.generator = generator;
        }

        public double getProbability() {
            return probability;
        }

        public T getValue() {
            return generator.nextValue();
        }
    }
}
