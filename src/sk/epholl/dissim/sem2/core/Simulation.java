package sk.epholl.dissim.sem2.core;

import javax.swing.*;

/**
 * Created by Tomáš on 09.03.2016.
 */
public abstract class Simulation<T extends SimulationParameters, R, S, U> implements Runnable {

    public interface SimulationListener<R, U> {
        void onStarted();
        void onFinished(R results);
        void onGuiUpdate(final double progress, R results);
        void onGuiSimulationStatus(U state);
    }

    protected final T simulationParameters;
    protected final SimulationListener<R, U> simulationListener;

    protected SimulationCore<T, S, U> simulationCore;

    protected long currentReplication;

    private volatile boolean running = false;
    private volatile boolean cancelled = false;

    public Simulation(T simulationParameters, SimulationListener<R, U> simulationListener) {
        this.simulationParameters = simulationParameters;
        this.simulationListener = simulationListener;
        simulationCore = initSimulationCore();
    }

    @Override
    public final void run() {

        simulationCore.addListener(new SimulationCore.ResultListener<S, U>() {
            @Override
            public void onReplicationFinished(S result) {
                replicationFinished(result);
            }

            @Override
            public void onContinuousUpdate(U state) {
                SwingUtilities.invokeLater(() -> simulationListener.onGuiSimulationStatus(state));
            }
        });
        running = true;
        cancelled = false;
        SwingUtilities.invokeLater(simulationListener::onStarted);

        currentReplication = 0;
        performIterations();

        if (!cancelled) {
            SwingUtilities.invokeLater(() -> simulationListener.onFinished(getResult()));
        } else {
            SwingUtilities.invokeLater(() -> simulationListener.onGuiUpdate(getProgress(), getResult()));
        }
        running = false;
    }

    public void pause() {
        running = false;
        simulationCore.pause();
    }

    public void resume() {
        running = true;
    }

    public void cancel() {
        running = false;
        cancelled = true;
    }

    public boolean isRunning() {
        return running;
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
                if (isWarmupFinished()) {
                    final R results = getResult();

                    SwingUtilities.invokeLater( () -> simulationListener.onGuiUpdate(progress, results));
                } else {
                    SwingUtilities.invokeLater( () -> simulationListener.onGuiUpdate(progress, null));
                }
            }
            simulationCore.singleIteration();
        }
    }

    public void setContinous(boolean continous) {
        simulationCore.setContinuousRun(continous);
    }

    public void setSpeed(double speed) {
        simulationCore.setContinuousSpeed(speed);
    }

    protected abstract SimulationCore<T, S, U> initSimulationCore();
    protected abstract void replicationFinished(S result);
    protected abstract R getResult();

    private boolean isUpdateGuiReplication() {
        return  currentReplication % simulationParameters.getGuiUpdateInterval() == 0; // is gui update frame
    }

    private boolean isWarmupFinished() {
        return currentReplication > simulationParameters.getWarmupReplicationCount(); //warmup finished
    }

    private double getProgress() {
        return currentReplication / (double) simulationParameters.getReplicationCount();
    }
}
