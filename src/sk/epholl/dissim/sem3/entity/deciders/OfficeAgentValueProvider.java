package sk.epholl.dissim.sem3.entity.deciders;

import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.util.deciders.ValueProvider;

/**
 * Created by Tomáš on 16.05.2017.
 */
public abstract class OfficeAgentValueProvider implements ValueProvider {

    private OfficeAgent agent;

    @Override
    public double getValue() {
        return getValue(agent);
    }

    public abstract double getValue(OfficeAgent agent);

    public void setAgent(OfficeAgent agent) {
        this.agent = agent;
    }
}
