package sk.epholl.dissim.carshop;

import sk.epholl.dissim.generator.*;

/**
 * Created by Tomáš on 17.03.2017.
 */
public class CarShopSimulationGenerators {

    private RandomGenerator<Double> customerEntryRandom;

    private RandomGenerator<Integer> amountOfRepairsGenerator;

    private RandomGenerator<Double> takingOrderTimeGenerator;

    private RandomGenerator<Double> enrollingCarTimeGenerator;

    private RandomGenerator<Double> carTransferTimeGenerator;

    private RandomGenerator<Double> returningCarTimeGenerator;

    private RandomGenerator<Integer> repairDurationGenerator;

    public CarShopSimulationGenerators() {
        customerEntryRandom =
                new ExponentialRandom(300d); // one every 300 seconds

        amountOfRepairsGenerator =
                new EmpiricRandom.Builder<Integer>() // amount of repairs needed
                        .addGenerator(0.4d, new Constant<>(1))
                        .addGenerator(0.15d, new Constant<>(2))
                        .addGenerator(0.14d, new Constant<>(3))
                        .addGenerator(0.12d, new Constant<>(4))
                        .addGenerator(0.1d, new Constant<>(5))
                        .addGenerator(0.09d, new Constant<>(6))
                        .get();

        takingOrderTimeGenerator = new ContinuousEvenRandom(70, 310); // seconds

        enrollingCarTimeGenerator = new ContinuousEvenRandom(80, 160); // seconds

        carTransferTimeGenerator = new TriangularRandom(120, 540, 240); //seconds

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

    public double getNextCustomerEnteranceDelay() {
        return customerEntryRandom.nextValue();
    }

    public int getNextAmountOfRepairs() {
        return amountOfRepairsGenerator.nextValue();
    }

    public double getNextOrderTakingDuration() {
        return takingOrderTimeGenerator.nextValue();
    }

    public double getNextCarEnrollingDuration() {
        return enrollingCarTimeGenerator.nextValue();
    }

    public double getNextCarTransferDuration() {
        return carTransferTimeGenerator.nextValue();
    }

    public double getNextCarReturningTime() {
        return returningCarTimeGenerator.nextValue();
    }

    public int getNextRepairDuration() {
        return repairDurationGenerator.nextValue() * 60;
    }
}
