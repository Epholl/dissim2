package sk.epholl.dissim.sem2.event;

import sk.epholl.dissim.sem2.carshop.CarShopSimulationCore;
import sk.epholl.dissim.sem2.entity.Car;

/**
 * Created by Tomáš on 26.03.2017.
 */
public class TakeOrderEvent extends CarShopEvent {

    public static TakeOrderEvent newInstance(CarShopSimulationCore simulation, Car car) {
        double simTime = simulation.getSimulationTime();
        double orderTakingTime = simulation.getGenerators().getNextOrderTakingDuration();
        double carAcquiringTime = simulation.getGenerators().getNextCarEnrollingDuration();
        double carTransferTime = simulation.getGenerators().getNextCarTransferDuration();
        double occurTime = simTime + orderTakingTime + carAcquiringTime + carTransferTime;

        car.setOrderTakingDuration(orderTakingTime);
        car.setCarAcquisitionDuration(carAcquiringTime);
        car.setTransferToGarageDuration(carTransferTime);

        car.setTimeOrderStartedTaking(simTime);
        car.setTimeOrderTaken(simTime + orderTakingTime);
        car.setTimeCarAcquired(simTime + orderTakingTime + carAcquiringTime);
        car.setTimeCarTransferredToGarage(simTime + orderTakingTime + carAcquiringTime + carTransferTime);

        TakeOrderEvent event = new TakeOrderEvent(occurTime, simulation, car);
        return event;
    }

    public TakeOrderEvent(double occurTime, CarShopSimulationCore simulation, Car car) {
        super(occurTime, simulation, car);
    }

    @Override
    public void onOccur() {
        simulation.handleOrderTaken(car);
    }
}
