package sk.epholl.dissim.carshop;

import sk.epholl.dissim.core.Simulation;
import sk.epholl.dissim.core.SimulationCore;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tomáš on 18.03.2017.
 */
public class CarShopSimulation extends Simulation<CarShopSimulationParameters, CarShopSimulation.Results, CarShopSimulation.State> {

    public class Results {

    }

    public class State {
        
    }

    public CarShopSimulation(CarShopSimulationParameters simulationParameters, Simulation.SimulationListener<Results, State> simulationListener) {
        super(simulationParameters, simulationListener);
    }

    @Override
    protected SimulationCore<CarShopSimulationParameters, Results, State> initSimulationCore() {
        return null;
    }
}
