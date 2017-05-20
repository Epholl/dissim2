package sk.epholl.dissim.sem3.entity.deciders;

import org.junit.Assert;
import org.junit.Test;
import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.util.deciders.Comparator;

/**
 * Created by Tomáš on 16.05.2017.
 */
public class Worker1DeciderTest {

    @Test
    public void basicTest() {
        Worker1Decider d = new Worker1Decider(Worker1Decision.ReturnCar);

        Assert.assertEquals(d.evaluate(), Worker1Decision.ReturnCar);

        d.addCondition(new Worker1Condition(new OfficeAgentValueProvider("one") {
            @Override
            public double getValue(OfficeAgent agent) {
                return 1;
            }
        }, Comparator.lessThan,
         new OfficeAgentValueProvider("two") {
            @Override
            public double getValue(OfficeAgent agent) {
                return 2;
            }
        }, Worker1Decision.TakeOrder));

        d.addCondition(new Worker1Condition(new OfficeAgentValueProvider("one") {
            @Override
            public double getValue(OfficeAgent agent) {
                return 1;
            }
        }, Comparator.lessThan, new OfficeAgentValueProvider("zero") {
            @Override
            public double getValue(OfficeAgent agent) {
                return 0;
            }
        }, Worker1Decision.ReturnCar));


        Assert.assertEquals(d.evaluate(), Worker1Decision.TakeOrder);
    }

}