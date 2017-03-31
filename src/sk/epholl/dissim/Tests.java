package sk.epholl.dissim;

import sk.epholl.dissim.core.SimulationCore;
import sk.epholl.dissim.sem2.Sem2Results;
import sk.epholl.dissim.sem2.Sem2SimulationCore;
import sk.epholl.dissim.sem2.Sem2SimulationParameters;

/**
 * Created by Tomáš on 31.03.2016.
 */
public class Tests {

    public static void main(String[] arguments) {

        /*Sem2SimulationRunner.StatisticCounter counter = new Sem2SimulationRunner.StatisticCounter();

        counter.addValue(2);
        counter.addValue(3);
        counter.addValue(5);
        counter.addValue(6);
        counter.addValue(9);

        System.out.println(counter.getMean());
        System.out.println(counter.getVariance());
        System.out.println(counter.getDeviation());
        System.out.println(counter.getLeftConfidenceInterval());
        System.out.println(counter.getRightConfidenceInterval());

        Sem2SimulationRunner runner = new Sem2SimulationRunner();
        runner.setIterationCount(10000);
        runner.setSimulation(new Sem2SimulationParameters(), 3);
        runner.setSimListener(new Sem2SimulationRunner.Listener() {
            @Override
            public void onFinalResults(Sem2SimulationRunner.StatisticCounter averageTime, Sem2SimulationRunner.StatisticCounter avgLoaderWait, Sem2SimulationRunner.StatisticCounter avgLoaderQueue, Sem2SimulationRunner.StatisticCounter AvgUnloaderWait, Sem2SimulationRunner.StatisticCounter avgUnloaderQueue) {
                System.out.println("finished...");
                System.out.println(averageTime.getMean() + ", " + averageTime.getLeftConfidenceInterval() + ", " + averageTime.getRightConfidenceInterval());
            }

            @Override
            public void onContinousUpdate(Sem2Results results) {

            }
        });
        runner.run();*/

        Sem2SimulationParameters p = new Sem2SimulationParameters();
        Sem2SimulationCore core = new Sem2SimulationCore(p);
        core.setVehicleVariant(3);

        System.out.println("Done...");


        core.addListener(new SimulationCore.ResultListener<Sem2Results, Void>() {
            @Override
            public void onReplicationFinished(Sem2Results result) {
                //System.out.println("Finished." + result.loader.averageWaitingTime / 60);
            }

            @Override
            public void onContinuousUpdate(Void state) {

            }
        });
        core.start();

        /*SimulationCore core = new SimulationCore() {

            @Override
            protected boolean simulationEndCondition() {
                return getSimulationTime() > 100000;
            }
        };

        Vehicle v1 = new Vehicle(10, 60, 0, 100);
        BumpyRoad r1 = new BumpyRoad(core, 30);
        BumpyRoad r2 = new BumpyRoad(core, 30);
        Loader l = new Loader(core, 60, 105);
        Unloader u = new Unloader(core, 60, 105);
        r1.setOnFinishedListener(new SimulationComponent.EventFinishedListener() {
            @Override
            public void onVehicleFinished(Vehicle vehicle) {
                System.out.println(core.getSimulationTime() + ", Travel 1 finished, ");
                l.accept(vehicle);
            }
        });
        l.setOnFinishedListener(vehicle -> {
            System.out.println(core.getSimulationTime() + ", Vehicle loaded:   " + vehicle.getCurrentLoad());
            r2.accept(vehicle);
        });
        r2.setOnFinishedListener(vehicle -> {
            System.out.println(core.getSimulationTime() + ", Travel 2 finished");
            u.accept(vehicle);
        });
        u.setOnFinishedListener(vehicle -> {
            System.out.println(core.getSimulationTime() + ", Vehicle unloaded, total: " + u.getCurrentCargoAmount());
            r1.accept(vehicle);
        });
        l.accept(v1);
        core.start();*/
    }
}
