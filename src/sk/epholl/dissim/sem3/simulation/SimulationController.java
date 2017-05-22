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

    private LinkedList<SimulationParameters> batchQueue;

    public SimulationController() {
        this.simulation = new MySimulation();
        parameters = new SimulationParameters();
        resultManager = new ResultManager();
        simulation.setResultManager(resultManager);
        afterSimulationCallbacks = new LinkedList<>();
        batchQueue = new LinkedList<>();

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
        if (hasBatchesRemaining()) {
            setParameters(batchQueue.poll());
        }
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
            if (hasBatchesRemaining()) {
                resetSimulation();
                startSimulation();
            }
        });

        simulation.onRefreshUI(simulation1 -> {
            MySimulation sim = (MySimulation) simulation1;
            sim.agentGuiUpdate();
            sim.execIfContinous(() -> {
                sim.getResultManager().addValue(Rst.SIMULATION_TIME, sim.currentTime());
            });
            sim.getResultManager().swingFlush();
        });

        simulation.onReplicationDidFinish(simulation1 -> {
            MySimulation sim = (MySimulation) simulation1;
            sim.agentReplicationFinishedUpdate();
            sim.getResultManager().swingFlush();
        });
    }

    public void addSimulationEndedCallback(Runnable r) {
        afterSimulationCallbacks.add(r);
    }

    public MySimulation getSimulation() {
        return simulation;
    }

    public void setParameters(SimulationParameters parameters) {
        this.parameters = parameters;
    }

    public void batchCurrent() {
        batchQueue.add(parameters.copy());
    }

    public boolean hasBatchesRemaining() {
        return !batchQueue.isEmpty();
    }

    public int getBatchesRemaingSize() {
        return batchQueue.size();
    }

    public void resetBatches() {
        batchQueue.clear();
    }
}
