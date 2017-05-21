package sk.epholl.dissim.sem3.entity.deciders;

import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.util.TimeUtils;
import sk.epholl.dissim.util.deciders.ValueProvider;

/**
 * Created by Tomáš on 16.05.2017.
 */
public abstract class OfficeAgentValueProvider implements ValueProvider {

    public static final OfficeAgentValueProvider[] values() {
        return new OfficeAgentValueProvider[] {
                lot1FreeSpace, lot2FreeSpace, worktimeLeftToday, nextCustomerWaitingTime, customersWaitingForOrderCount, constant
        };
    }

    public static final OfficeAgentValueProvider newConstant(double value) {
        return new OfficeAgentValueProvider(null) {
            @Override
            public double getValue(OfficeAgent agent) {
                return value;
            }

            @Override
            public String toString() {
                if(value == (long) value)
                    return String.format("%d",(long)value);
                else
                    return String.format("%s",value);
            }
        };
    }

    public static final OfficeAgentValueProvider constant = new OfficeAgentValueProvider("constant") {

            @Override
            public double getValue(OfficeAgent agent) {
                throw new UnsupportedOperationException("use new(Constant(double) instead");
            }
        };

    public static final OfficeAgentValueProvider lot1FreeSpace = new OfficeAgentValueProvider("lot 1 free spots") {
        @Override
        public double getValue(OfficeAgent agent) {
            return agent.getLot1FreeParkingSpots().getFreeUnits();
        }
    };

    public static final OfficeAgentValueProvider lot2FreeSpace = new OfficeAgentValueProvider("lot 2 free spots") {
        @Override
        public double getValue(OfficeAgent agent) {
            return agent.getLot2FreeParkingSpots().getFreeUnits();
        }
    };

    public static final OfficeAgentValueProvider worktimeLeftToday = new OfficeAgentValueProvider("minutes till day ends") {
        @Override
        public double getValue(OfficeAgent agent) {
            return TimeUtils.getTimeRemainingToday(agent.mySim().currentTime())/60;
        }
    };

    public static final OfficeAgentValueProvider nextCustomerWaitingTime = new OfficeAgentValueProvider("minutes in queue for next order") {
        @Override
        public double getValue(OfficeAgent agent) {
            return agent.getTimeNextCustomerWaitingForOrder()/60;
        }
    };

    public static final OfficeAgentValueProvider customersWaitingForOrderCount = new OfficeAgentValueProvider("customers waiting to order") {
        @Override
        public double getValue(OfficeAgent agent) {
            return agent.getCustomersWaitingForOrderCount();
        }
    };

    private OfficeAgent agent;

    private String name;

    protected OfficeAgentValueProvider(String name) {
        this.name = name;
    }

    @Override
    public double getValue() {
        return getValue(agent);
    }

    public abstract double getValue(OfficeAgent agent);

    public void setAgent(OfficeAgent agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return name;
    }
}
