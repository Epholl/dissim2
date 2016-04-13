package sk.epholl.dissim;

import sk.epholl.dissim.core.SimulationCore;
import sk.epholl.dissim.entity.BumpyRoad;
import sk.epholl.dissim.entity.SimulationComponent;
import sk.epholl.dissim.entity.Vehicle;

/**
 * Created by Tomáš on 31.03.2016.
 */
public class Tests {

    public static void main(String[] arguments) {

        SimulationCore core = new SimulationCore() {
            @Override
            protected boolean simulationEndCondition() {
                return false;
            }
        };

        Vehicle v1 = new Vehicle(10, 60, 0.5, 100);
        Vehicle v2 = new Vehicle(10, 60, 0.5, 100);
        Vehicle v3 = new Vehicle(10, 60, 0.5, 100);
        BumpyRoad r1 = new BumpyRoad(core, 30);
        r1.setOnFinishedListener(new SimulationComponent.EventFinishedListener() {
            @Override
            public void onVehicleFinished(Vehicle vehicle) {
                System.out.println("Vehicle finished: " + core.getSimulationTime() + ", " + vehicle);
            }
        });
        r1.accept(v3);
        r1.accept(v1);
        r1.accept(v2);

        core.start();
    }
}
