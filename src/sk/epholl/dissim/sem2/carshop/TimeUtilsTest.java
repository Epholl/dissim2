package sk.epholl.dissim.sem2.carshop;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tomáš on 24.03.2017.
 */
public class TimeUtilsTest {

    @Test
    public void testWorkingHours() {
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeOfDay(TimeUnit.HOURS.toSeconds(3)), TimeUnit.HOURS.toSeconds(3), 0.0000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeOfDay(TimeUnit.HOURS.toSeconds(9)), TimeUnit.HOURS.toSeconds(1), 0.0000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeOfDay(TimeUnit.HOURS.toSeconds(24)), TimeUnit.HOURS.toSeconds(0), 0.0000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeOfDay(TimeUnit.HOURS.toSeconds(28)), TimeUnit.HOURS.toSeconds(4), 0.0000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeOfDay(TimeUnit.HOURS.toSeconds(169)), TimeUnit.HOURS.toSeconds(1), 0.0000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeOfDay(TimeUnit.HOURS.toSeconds(166)), TimeUnit.HOURS.toSeconds(6), 0.0000001d);
    }

    @Test
    public void testGetDay() {
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getDay(0d), 1);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getDay(TimeUnit.HOURS.toSeconds(5)), 1);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getDay(TimeUnit.HOURS.toSeconds(12)), 2);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getDay(TimeUnit.HOURS.toSeconds(23)), 3);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getDay(TimeUnit.HOURS.toSeconds(24)), 4);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getDay(TimeUnit.HOURS.toSeconds(167)), 21);
    }

    @Test
    public void testTimeRemainingToday() {
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeRemainingToday(0d), TimeUnit.HOURS.toSeconds(8), 0.000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeRemainingToday(TimeUnit.HOURS.toSeconds(8)), TimeUnit.HOURS.toSeconds(8), 0.000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeRemainingToday(TimeUnit.HOURS.toSeconds(12)), TimeUnit.HOURS.toSeconds(4), 0.000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeRemainingToday(TimeUnit.HOURS.toSeconds(21)), TimeUnit.HOURS.toSeconds(3), 0.000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getTimeRemainingToday(TimeUnit.HOURS.toSeconds(25)), TimeUnit.HOURS.toSeconds(7), 0.000001d);
    }

    @Test
    public void testGetNextDayStart() {
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getNextDayStart(TimeUnit.HOURS.toSeconds(0)), TimeUnit.HOURS.toSeconds(8), 0.000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getNextDayStart(TimeUnit.HOURS.toSeconds(5)), TimeUnit.HOURS.toSeconds(8), 0.000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getNextDayStart(TimeUnit.HOURS.toSeconds(8)), TimeUnit.HOURS.toSeconds(16), 0.000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getNextDayStart(TimeUnit.HOURS.toSeconds(15)), TimeUnit.HOURS.toSeconds(16), 0.000001d);
        Assert.assertEquals(CarShopSimulationCore.TimeUtils.getNextDayStart(TimeUnit.HOURS.toSeconds(16)), TimeUnit.HOURS.toSeconds(24), 0.000001d);
    }

    @Test
    public void testWillEventFinishToday() {
        Assert.assertTrue(CarShopSimulationCore.TimeUtils.willEventFinishToday(0d, 3600d));
        Assert.assertFalse(CarShopSimulationCore.TimeUtils.willEventFinishToday(
                TimeUnit.HOURS.toSeconds(1), TimeUnit.HOURS.toSeconds(8)));
        Assert.assertTrue(CarShopSimulationCore.TimeUtils.willEventFinishToday(0d, TimeUnit.HOURS.toSeconds(8)));
    }

    @Test
    public void testDayTimeFormatting() {
        System.out.println(CarShopSimulationCore.TimeUtils.formatDayTime(0d));
        System.out.println(CarShopSimulationCore.TimeUtils.formatDayTime(1800d));
        System.out.println(CarShopSimulationCore.TimeUtils.formatDayTime(3600d));
        System.out.println(CarShopSimulationCore.TimeUtils.formatDayTime(86500d));
    }

    @Test
    public void testTimeDurationFormatting() {
        System.out.println(CarShopSimulationCore.TimeUtils.formatTimePeriod(3600));
        System.out.println(CarShopSimulationCore.TimeUtils.formatTimePeriod(TimeUnit.HOURS.toSeconds(24) - 1));
        System.out.println(CarShopSimulationCore.TimeUtils.formatTimePeriod(TimeUnit.HOURS.toSeconds(32)));
        System.out.println(CarShopSimulationCore.TimeUtils.formatTimePeriod(123456));
    }
}