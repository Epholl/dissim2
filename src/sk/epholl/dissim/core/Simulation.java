package sk.epholl.dissim.core;

import java.util.List;

/**
 * Created by Tomáš on 09.03.2016.
 */
public interface Simulation extends Runnable {

    interface SimulationListener {
        void onStarted();
        void onFinished(List<Integer> results, double averageValue);
        void onGuiUpdate(int replicationCount, double averageValue);
    }

    void setReplicationCount(int replicationCount);
    void setSimulationListener(SimulationListener listener);
    void setGuiUpdateIterationInterval(int iterationCountBeforeGuiUpdate);

    @Override
    void run();
    void stop();
}
