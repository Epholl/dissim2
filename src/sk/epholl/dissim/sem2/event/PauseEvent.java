package sk.epholl.dissim.sem2.event;

import sk.epholl.dissim.sem2.core.Event;
import sk.epholl.dissim.sem2.core.SimulationCore;

/**
 * Created by Tomáš on 14.04.2016.
 */
public class PauseEvent extends Event {

    private SimulationCore simCore;

    boolean active = false;

    public PauseEvent(double occurTime, SimulationCore simCore) {
        super(occurTime);
        this.simCore = simCore;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public synchronized void onOccur() {
        if (active) {
            try {
                double timeMultiplier = simCore.getSimulationSpeed();
                simCore.publishContinuousStateResults();
                Thread.sleep(10);
                occurTime = simCore.getSimulationTime() + timeMultiplier;
                simCore.addEvent(this);
            } catch (InterruptedException e) {

            }
        }
    }
}
