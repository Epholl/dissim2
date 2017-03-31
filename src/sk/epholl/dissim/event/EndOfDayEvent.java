package sk.epholl.dissim.event;

import sk.epholl.dissim.carshop.CarShopSimulationCore;
import sk.epholl.dissim.entity.Car;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tomáš on 27.03.2017.
 */
public class EndOfDayEvent extends CarShopEvent {

    public static EndOfDayEvent newInstance(CarShopSimulationCore simultion) {
        double occurTime = simultion.getSimulationTime() + CarShopSimulationCore.TimeUtils.workDayDuration;
        return new EndOfDayEvent(occurTime, simultion, null);
    }

    public EndOfDayEvent(double occurTime, CarShopSimulationCore simulation, Car car) {
        super(occurTime, simulation, car);
    }

    @Override
    public void onOccur() {
        simulation.handleEndOfDay();
    }
}
