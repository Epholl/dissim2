package sk.epholl.dissim.sem3.entity.deciders;

import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.util.deciders.Comparator;
import sk.epholl.dissim.util.deciders.Condition;
import sk.epholl.dissim.util.deciders.ValueProvider;

/**
 * Created by Tomáš on 16.05.2017.
 */
public class Worker1Condition extends Condition<Worker1Decision> {

    private OfficeAgent agent;

    public Worker1Condition(OfficeAgentValueProvider arg1Provider, Comparator comparator, OfficeAgentValueProvider arg2Provider, Worker1Decision returnValue) {
        super(arg1Provider, comparator, arg2Provider, returnValue);
    }

    public void setAgent(OfficeAgent agent) {
        this.agent = agent;
        ((OfficeAgentValueProvider)this.arg1Provider).setAgent(agent);
        ((OfficeAgentValueProvider)this.arg2Provider).setAgent(agent);
    }
}
