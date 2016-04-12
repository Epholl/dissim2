package sk.epholl.dissim.entity;

import sk.epholl.dissim.generator.ContinuousEvenRandom;
import sk.epholl.dissim.generator.RandomGenerator;

/**
 * Created by Tomáš on 10.04.2016.
 */
public class Vehicle implements Cloneable {

    enum State {
        WAITING,
        LOADING,
        TRAVELLING,
        UNLOADING
    }

    private double capacity;
    private double speed;
    private double breakdownProbability;
    private double repairTime;

    private RandomGenerator<Double> breakdownGenerator;

    private double currentLoad;

    public Vehicle(double capacity, double speed, double breakdownProbability, double repairTime) {
        this.capacity = capacity;
        this.speed = speed;
        this.breakdownProbability = breakdownProbability;
        this.repairTime = repairTime;

        this.breakdownGenerator = new ContinuousEvenRandom(0, 1);
    }

    public double calculateTimeSpentRepairing() {
        if (calculateIsBreakdownNextRoute()) {
            return repairTime;
        } else {
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

    public void setCurrentLoad(double currentLoad) {
        this.currentLoad = currentLoad;
    }

    private boolean calculateIsBreakdownNextRoute() {
        double value = breakdownGenerator.nextValue();
        return value < breakdownProbability;
    }
}
