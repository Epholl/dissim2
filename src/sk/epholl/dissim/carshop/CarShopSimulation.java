package sk.epholl.dissim.carshop;

import sk.epholl.dissim.core.Simulation;
import sk.epholl.dissim.core.SimulationCore;
import sk.epholl.dissim.entity.Car;
import sk.epholl.dissim.util.StatisticCounter;

/**
 * Created by Tomáš on 18.03.2017.
 */
public class CarShopSimulation extends Simulation<CarShopSimulationParameters, CarShopSimulation.Result, CarShopSimulation.IterationResults, CarShopSimulation.State> {

    public static class IterationResults {
        public final long finishedCustomers;

        public final long refusedCustomers;
        public final double averageRefusedCustomers;

        public final long carsInWorkshop;

        public final double averageWaitForOrder;
        public final double averageTimeInSystem;
        public final double averageTimeFromAcquisitionToReturn;

        public final double averageEntryWait;
        public final double averageEntryQueue;

        public final double averageRepairWait;
        public final double averageRepairQueue;

        public final double averageReturnWait;
        public final double averageReturnQueue;

        public IterationResults(long finishedCustomers,
                                long refusedCustomers,
                                double averageRefusedCustomers,
                                long carsInWorkshop,
                                double averageWaitForOrder,
                                double averageTimeInSystem,
                                double averageTimeFromAcquisitionToReturn,
                                double averageEntryWait,
                                double averageEntryQueue,
                                double averageRepairWait,
                                double averageRepairQueue,
                                double averageReturnWait,
                                double averageReturnQueue) {
            this.finishedCustomers = finishedCustomers;
            this.refusedCustomers = refusedCustomers;
            this.averageRefusedCustomers = averageRefusedCustomers;
            this.carsInWorkshop = carsInWorkshop;
            this.averageWaitForOrder = averageWaitForOrder;
            this.averageTimeInSystem = averageTimeInSystem;
            this.averageTimeFromAcquisitionToReturn = averageTimeFromAcquisitionToReturn;
            this.averageEntryWait = averageEntryWait;
            this.averageEntryQueue = averageEntryQueue;
            this.averageRepairWait = averageRepairWait;
            this.averageRepairQueue = averageRepairQueue;
            this.averageReturnWait = averageReturnWait;
            this.averageReturnQueue = averageReturnQueue;
        }

        @Override
        public String toString() {
            return "finished customers: " + finishedCustomers + " \n "
                    + "refused customers: " + refusedCustomers + " \n "
                    + "average refused customers: " + averageRefusedCustomers + " \n "
                    + "average wait for customer " + averageWaitForOrder + " \n "
                    + "cars in workshop " + carsInWorkshop + " \n "
                    + "average time in system " + averageTimeInSystem + " \n "
                    + "average time from acquisition to return " + averageTimeFromAcquisitionToReturn + " \n "
                    + "average entry wait " + averageEntryWait + " \n "
                    + "average entry queue " + averageEntryQueue + " \n "
                    + "average repair wait " + averageRepairWait + " \n "
                    + "average repair queue " + averageRepairQueue + " \n "
                    + "average return wait " + averageReturnWait + " \n "
                    + "average return queue " + averageReturnQueue;
        }
    }

    public static class State {

        public final IterationResults results;
        public final double simulationTime;
        public final String[] cars;
        public final Car.State[] states;

        public final int freeType1workers;
        public final int freeType2workers;

        public State(IterationResults results, double simulationTime, String[] cars, Car.State[] states, int freeType1workers, int freeType2workers) {
            this.results = results;
            this.simulationTime = simulationTime;
            this.cars = cars;
            this.states = states;
            this.freeType1workers = freeType1workers;
            this.freeType2workers = freeType2workers;
        }
    }

    public static class Result {
        public final long currentReplication;

        public final StatisticCounter finishedCustomers;

        public final StatisticCounter refusedCustomers;
        public final StatisticCounter averageRefusedCustomers;

        public final StatisticCounter carsInWorshop;

        public final StatisticCounter averageWaitForOrder;
        public final StatisticCounter averageTimeInSystem;
        public final StatisticCounter averageTimeFromAcquisitionToReturn;

        public final StatisticCounter averageEntryWait;
        public final StatisticCounter averageEntryQueue;

        public final StatisticCounter averageRepairWait;
        public final StatisticCounter averageRepairQueue;

        public final StatisticCounter averageReturnWait;
        public final StatisticCounter averageReturnQueue;

        public Result(long currentReplication, StatisticCounter finishedCustomers,
                      StatisticCounter refusedCustomers,
                      StatisticCounter averageRefusedCustomers,
                      StatisticCounter carsInWorshop, StatisticCounter averageWaitForOrder,
                      StatisticCounter averageTimeInSystem,
                      StatisticCounter averageTimeFromAcquisitionToReturn,
                      StatisticCounter averageEntryWait,
                      StatisticCounter averageEntryQueue,
                      StatisticCounter averageRepairWait,
                      StatisticCounter averageRepairQueue,
                      StatisticCounter averageReturnWait,
                      StatisticCounter averageReturnQueue) {
            this.currentReplication = currentReplication;
            this.finishedCustomers = finishedCustomers;
            this.refusedCustomers = refusedCustomers;
            this.averageRefusedCustomers = averageRefusedCustomers;
            this.carsInWorshop = carsInWorshop;
            this.averageWaitForOrder = averageWaitForOrder;
            this.averageTimeInSystem = averageTimeInSystem;
            this.averageTimeFromAcquisitionToReturn = averageTimeFromAcquisitionToReturn;
            this.averageEntryWait = averageEntryWait;
            this.averageEntryQueue = averageEntryQueue;
            this.averageRepairWait = averageRepairWait;
            this.averageRepairQueue = averageRepairQueue;
            this.averageReturnWait = averageReturnWait;
            this.averageReturnQueue = averageReturnQueue;
        }

        @Override
        public String toString() {
            return "finished customers: " + finishedCustomers + " \n "
                    + "refused customers: " + refusedCustomers + " \n "
                    + "average refused customers: " + averageRefusedCustomers + " \n "
                    + "average wait for customer " + averageWaitForOrder + " \n "
                    + "cars in workshop " + carsInWorshop + " \n "
                    + "average time in system " + averageTimeInSystem + " \n "
                    + "average time from acquisition to return " + averageTimeFromAcquisitionToReturn + " \n "
                    + "average entry wait " + averageEntryWait + " \n "
                    + "average entry queue " + averageEntryQueue + " \n "
                    + "average repair wait " + averageRepairWait + " \n "
                    + "average repair queue " + averageRepairQueue + " \n "
                    + "average return wait " + averageReturnWait + " \n "
                    + "average return queue " + averageReturnQueue;
        }
    }

    private final StatisticCounter finishedCustomers = new StatisticCounter();

    private final StatisticCounter refusedCustomers = new StatisticCounter();
    private final StatisticCounter averageRefusedCustomers = new StatisticCounter();

    private final StatisticCounter carsInWorkshop = new StatisticCounter();

    private final StatisticCounter averageWaitForOrder = new StatisticCounter();
    private final StatisticCounter averageTimeInSystem = new StatisticCounter();
    private final StatisticCounter averageTimeFromAcquisitionToReturn = new StatisticCounter();

    private final StatisticCounter averageEntryWait = new StatisticCounter();
    private final StatisticCounter averageEntryQueue = new StatisticCounter();

    private final StatisticCounter averageRepairWait = new StatisticCounter();
    private final StatisticCounter averageRepairQueue = new StatisticCounter();

    private final StatisticCounter averageReturnWait = new StatisticCounter();
    private final StatisticCounter averageReturnQueue = new StatisticCounter();

    public CarShopSimulation(CarShopSimulationParameters simulationParameters, Simulation.SimulationListener<Result, State> simulationListener) {
        super(simulationParameters, simulationListener);
    }

    @Override
    protected SimulationCore<CarShopSimulationParameters, IterationResults, State> initSimulationCore() {
        return new CarShopSimulationCore(simulationParameters);
    }

    @Override
    protected void replicationFinished(IterationResults result) {
        finishedCustomers.addValue(result.finishedCustomers);

        refusedCustomers.addValue(result.refusedCustomers);
        averageRefusedCustomers.addValue(result.averageRefusedCustomers);

        carsInWorkshop.addValue(result.carsInWorkshop);

        averageWaitForOrder.addValue(result.averageWaitForOrder);
        averageTimeInSystem.addValue(result.averageTimeInSystem);
        averageTimeFromAcquisitionToReturn.addValue(result.averageTimeFromAcquisitionToReturn);

        averageEntryWait.addValue(result.averageEntryWait);
        averageEntryQueue.addValue(result.averageEntryQueue);

        averageRepairWait.addValue(result.averageRepairWait);
        averageRepairQueue.addValue(result.averageRepairQueue);

        averageReturnWait.addValue(result.averageReturnWait);
        averageReturnQueue.addValue(result.averageReturnQueue);
    }

    @Override
    protected Result getResult() {
         return new Result(
                 currentReplication,
                 finishedCustomers.copy(),
                refusedCustomers.copy(),
                averageRefusedCustomers.copy(),
                 carsInWorkshop.copy(),
                 averageWaitForOrder.copy(),
                averageTimeInSystem.copy(),
                averageTimeFromAcquisitionToReturn.copy(),
                averageEntryWait.copy(),
                averageEntryQueue.copy(),
                averageRepairWait.copy(),
                averageRepairQueue.copy(),
                averageReturnWait.copy(),
                averageReturnQueue.copy());
    }
}
