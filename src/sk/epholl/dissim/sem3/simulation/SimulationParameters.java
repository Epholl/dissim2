package sk.epholl.dissim.sem3.simulation;

import sk.epholl.dissim.sem3.Generators;
import sk.epholl.dissim.sem3.entity.RoadModel;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decider;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decision;

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

    private int type1WorkerCount;

    private int type2WorkerCount;

    private Worker1Strategy worker1Strategy;

    private double clientIncomeIncreasePercent;

    private Generators generators = new Generators(1.0);

    private RoadModel roadModel = new RoadModel();

    private Worker1Decider worker1Decider = new Worker1Decider(Worker1Decision.ReturnCar);

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

    public int getType1WorkerCount() {
        return type1WorkerCount;
    }

    public int getWorker1TotalPrice() {
        return type1WorkerCount * Const.worker1Price;
    }

    public int getWorker2TotalPrice() {
        return type2WorkerCount * Const.worker2Price;
    }

    public void setType1WorkerCount(int type1WorkerCount) {
        this.type1WorkerCount = type1WorkerCount;
    }

    public int getType2WorkerCount() {
        return type2WorkerCount;
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

    public void setType2WorkerCount(int type2WorkerCount) {
        this.type2WorkerCount = type2WorkerCount;
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

    public Worker1Decider getWorker1Decider() {
        return worker1Decider;
    }

    public void setWorker1Decider(Worker1Decider worker1Decider) {
        this.worker1Decider = worker1Decider;
    }
}
