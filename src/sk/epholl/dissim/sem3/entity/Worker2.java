package sk.epholl.dissim.sem3.entity;

import sk.epholl.dissim.sem3.simulation.MySimulation;
import sk.epholl.dissim.util.StatisticStateCounter;

/**
 * Created by Tomáš on 14.05.2017.
 */
public class Worker2 {

    public enum State {
        Idle, Repairing, WaitingToReturnCar
    }

    private MySimulation sim;

    private StatisticStateCounter<Worker1.State> stateCounter;

    private Vehicle vehicle;

    private final int id;

    public Worker2(final int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Worker2 " + id;
    }
}
