package sk.epholl.dissim.sem2;

import sk.epholl.dissim.entity.Loader;
import sk.epholl.dissim.entity.Unloader;
import sk.epholl.dissim.entity.Vehicle;

import java.util.List;

/**
 * Created by Tomáš on 14.04.2016.
 */
public class Sem2Results {

    public double simTime;

    public Loader.State loader;
    public Unloader.State unloader;

    public List<Vehicle> vehicles;

    public Sem2Results(double simTime, List<Vehicle> vehicles, Loader.State loader, Unloader.State unloader) {
        this.simTime = simTime;
        this.loader = loader;
        this.unloader = unloader;
        this.vehicles = vehicles;
    }
}
