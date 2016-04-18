package sk.epholl.dissim.entity;

import sk.epholl.dissim.generator.ContinuousEvenRandom;
import sk.epholl.dissim.generator.RandomGenerator;
import sk.epholl.dissim.util.Utils;

/**
 * Created by Tomáš on 10.04.2016.
 */
public class Vehicle implements Cloneable {

    public static final String STATE_IDLE = "idle";
    public static final String STATE_TRAVELLING = "travelling ";
    public static final String STATE_BREAKDOWN = "broken ";
    public static final String STATE_WAITING = "waiting ";
    public static final String STATE_LOADING = "loading";
    public static final String STATE_UNLOADING = "unloading";

    private double capacity;
    private double speed;
    private double breakdownProbability;
    private double repairTime;

    private boolean isBreakdown = false;
    private double waitingStartedTime;

    private int finishedLoadsCount = 0;
    private String state = STATE_IDLE;

    private RandomGenerator<Double> breakdownGenerator;

    private double currentLoad;

    public Vehicle(double capacity, double speed, double breakdownProbability, double repairTime) {
        this.capacity = capacity;
        this.speed = speed;
        this.breakdownProbability = breakdownProbability;
        this.repairTime = repairTime;

        this.breakdownGenerator = new ContinuousEvenRandom(0, 1);
    }

    public Vehicle(double capacity, double speed, double breakdownProbability, double repairTime, boolean isBreakdown, String state, double currentLoad, int finishedLoadsCount) {
        this.capacity = capacity;
        this.speed = speed;
        this.breakdownProbability = breakdownProbability;
        this.repairTime = repairTime;

        this.isBreakdown = isBreakdown;
        this.state = state;
        this.currentLoad = currentLoad;

        this.finishedLoadsCount = finishedLoadsCount;
        this.breakdownGenerator = null;
    }

    public Vehicle clone() {
        return new Vehicle(capacity, speed, breakdownProbability, repairTime, isBreakdown, state, currentLoad, finishedLoadsCount);
    }

    public Vehicle newCopy() {
        return new Vehicle(capacity, speed, breakdownProbability, repairTime);
    }

    public double calculateTimeSpentRepairing() {
        if (calculateIsBreakdownNextRoute()) {
            isBreakdown = true;
            return Utils.minutesToSeconds(repairTime);
        } else {
            isBreakdown = false;
            return 0;
        }
    }

    public double getCapacity() {
        return capacity;
    }

    public double getSpeed() {
        return speed;
    }

    public double getBreakdownProbability() {
        return breakdownProbability;
    }

    public double getRepairTime() {
        return repairTime;
    }

    public double getCurrentLoad() {
        return currentLoad;
    }

    public String getState() {
        return state;
    }

    public boolean isBreakdown() {
        return isBreakdown;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double unload() {

        finishedLoadsCount++;
        double load = currentLoad;
        currentLoad = 0d;
        return load;
    }

    public void load(double currentLoad) {
        this.currentLoad = currentLoad;
    }

    private boolean calculateIsBreakdownNextRoute() {
        double value = breakdownGenerator.nextValue();
        return value < breakdownProbability;
    }

    public String getLoadInfo() {
        return currentLoad + "/" + capacity;
    }

    @Override
    public String toString() {
        return getLoadInfo() + ", state: " + getState();
    }

    public int getFinishedLoadsCount() {
        return finishedLoadsCount;
    }

    public void setWaitingStartedTime(double time) {
        waitingStartedTime = time;
    }

    public double getWaitingStartedTime() {
        return waitingStartedTime;
    }
}
