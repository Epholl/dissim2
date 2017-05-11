package sk.epholl.dissim.sem2.carshop;

import sk.epholl.dissim.sem2.core.SimulationParameters;

/**
 * Created by Tomáš on 17.03.2017.
 */
public class CarShopSimulationParameters extends SimulationParameters {

    public final int type1WorkerCount;
    public final int type2WorkerCount;

    public final int durationHours;

    public CarShopSimulationParameters(long replicationCount,
                                       int type1WorkerCount,
                                       int type2workerCount,
                                       int durationHours) {
        super(replicationCount, 10d);
        this.type1WorkerCount = type1WorkerCount;
        this.type2WorkerCount = type2workerCount;

        this.durationHours = durationHours;
    }

    public CarShopSimulationParameters(long replicationCount,
                                       double warmupReplicationPercent,
                                       int type1WorkerCount,
                                       int type2workerCount,
                                       int durationHours) {
        super(replicationCount, warmupReplicationPercent);
        this.type1WorkerCount = type1WorkerCount;
        this.type2WorkerCount = type2workerCount;

        this.durationHours = durationHours;
    }
}
