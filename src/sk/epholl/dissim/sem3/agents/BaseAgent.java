package sk.epholl.dissim.sem3.agents;

import OSPABA.Agent;
import OSPABA.Simulation;
import sk.epholl.dissim.sem3.simulation.MySimulation;
import sk.epholl.dissim.sem3.simulation.SimulationParameters;
import sk.epholl.dissim.util.subscribers.ResultManager;
import sk.epholl.dissim.util.subscribers.ValueType;

/**
 * Created by Tomáš on 08.05.2017.
 */
public class BaseAgent extends Agent {

    public BaseAgent(int id, MySimulation mySim, Agent parent) {
        super(id, mySim, parent);
    }

    public MySimulation getSimulation() {
        return ((MySimulation)mySim());
    }

    public SimulationParameters getParams() {
        return getSimulation().getParameters();
    }

    public <T> void publishValue(final ValueType<T> type, final T value) {
        getSimulation().getResultManager().addValue(type, value);
    }

    public <T> void publishValueContinous(final ValueType<T> type, final T value) {
        getSimulation().execIfContinous( () -> {
            getSimulation().getResultManager().addValue(type, value);
        });
    }
}
