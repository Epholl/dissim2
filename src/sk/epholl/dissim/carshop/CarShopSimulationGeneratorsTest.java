package sk.epholl.dissim.carshop;

import org.junit.Test;
import sk.epholl.dissim.generator.Constant;
import sk.epholl.dissim.generator.EmpiricRandom;
import sk.epholl.dissim.generator.RandomGenerator;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Tomáš on 17.03.2017.
 */
public class CarShopSimulationGeneratorsTest {

    @Test
    public void testAmountOfRepairsGenerator() {
        RandomGenerator<Integer> rng = new EmpiricRandom.Builder<Integer>()
                .addGenerator(0.4d, new Constant<>(1))
                .addGenerator(0.15d, new Constant<>(2))
                .addGenerator(0.14d, new Constant<>(3))
                .addGenerator(0.12d, new Constant<>(4))
                .addGenerator(0.1d, new Constant<>(5))
                .addGenerator(0.09d, new Constant<>(6))
                .get();

        int[] results = new int[7];
        int testcases = 1000000;
        double sum = 0;
        for (int i = 0; i < testcases; i++) {
            int val = rng.nextValue();
            sum+= val;
            results[val]++;
        }

        System.out.println(sum / testcases);
        System.out.println(Arrays.toString(results));
    }

    @Test
    public void testBasicStatistics() {
        CarShopSimulationGenerators gen = new CarShopSimulationGenerators();

        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = 0;

        double singleRun;
        int repairs;
        int replications = 1000000;
        for (int i = 0; i < replications; i++) {
            singleRun = 0d;
            singleRun += gen.getNextOrderTakingDuration();
            singleRun += gen.getNextCarEnrollingDuration();
            singleRun += gen.getNextCarTransferDuration();
            repairs = gen.getNextAmountOfRepairs();
            for (int r = 0; r < repairs; r++) {
                singleRun += gen.getNextRepairDuration();
            }
            singleRun += gen.getNextCarTransferDuration();
            singleRun += gen.getNextCarReturningTime();

            if (singleRun > max) {
                max = singleRun;
            }
            if (singleRun < min) {
                min = singleRun;
            }
            sum += singleRun;
        }

        System.out.println("Min: " + min + ", max: " + max + " avg: " + (sum/replications));
    }

    @Test
    public void testGenerator() {
        CarShopSimulationGenerators gen = new CarShopSimulationGenerators();

        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = 0;

        double singleRun;
        int repairs;
        int replications = 1000000;
        for (int i = 0; i < replications; i++) {
            singleRun = 0d;
            repairs = gen.getNextAmountOfRepairs();
            for (int r = 0; r < repairs; r++) {
                singleRun += gen.getNextRepairDuration();
                break;
            }

            if (singleRun > max) {
                max = singleRun;
            }
            if (singleRun < min) {
                min = singleRun;
            }
            sum += singleRun;
        }

        System.out.println("Min: " + min + ", max: " + max + " avg: " + (sum/replications));
    }
}