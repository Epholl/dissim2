package sk.epholl.dissim.sem3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tomáš on 09.05.2017.
 */
public class GeneratorsTest {

    @Test
    public void baseicNewInstanceTest() {
        Generators gen = new Generators(1.0d);
    }

    @Test
    public void entryMultiplierTest() {
        double timeSum = 0;
        int basicCount = 0;
        double doubleTimeSum = 0;
        int doubleCount = 0;
        Generators basicGen = new Generators(1d);
        Generators doubleGen = new Generators(2d);

        final double limit = 10000000d;

        while (timeSum < limit) {
            timeSum += basicGen.getCustomerEntryRandom().nextValue();
            basicCount++;
        }

        while (doubleTimeSum < limit) {
            doubleTimeSum += doubleGen.getCustomerEntryRandom().nextValue();
            doubleCount++;
        }

        System.out.println("Basic: " + basicCount + ", double: " + doubleCount);
        System.out.println("Doubled: " + (basicCount*2) + ", halved: " + (doubleCount/2));
    }

    @Test
    public void repairTimeConsoleTest() {
        double timeSum = 0;
        int testCases = 1000000;
        int repairsCount = 0;
        Generators g = new Generators(1d);
        int[] histogram = new int[7];

        for (int i = 0; i < testCases; i++) {
            int repairs = g.getAmountOfRepairsGenerator().nextValue();
            histogram[repairs]++;
            repairsCount += repairs;
            for (int j = 0; j < repairs; j++) {
                timeSum += g.getRepairDurationMinutesGenerator().nextValue();
            }
        }

        System.out.println("Average repairs count: " + ((double)repairsCount) / testCases);
        System.out.println("Average single repair duration: " + timeSum / repairsCount);
        System.out.println("Average total repair duration: " + timeSum / testCases);
        System.out.print("Histogram: ");
        for (int i = 0; i < histogram.length; i++) {
            System.out.print(i + ": " + ((double)histogram[i])/testCases + ", ");
        }

        System.out.println();
        System.out.println("Average revenue per customer: " + ((timeSum / testCases)/60)*25 );
    }
}