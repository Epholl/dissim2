package sk.epholl.dissim.carshop;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tomáš on 26.03.2017.
 */
public class CarShopSimulationCoreTest {

    @Test
    public void testRun() {
        CarShopSimulationParameters params = new CarShopSimulationParameters(1, 5, 30);
        CarShopSimulationCore sim = new CarShopSimulationCore(params);
        sim.singleIteration();
    }

}