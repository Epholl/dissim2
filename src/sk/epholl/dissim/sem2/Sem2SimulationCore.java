package sk.epholl.dissim.sem2;

import sk.epholl.dissim.core.SimulationCore;
import sk.epholl.dissim.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomáš on 14.04.2016.
 */
public class Sem2SimulationCore extends SimulationCore<Sem2Results> {

    private Loader loader;
    private Unloader unloader;
    private NarrowRoad abRoad;
    private BumpyRoad bcRoad;
    private NarrowRoad caRoad;

    private Sem2SimulationParameters params;
    private List<Vehicle> vehicles = new ArrayList<>();

    public Sem2SimulationCore(Sem2SimulationParameters params) {
        this.params = params;
        loader = new Loader(this, "loader", params.loaderSpeed, params.cargoAmount);
        unloader = new Unloader(this, "unloader", params.unloaderSpeed, params.cargoAmount);
        abRoad = new NarrowRoad(this, "A->B", params.roadAbLength);
        bcRoad = new BumpyRoad(this, "B->C", params.roadBcLength);
        caRoad = new NarrowRoad(this, "C->A", params.roadCaLength);

        loader.setOnFinishedListener(vehicle -> {
            abRoad.accept(vehicle);
        });
        abRoad.setOnFinishedListener(vehicle -> {
            unloader.accept(vehicle);
        });
        unloader.setOnFinishedListener(vehicle -> {
            bcRoad.accept(vehicle);
        });
        bcRoad.setOnFinishedListener(vehicle -> {
            caRoad.accept(vehicle);
        });
        caRoad.setOnFinishedListener(vehicle -> {
            loader.accept(vehicle);
        });
    }

    public void setVehicleVariant(int variantIndex) {
        vehicles.clear();
        for (int i: params.vehicleCombinations[variantIndex]) {
            vehicles.add(params.availableVehicles[i].newCopy());
        }
    }

    @Override
    public void start() {
        for (Vehicle vehicle: vehicles) {
            loader.accept(vehicle);
        }
        super.start();
    }

    @Override
    protected boolean simulationEndCondition() {
        return unloader.allCargoTransferred();
    }

    @Override
    public Sem2Results getResults() {

        List<Vehicle> vehicleClones = new ArrayList<>();
        for (Vehicle v: vehicles) {
            vehicleClones.add(v.clone());
        }
        Loader.State loaderState = loader.getState();
        Unloader.State unloaderState = unloader.getState();

        return new Sem2Results(getSimulationTime(), vehicleClones, loaderState, unloaderState);
    }


}
