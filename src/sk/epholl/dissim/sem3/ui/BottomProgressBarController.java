package sk.epholl.dissim.sem3.ui;

import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.sem3.simulation.SimulationController;
import sk.epholl.dissim.util.TimeUtils;
import sk.epholl.dissim.util.subscribers.Subscriber;

import javax.swing.*;

/**
 * Created by Tomáš on 12.05.2017.
 */
public class BottomProgressBarController {

    private JProgressBar progressBar;

    private SimulationController simulationController;

    private boolean isContinous = false;

    private int currentReplication;
    private double currentTime;

    private final Subscriber<Integer> currentReplicationSubscriber = value -> {
        currentReplication = value+1;
        refresh();
    };

    private final Subscriber<Double> currentSimTimeSubscriber = value -> {
        currentTime = value;
        refresh();
    };

    public BottomProgressBarController(final JProgressBar progressBar, final SimulationController simulationController) {
        this.progressBar = progressBar;
        this.simulationController = simulationController;
        simulationController.getResultManager().addSubscriber(Rst.REPLICATION_COUNT, currentReplicationSubscriber);
        simulationController.getResultManager().addSubscriber(Rst.SIMULATION_TIME, currentSimTimeSubscriber);
    }

    public void setContinous(final boolean isContinous) {
        this.isContinous = isContinous;
        refresh();
    }

    public void reset() {
        currentReplication = 0;
        currentTime = 0;
        refresh();
    }

    private void refresh() {
        if (isContinous) {
            String text = "Replication " + currentReplication + ", Time: " + TimeUtils.formatDayTime(currentTime);
            progressBar.setString(text);
            double value = currentTime / simulationController.getParameters().getReplicationDurationSeconds();
            int convertedValue = (int) (value*1000);
            progressBar.setValue(convertedValue);
        } else {
            String text = "Replication " + currentReplication;
            progressBar.setString(text);
            double value = ((double)currentReplication) / simulationController.getParameters().getRepliacationCount();
            int convertedValue = (int) (value*1000);
            progressBar.setValue(convertedValue);
        }
    }
}
