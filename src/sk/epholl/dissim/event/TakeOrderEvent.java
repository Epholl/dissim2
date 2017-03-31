package sk.epholl.dissim.event;

import sk.epholl.dissim.carshop.CarShopSimulationCore;
import sk.epholl.dissim.entity.Car;

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
        car.setTimeOrderTaken(CarShopSimulationCore.TimeUtils.getEventFinishTimeAtomicEvent(simTime, orderTakingTime));
        car.setTimeCarAcquired(CarShopSimulationCore.TimeUtils.getEventFinishTimeAtomicEvent(simTime, orderTakingTime + carAcquiringTime));
        car.setTimeCarTransferredToGarage(CarShopSimulationCore.TimeUtils.getEventFinishTimeAtomicEvent(simTime, orderTakingTime + carAcquiringTime + carTransferTime));

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
