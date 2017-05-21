package sk.epholl.dissim.util;

import org.junit.Assert;
import org.junit.Test;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Condition;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decision;

import static org.junit.Assert.*;

/**
 * Created by Tomáš on 21.05.2017.
 */
public class StatisticStateCounterTest {

    private static class MockSimTimeProvider implements SimTimeProvider {

        public double simTime;
        @Override
        public double getSimulationTime() {
            return simTime;
        }
    }

    @Test
    public void basicTest() {
        MockSimTimeProvider stp = new MockSimTimeProvider();
        StatisticStateCounter<Worker1Decision> counter = new StatisticStateCounter<>(stp);

        counter.setCurrentState(Worker1Decision.ReturnCar);
        counter.setCurrentState(Worker1Decision.ReturnCar);
        counter.setCurrentState(Worker1Decision.ReturnCar);
        counter.setCurrentState(Worker1Decision.TakeOrder);

        Assert.assertEquals(3, counter.getStateAmount(Worker1Decision.ReturnCar));
        Assert.assertEquals(4, counter.getTotalStateAmounts());
    }

}