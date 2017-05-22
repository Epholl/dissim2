package sk.epholl.dissim.sem3.entity.deciders;

import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.util.deciders.Condition;
import sk.epholl.dissim.util.deciders.Decider;

import java.util.LinkedList;

/**
 * Created by Tomáš on 16.05.2017.
 */
public class Worker1Decider extends Decider<Worker1Decision> {

    private OfficeAgent agent;

    public Worker1Decider(Worker1Decision defaultValue) {
        super(defaultValue);
    }

    public void setAgent(OfficeAgent agent) {
        this.agent = agent;
        for (Condition condition: conditions) {
            if (condition instanceof Worker1Condition) {
                ((Worker1Condition)condition).setAgent(agent);
            }
        }
    }

    public void addCondition(Worker1Condition condition) {
        super.addCondition(condition);
        condition.setAgent(agent);
    }

    public OfficeAgent getAgent() {
        return agent;
    }

    public Worker1Decider copy() {
        Worker1Decider copy = new Worker1Decider(defaultValue);
        copy.conditions = new LinkedList<>();
        copy.conditions.addAll(this.conditions);
        return copy;
    }

}
