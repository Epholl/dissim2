package sk.epholl.dissim.util.subscribers;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class ResultManagerTest {

    final static ResultValueType<String> simpleString = new ResultValueType<String>(String.class);
    final static ResultValueType<Integer> simpleInt = new ResultValueType<Integer>(Integer.class);

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

}