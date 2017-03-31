package sk.epholl.dissim.event;

import sk.epholl.dissim.carshop.CarShopSimulationCore;
import sk.epholl.dissim.core.SimulationCore;
import sk.epholl.dissim.entity.Car;

/**
 * Created by Tomáš on 26.03.2017.
 */
public class RepairEvent extends CarShopEvent {

    public static RepairEvent newInstance(CarShopSimulationCore simulation, Car car) {
        final double simTime = simulation.getSimulationTime();
        final int numberOfRepairs = simulation.getGenerators().getNextAmountOfRepairs();
        double totalRepairDuration = 0d;
        for (int i = 0; i < numberOfRepairs; i++) {
            totalRepairDuration += simulation.getGenerators().getNextRepairDuration();
        }
        final double occurTime = simTime + totalRepairDuration;

        car.setTimeCarStartedRepairing(simTime);
        car.setRepairDuration(totalRepairDuration);
        car.setTimeCarRepaired(occurTime);

        return new RepairEvent(occurTime, simulation, car);
    }

    public RepairEvent(double occurTime, CarShopSimulationCore simulation, Car car) {
        super(occurTime, simulation, car);
    }

    @Override
    public void onOccur() {
        simulation.handleRepairFinished(car);
    }
}
