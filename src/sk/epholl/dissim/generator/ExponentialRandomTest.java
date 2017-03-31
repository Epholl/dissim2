package sk.epholl.dissim.generator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tomáš on 17.03.2017.
 */
public class ExponentialRandomTest {
    @Test
    public void nextValue() throws Exception {

        ExponentialRandom random = new ExponentialRandom(100d);
        double sum = 0;

        for (int i = 0; i < 1000000; i++) {
            sum += random.nextValue();
        }

        System.out.println(sum/1000000);
    }

    @Test
    public void randomOutput() throws Exception {

        ExponentialRandom random = new ExponentialRandom(58d);

        for (int i = 0; i < 10000; i++) {
            System.out.println(random.nextValue());
        }
    }
}