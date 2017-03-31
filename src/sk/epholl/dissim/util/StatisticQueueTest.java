package sk.epholl.dissim.util;

import org.junit.Assert;
import org.junit.Test;
import sk.epholl.dissim.core.Simulation;
import sk.epholl.dissim.core.SimulationCore;
import sk.epholl.dissim.core.SimulationParameters;

/**
 * Created by Tomáš on 18.03.2017.
 */
public class StatisticQueueTest {

    private class MockParameters extends SimulationParameters {
        public MockParameters(long replicationCount, long warmupReplicationCount) {
            super(replicationCount, warmupReplicationCount);
        }
    }

    @Test
    public void testInsert() {
        MockSimulationCore mockSim = newMockSim();

        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();

        Assert.assertFalse(o1 == o2);
        Assert.assertFalse(o1.equals(o2));

        StatisticQueue<Object> queue = new StatisticQueue<>(mockSim);
        queue.enqueue(o1);
        mockSim.setMockTime(1000d);
        queue.dequeue();
        Assert.assertEquals(1, queue.getAverageQueueLength(), 0.00001d);
        mockSim.setMockTime(4000d);
        Assert.assertEquals(0.25d, queue.getAverageQueueLength(), 0.00001d);
        queue.enqueue(o2);
        queue.enqueue(o3);
        mockSim.setMockTime(7000d);
        Assert.assertEquals(1, queue.getAverageQueueLength(), 0.00001d);

    }

    private MockSimulationCore newMockSim() {
        MockSimulationCore mockSim = new MockSimulationCore(new MockParameters(1, 0));
        return mockSim;
    }

    private final class MockSimulationCore extends SimulationCore<MockParameters, Void, Void> {

        private double mockSimTime = 0d;

        public MockSimulationCore(MockParameters simulationParameters) {
            super(simulationParameters);
        }

        public void setMockTime(double newTime) {
            mockSimTime = newTime;
        }

        @Override
        public double getSimulationTime() {
            return mockSimTime;
        }

        @Override
        protected boolean simulationEndCondition() {
            return false;
        }

        @Override
        protected void singleIteration() {

        }

        @Override
        public Void getResults() {
            return null;
        }

        @Override
        public Void getState() {
            return null;
        }
    }
}