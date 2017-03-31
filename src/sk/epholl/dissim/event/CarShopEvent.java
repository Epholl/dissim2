package sk.epholl.dissim.event;

import sk.epholl.dissim.carshop.CarShopSimulationCore;
import sk.epholl.dissim.core.Event;
import sk.epholl.dissim.entity.Car;

/**
 * Created by Tomáš on 23.03.2017.
 */
public abstract class CarShopEvent extends Event {

    protected final CarShopSimulationCore simulation;

    protected final Car car;

    public CarShopEvent(final double occurTime, final CarShopSimulationCore simulation, final Car car) {
        super(occurTime);
        this.simulation = simulation;
        this.car = car;
    }

    @Override
    public String toString() {
        return CarShopSimulationCore.TimeUtils.formatDayTime(occurTime) + ": " + getClass().getSimpleName();
    }
}
