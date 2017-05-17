package sk.epholl.dissim.sem3.entity.deciders;

import org.junit.Assert;
import org.junit.Test;
import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.util.deciders.Comparator;
import sk.epholl.dissim.util.deciders.Condition;
import sk.epholl.dissim.util.deciders.Decider;
import sk.epholl.dissim.util.deciders.ValueProvider;

import static org.junit.Assert.*;

/**
 * Created by Tomáš on 16.05.2017.
 */
public class Worker1DeciderTest {

    @Test
    public void basicTest() {
        Worker1Decider d = new Worker1Decider(Worker1Decision.ReturnCar);

        Assert.assertEquals(d.evaluate(), Worker1Decision.ReturnCar);

        d.addCondition(new Worker1Condition(new OfficeAgentValueProvider() {
            @Override
            public double getValue(OfficeAgent agent) {
                return 1;
            }

            @Override
            public String getName() {
                return "one";
            }
        }, Comparator.lessThan,
         new OfficeAgentValueProvider() {
            @Override
            public double getValue(OfficeAgent agent) {
                return 2;
            }

            @Override
            public String getName() {
                return "two";
            }
        }, Worker1Decision.TakeOrder));

        d.addCondition(new Worker1Condition(new OfficeAgentValueProvider() {

            @Override
            public double getValue(OfficeAgent agent) {
                return 1;
            }

            @Override
            public String getName() {
                return "one";
            }
        }, Comparator.lessThan, new OfficeAgentValueProvider() {
            @Override
            public double getValue(OfficeAgent agent) {
                return 0;
            }

            @Override
            public String getName() {
                return "zero";
            }
        }, Worker1Decision.ReturnCar));

        d.moveCondition(d.getConditions().get(1), Decider.Direction.Up);

        Assert.assertEquals(d.evaluate(), Worker1Decision.TakeOrder);
    }

}