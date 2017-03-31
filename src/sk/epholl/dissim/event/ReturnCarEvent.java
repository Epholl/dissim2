package sk.epholl.dissim.event;

import sk.epholl.dissim.carshop.CarShopSimulationCore;
import sk.epholl.dissim.entity.Car;

/**
 * Created by Tomáš on 26.03.2017.
 */
public class ReturnCarEvent extends CarShopEvent {

    public static ReturnCarEvent newInstance(CarShopSimulationCore simulation, Car car) {
        double simTime = simulation.getSimulationTime();

        double carTransferDuration = simulation.getGenerators().getNextCarTransferDuration();
        double returnTime = simulation.getGenerators().getNextCarReturningTime();
        double occurTime = simTime + carTransferDuration + returnTime;

        car.setTimeCarStartedTransferringFromGarage(simTime);
        car.setTransferFromGarageDuration(carTransferDuration);
        car.setReturnToCustomerDuration(returnTime);
        car.setTimeCarTransferredFromGarage(simTime + carTransferDuration);
        car.setTimeCarReturnedToCustomer(occurTime);

        ReturnCarEvent event = new ReturnCarEvent(occurTime, simulation, car);
        return event;
    }

    public ReturnCarEvent(double occurTime, CarShopSimulationCore simulation, Car car) {
        super(occurTime, simulation, car);
    }

    @Override
    public void onOccur() {
        simulation.handleCarReturned(car);
    }
}
