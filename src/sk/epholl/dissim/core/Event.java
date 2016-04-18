package sk.epholl.dissim.core;

/**
 * Created by Tomáš on 26.03.2016.
 */
public abstract class Event implements Comparable<Event> {

    public static final float EPSILON = 0.000001f;

    private long simulationEnrollId;
    protected double enrollTime;
    protected double occurTime;

    public Event(double occurTime) {
        this.occurTime = occurTime;
    }

    public abstract void onOccur();

    public double getEnrollTime() {
        return enrollTime;
    }

    public double getOccurTime() {
        return occurTime;
    }

    public void setEnrollTime(double enrollTime) {
        this.enrollTime = enrollTime;
    }

    public void setSimulationEnrollId(long newId) {
        simulationEnrollId = newId;
    }

    public long getSimulationEnrollId() {
        return simulationEnrollId;
    }

    @Override
    public int compareTo(Event o) {
        if (Math.abs(occurTime - o.occurTime) < EPSILON) {
            return simulationEnrollId < o.simulationEnrollId ? -1 : 1;
        } else {
            return occurTime < o.occurTime? -1 : 1;
        }
    }
}
