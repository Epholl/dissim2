package sk.epholl.dissim.sem3.simulation;

import sk.epholl.dissim.util.subscribers.ResultManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomáš on 07.05.2017.
 */
public class SimulationController {

    private MySimulation simulation;

    private SimulationParameters parameters;

    private ResultManager resultManager;

    private List<Runnable> afterSimulationCallbacks;

    public SimulationController() {
        this.simulation = new MySimulation();
        parameters = new SimulationParameters();
        resultManager = new ResultManager();
        simulation.setResultManager(resultManager);
        afterSimulationCallbacks = new LinkedList<>();

        setUpSimulationCallbacks();
    }

    public void resetSimulation() {
        simulation.stopSimulation();
        simulation = new MySimulation();
        simulation.setResultManager(resultManager);

        setUpSimulationCallbacks();
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
            simulation.simulate(getParameters().getRepliacationCount(), parameters.getReplicationDurationSeconds());
        });
        simThread.start();
    }

    public void pauseSimulation() {
        simulation.pauseSimulation();
    }

    public void resumeSimulation() {
        simulation.resumeSimulation();
    }

    public void setSimulationSpeed(final double simTimeSecondsIn1Second) {
        simulation.setSimSpeed(simTimeSecondsIn1Second/10, 0.1);
    }

    public void setMaxSpeed() {
        simulation.setMaxSimSpeed();
    }

    public void setUpSimulationCallbacks() {

        simulation.onSimulationDidFinish(simulation1 -> {
            for (Runnable r: afterSimulationCallbacks) {
                r.run();
            }
        });

        simulation.onRefreshUI(simulation1 -> {
            simulation.execIfContinous(() -> {
                simulation.getResultManager().addValue(Rst.CONSOLE_LOG, "Sim time " + simulation.currentTime());
            });
            simulation.getResultManager().swingFlush();
        });

        simulation.onReplicationDidFinish(simulation -> {
            resultManager.swingFlush();
        });
    }

    public void addSimulationEndedCallback(Runnable r) {
        afterSimulationCallbacks.add(r);
    }
}
