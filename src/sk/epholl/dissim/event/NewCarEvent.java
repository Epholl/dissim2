package sk.epholl.dissim.event;

import sk.epholl.dissim.carshop.CarShopSimulationCore;
import sk.epholl.dissim.entity.Car;

/**
 * Created by Tomáš on 23.03.2017.
 */
public class NewCarEvent extends CarShopEvent {

    public static NewCarEvent newInstance(CarShopSimulationCore simulation) {
        final double arrivalTime = simulation.getSimulationTime() + simulation.getGenerators().getNextCustomerEnteranceDelay();
        Car newCar = new Car(arrivalTime);
        return new NewCarEvent(arrivalTime, simulation, newCar);
    }

    public NewCarEvent(double occurTime, CarShopSimulationCore simulation, Car car) {
        super(occurTime, simulation, car);
    }

    @Override
    public void onOccur() {
        simulation.handleNewCarArrived(car);
    }
}
