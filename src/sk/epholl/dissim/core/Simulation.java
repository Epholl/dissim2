package sk.epholl.dissim.core;

import javax.swing.*;

/**
 * Created by Tomáš on 09.03.2016.
 */
public abstract class Simulation<T extends SimulationParameters, S, U> implements Runnable {

    public interface SimulationListener<S, U> {
        void onStarted();
        void onFinished(S results);
        void onGuiUpdate(final double progress, S results);
        void onGuiSimulationStatus(U state);
    }

    protected final T simulationParameters;
    protected final SimulationListener<S, U> simulationListener;

    protected SimulationCore<T, S, U> simulationCore;

    protected long currentReplication;

    private volatile boolean running = false;
    private volatile boolean cancelled = false;

    public Simulation(T simulationParameters, SimulationListener<S, U> simulationListener) {
        this.simulationParameters = simulationParameters;
        this.simulationListener = simulationListener;
    }

    @Override
    public final void run() {
        simulationCore = initSimulationCore();
        running = true;
        cancelled = false;
        SwingUtilities.invokeLater(simulationListener::onStarted);

        currentReplication = 0;
        performIterations();

        if (!cancelled) {
            SwingUtilities.invokeLater(() -> simulationListener.onFinished(simulationCore.getResults()));
        } else {
            SwingUtilities.invokeLater(() -> simulationListener.onGuiUpdate(getProgress(), simulationCore.getResults()));
        }
        running = false;
    }

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
    }

    public void cancel() {
        running = false;
        cancelled = true;
    }

    private void performIterations() {
        for (; currentReplication < simulationParameters.getReplicationCount(); currentReplication++) {
            while (!running) {
                if (cancelled) {
                    return;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (isUpdateGuiReplication()) {
                final double progress = getProgress();
                final S results = simulationCore.getResults();

                SwingUtilities.invokeLater( () -> simulationListener.onGuiUpdate(progress, results));
            }
            simulationCore.singleIteration();
        }
    }

    protected abstract SimulationCore<T, S, U> initSimulationCore();

    private boolean isUpdateGuiReplication() {
        return  currentReplication > simulationParameters.getWarmupReplicationCount() //warmup finished
                || currentReplication % simulationParameters.getGuiUpdateInterval() == 0; // is gui update frame
    }

    private double getProgress() {
        return currentReplication / (double) simulationParameters.getReplicationCount();
    }
}
