package sk.epholl.dissim.generator;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Tomáš on 17.03.2017.
 */
public class TriangularDistributionTest {

    @Test
    public void testGeneration() {
        TriangularRandom rng = new TriangularRandom(0d, 10d, 5d);

        int[] hst = new int[11];

        for (int i = 0; i < 100000; i++) {
            double val = rng.nextValue();
            for (int j = 0; j < hst.length; j++) {
                if (val < j) {
                    hst[j-1]++;
                    break;
                }
            }
        }
        System.out.println(Arrays.toString(hst));
    }

    @Test
    public void randomOutput() throws Exception {

        TriangularRandom random = new TriangularRandom(0, 50, 15);

        for (int i = 0; i < 10000; i++) {
            System.out.println(random.nextValue());
        }
    }
}