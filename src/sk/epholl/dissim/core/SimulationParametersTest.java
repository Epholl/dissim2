package sk.epholl.dissim.core;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tomáš on 17.03.2017.
 */
public class SimulationParametersTest {
    @Test
    public void getReplicationCount() throws Exception {

        SimulationParameters param = new SimulationParameters(1000, 100);
        Assert.assertEquals(param.getReplicationCount(), 1000);
    }

    @Test
    public void getWarmupReplicationCount() throws Exception {
        SimulationParameters param = new SimulationParameters(10000, 100);
        Assert.assertEquals(param.getWarmupReplicationCount(), 100);
        param.setWarmupReplicationCount(1000);
        Assert.assertEquals(param.getWarmupReplicationCount(), 1000);
        param.setWarmupReplicationPercentage(50d);
        Assert.assertEquals(param.getWarmupReplicationCount(), 5000);
    }
}