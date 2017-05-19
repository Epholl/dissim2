package sk.epholl.dissim.sem3.entity.deciders;

import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.util.deciders.ValueProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tomáš on 16.05.2017.
 */
public abstract class OfficeAgentValueProvider implements ValueProvider {

    public static final OfficeAgentValueProvider[] values() {
        return new OfficeAgentValueProvider[] {
                lot1FreeSpace, lot2FreeSpace, constant,
        };
    }

    public static final OfficeAgentValueProvider newConstant(double value) {
        return new OfficeAgentValueProvider() {
            @Override
            public double getValue(OfficeAgent agent) {
                throw new UnsupportedOperationException("use newConstant(double) instead");
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

    public static final OfficeAgentValueProvider constant = new OfficeAgentValueProvider() {

            @Override
            public double getValue(OfficeAgent agent) {
                return 0.0;
            }

            @Override
            public String toString() {
                return "constant";
            }
        };

    public static final OfficeAgentValueProvider lot1FreeSpace = new OfficeAgentValueProvider() {
        @Override
        public double getValue(OfficeAgent agent) {
            return agent.getLot1FreeParkingSpots().getFreeUnits();
        }

        @Override
        public String toString() {
            return "lot 1 free spots";
        }
    };

    public static final OfficeAgentValueProvider lot2FreeSpace = new OfficeAgentValueProvider() {
        @Override
        public double getValue(OfficeAgent agent) {
            return agent.getLot2FreeParkingSpots().getFreeUnits();
        }

        @Override
        public String toString() {
            return "lot 2 free spots";
        }
    };

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
