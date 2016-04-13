package sk.epholl.dissim.sem2;

import sk.epholl.dissim.entity.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomáš on 13.04.2016.
 */
public class Sem2SimulationParameters {

    public List<Vehicle> vehicles = new ArrayList<>();

    public double cargoAmount = 5000; // t

    public double loaderSpeed = 180; // m^3 / min

    public double unloaderSpeed = 200; // m^3 / min

    public double roadAbLength = 45; // km

    public double roadBcLength = 15; // km

    public double roadCaLength = 35; // km

    public Vehicle[] availableVehicles = {
            new Vehicle(10, 60, 0.12, 80),
            new Vehicle(20, 50, 0.04, 50),
            new Vehicle(25, 45, 0.04, 100),
            new Vehicle(5, 70, 0.11, 44),
            new Vehicle(40, 30, 0.06, 170)
    };
    }
}
