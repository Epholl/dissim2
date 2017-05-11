package sk.epholl.dissim.sem3.simulation;

import OSPABA.Simulation;
import sk.epholl.dissim.sem3.Generators;
import sk.epholl.dissim.util.subscribers.ResultManager;

import java.util.function.Consumer;

/**
 * Created by Tomáš on 07.05.2017.
 */
public class SimulationController {

    private MySimulation simulation;

    private SimulationParameters parameters;

    private ResultManager resultManager;

    public SimulationController() {
        this.simulation = new MySimulation();
        parameters = new SimulationParameters();
        resultManager = new ResultManager();
        simulation.setResultManager(resultManager);

        simulation.onRefreshUI(simulation1 -> {
            simulation.getResultManager().swingFlush();
        });

        setUpSimulationAfterIterationUpdate();
    }

    public ResultManager getResultManager() {
        return resultManager;
    }

    public SimulationParameters getParameters() {
        return parameters;
    }

    public void startSimulation() {
        simulation.setParameters(parameters);
        final Thread simThread = new Thread(() -> {
            simulation.simulate(10000, 100000d);
        });
        simThread.start();
    }

    public void pauseSimulation() {
        simulation.pauseSimulation();
    }

    public void resumeSimulation() {
        simulation.resumeSimulation();
    }

    public void setContinuous(final double simTimeIn100Ms) {
        simulation.setSimSpeed(simTimeIn100Ms, 0.1);
    }

    public void setMaxSpeed() {
        simulation.setMaxSimSpeed();
    }

    public void setUpSimulationAfterIterationUpdate() {
        simulation.onReplicationDidFinish(simulation -> {
            resultManager.swingFlush();
        });
    }
}
