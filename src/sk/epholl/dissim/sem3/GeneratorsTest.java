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
}