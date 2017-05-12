package sk.epholl.dissim.sem3.simulation;

import sk.epholl.dissim.sem3.Generators;
import sk.epholl.dissim.sem3.entity.RoadModel;
import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;

/**
 * Created by Tomáš on 10.05.2017.
 */
public class SimulationParameters {

    enum Worker1Strategy {
        DEFAULT;
    }

    private int repliacationCount;

    private double replicationDurationSeconds;

    private int warmupPercentage;

    private int number1Workers;

    private int number2Workers;

    private Worker1Strategy worker1Strategy;

    private double clientIncomeIncreasePercent;

    private Generators generators = new Generators(1.0);

    private RoadModel roadModel = new RoadModel();

    public int getRepliacationCount() {
        return repliacationCount;
    }

    public void setRepliacationCount(int repliacationCount) {
        this.repliacationCount = repliacationCount;
    }

    public int getWarmupPercentage() {
        return warmupPercentage;
    }

    public void setWarmupPercentage(int warmupPercentage) {
        this.warmupPercentage = warmupPercentage;
    }

    public int getNumber1Workers() {
        return number1Workers;
    }

    public int getWorker1TotalPrice() {
        return number1Workers * Const.worker1Price;
    }

    public int getWorker2TotalPrice() {
        return number2Workers * Const.worker2Price;
    }

    public void setNumber1Workers(int number1Workers) {
        this.number1Workers = number1Workers;
    }

    public int getNumber2Workers() {
        return number2Workers;
    }

    public double getAdvertisementTotalPrice() {
        return clientIncomeIncreasePercent * Const.onePercentCustomerIncreaseCost;
    }

    public double getTotalPrice() {
        return getWorker1TotalPrice() + getWorker2TotalPrice() + getAdvertisementTotalPrice();
    }

    public double getClientIncomeCoeficient() {
        return 1.0 + (clientIncomeIncreasePercent/100);
    }

    public void setNumber2Workers(int number2Workers) {
        this.number2Workers = number2Workers;
    }

    public Generators getGenerators() {
        return generators;
    }

    public Worker1Strategy getWorker1Strategy() {
        return worker1Strategy;
    }

    public void setWorker1Strategy(Worker1Strategy worker1Strategy) {
        this.worker1Strategy = worker1Strategy;
    }

    public double getClientIncomeIncreasePercent() {
        return clientIncomeIncreasePercent;
    }

    public void setAdvertismentIncrease(double clientIncomeIncreasePercent) {
        this.clientIncomeIncreasePercent = clientIncomeIncreasePercent;
        generators.recreateCustomerEntryGeneratorWithMultiplier(getClientIncomeCoeficient());
    }

    public double getReplicationDurationSeconds() {
        return replicationDurationSeconds;
    }

    public void setReplicationDurationSeconds(double replicationDurationSeconds) {
        this.replicationDurationSeconds = replicationDurationSeconds;
    }

    public void setReplicationDurationDays(double replicationDurationDays) {
        this.replicationDurationSeconds = replicationDurationDays * 8 * 60 * 60;
    }

    public RoadModel getRoadModel() {
        return roadModel;
    }

    public void setRoadModel(RoadModel roadModel) {
        this.roadModel = roadModel;
    }
}
