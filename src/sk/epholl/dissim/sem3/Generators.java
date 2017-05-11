package sk.epholl.dissim.sem3;

import sk.epholl.dissim.generator.*;

/**
 * Created by Tomáš on 09.05.2017.
 */
public class Generators {

    private RandomGenerator<Double> customerEntryRandom;

    private RandomGenerator<Integer> amountOfRepairsGenerator;

    private RandomGenerator<Double> takingOrderTimeGenerator;

    private RandomGenerator<Double> enrollingCarTimeGenerator;

    private RandomGenerator<Double> returningCarTimeGenerator;

    private RandomGenerator<Integer> repairDurationGenerator;

    public Generators(final double entryMultiplier) {
        recreateCustomerEntryGeneratorWithMultiplier(entryMultiplier);

        amountOfRepairsGenerator =
                new EmpiricRandom.Builder<Integer>() // amount of repairs needed
                        .addGenerator(0.0544d, new Constant<>(1))
                        .addGenerator(0.3206d, new Constant<>(2))
                        .addGenerator(0.3061d, new Constant<>(3))
                        .addGenerator(0.2109d, new Constant<>(4))
                        .addGenerator(0.0986d, new Constant<>(5))
                        .addGenerator(0.0094d, new Constant<>(6))
                        .get();

        takingOrderTimeGenerator = new ContinuousEvenRandom(70, 310); // seconds

        enrollingCarTimeGenerator = new ContinuousEvenRandom(80, 160); // seconds

        returningCarTimeGenerator = new ContinuousEvenRandom(123, 257); // seconds

        repairDurationGenerator = new EmpiricRandom.Builder<Integer>() // MINUTES
                .addGenerator(0.7, new DiscreteEvenRandom(2, 20))
                .addGenerator(0.2, new EmpiricRandom.Builder<Integer>()
                        .addGenerator(0.1, new DiscreteEvenRandom(10, 40))
                        .addGenerator(0.6, new DiscreteEvenRandom(41, 61))
                        .addGenerator(0.3, new DiscreteEvenRandom(62, 100))
                        .get())
                .addGenerator(0.1, new DiscreteEvenRandom(120, 260))
                .get();
    }

    public void recreateCustomerEntryGeneratorWithMultiplier(double multiplier) {
        customerEntryRandom =
                new ExponentialRandom(2d, 1170d / multiplier);
    }

    public RandomGenerator<Double> getCustomerEntryRandom() {
        return customerEntryRandom;
    }

    public RandomGenerator<Integer> getAmountOfRepairsGenerator() {
        return amountOfRepairsGenerator;
    }

    public RandomGenerator<Double> getTakingOrderTimeGenerator() {
        return takingOrderTimeGenerator;
    }

    public RandomGenerator<Double> getEnrollingCarTimeGenerator() {
        return enrollingCarTimeGenerator;
    }

    public RandomGenerator<Double> getReturningCarTimeGenerator() {
        return returningCarTimeGenerator;
    }

    public RandomGenerator<Integer> getRepairDurationMinutesGenerator() {
        return repairDurationGenerator;
    }
}
