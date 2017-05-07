package sk.epholl.dissim.util.subscribers;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class ResultManagerTest {

    final static ValueType<String> simpleString = new ValueType<String>(String.class);
    final static ValueType<Integer> simpleInt = new ValueType<Integer>(Integer.class);

    @Test
    public void addValue() throws Exception {
        ResultManager rm = new ResultManager();
        rm.addValue(simpleString, "testValue");
        rm.addValue(simpleString, "testValue");
        rm.addValue(simpleString, "testValue");
        rm.addValue(simpleInt, 10);

        rm.addSubscriber(simpleString, (Subscriber<String>) value -> {
            Assert.assertEquals(value, "testValue");
        });
        rm.swingFlush();
    }

    @Test(expected = ClassCastException.class)
    public void testWrongType() {
        ResultManager rm = new ResultManager();
        rm.addValue(simpleInt, "wrong");
    }

    @Test
    public void testRemoveSubscriber() {
        ResultManager rm = new ResultManager();
        rm.addValue(simpleString, "should not be shown");

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onValueEmitted(String value) {
                throw new AssertionError("Should not have gotten here");
            }
        };
        rm.addSubscriber(simpleString, subscriber );
        rm.removeSubscriber(subscriber);
        rm.swingFlush();
    }

}