package sk.epholl.dissim.sem2.carshop;

import sk.epholl.dissim.sem2.core.Event;
import sk.epholl.dissim.sem2.core.SimulationCore;
import sk.epholl.dissim.sem2.entity.Car;
import sk.epholl.dissim.sem2.event.*;
import sk.epholl.dissim.util.StatisticCounter;
import sk.epholl.dissim.util.StatisticQueue;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Tomáš on 24.03.2017.
 */
public class CarShopSimulationCore extends SimulationCore<CarShopSimulationParameters, CarShopSimulation.IterationResults, CarShopSimulation.State> {

    private CarShopSimulationGenerators generators;

    private int type1WorkerCount;
    private int type2WorkerCount;

    private int type1FreeWorkers;
    private int type2FreeWorkers;

    private HashSet<Car> cars;

    private StatisticQueue<Car> entryQueue;
    private StatisticQueue<Car> workshopQueue;
    private StatisticQueue<Car> returnQueue;

    private StatisticCounter refusedCustomersCounter;
    private StatisticCounter timeInSystemCounter;
    private StatisticCounter timeFromAcquisitionToReturnCounter;
    private StatisticCounter timeInSystem2Counter;

    public CarShopSimulationCore(CarShopSimulationParameters simulationParameters) {
        super(simulationParameters);

        generators = new CarShopSimulationGenerators();
        type1WorkerCount = simulationParameters.type1WorkerCount;
        type2WorkerCount = simulationParameters.type2WorkerCount;

        cars = new HashSet<>();

        entryQueue = new StatisticQueue<>(this);
        workshopQueue = new StatisticQueue<>(this);
        returnQueue = new StatisticQueue<>(this);

        refusedCustomersCounter = new StatisticCounter();
        timeInSystemCounter = new StatisticCounter();
        timeFromAcquisitionToReturnCounter = new StatisticCounter();
        timeInSystem2Counter = new StatisticCounter();
    }

    @Override
    protected void singleIteration() {
        type1FreeWorkers = type1WorkerCount;
        type2FreeWorkers = type2WorkerCount;

        cars.clear();

        entryQueue.clear();
        workshopQueue.clear();
        returnQueue.clear();

        refusedCustomersCounter.clear();
        timeInSystemCounter.clear();
        timeFromAcquisitionToReturnCounter.clear();
        timeInSystem2Counter.clear();

        addEvent(NewCarEvent.newInstance(this));
        addEvent(EndOfDayEvent.newInstance(this));
        start();
    }

    public CarShopSimulationGenerators getGenerators() {
        return generators;
    }

    public void handleNewCarArrived(Car car) {
        addEvent(NewCarEvent.newInstance(this));
        cars.add(car);
        entryQueue.enqueue(car);
        checkAcceptNewCar();
    }

    public void handleOrderTaken(Car car) {
        type1FreeWorkers++;
        workshopQueue.enqueue(car);
        checkStartRepairingCar();
        checkWorkForType1Worker();
    }

    public void handleRepairFinished(Car car) {
        type2FreeWorkers++;
        returnQueue.enqueue(car);
        checkStartRepairingCar();
        checkReturnRepairedCar();
    }

    public void handleCarReturned(Car car) {
        type1FreeWorkers++;
        checkWorkForType1Worker();

        timeInSystemCounter.addValue(car.getTimeSpentInSystem());
        timeFromAcquisitionToReturnCounter.addValue(car.getTimeFromAcquisitionEndToReturn());

        timeInSystem2Counter.addValue(car.getTimeSpentInSystemWithOvertimes());

        cars.remove(car);

        /*System.out.println(car
                + ", entered "
                + TimeUtils.formatDayTime(car.getEntryTime())
                + ", left "
                + TimeUtils.formatDayTime(car.getExitTime())
                + ", total: "
                + TimeUtils.formatTimePeriod(car.getTimeSpentInSystem()));*/
    }

    public void handleEndOfDay() {
        int refusedCustomersCount = entryQueue.size();
        entryQueue.updateStatistics();
        workshopQueue.updateStatistics();
        returnQueue.updateStatistics();
        refusedCustomersCounter.addValue(refusedCustomersCount);
        /*System.out.println("Day " + (TimeUtils.getDay(getSimulationTime()) - 1) + " ends. Refused customers: " + refusedCustomersCount);
        System.out.println("Waiting for repairs " + workshopQueue.size() + ", waiting to be returned: " + returnQueue.size());
        System.out.println("Free workers type 1 / 2: " + type1FreeWorkers + " / " + type2FreeWorkers);*/
        entryQueue.softClear();
        addEvent(EndOfDayEvent.newInstance(this));
    }

    private void checkWorkForType1Worker() {
        checkReturnRepairedCar();
        checkAcceptNewCar();
    }

    private void checkAcceptNewCar() {
        if (type1FreeWorkers > 0 && !entryQueue.isEmpty()) {
            type1FreeWorkers--;
            final Car car = entryQueue.dequeue();
            TakeOrderEvent event = TakeOrderEvent.newInstance(this ,car);
            addEvent(event);
        }
    }

    private void checkStartRepairingCar() {
        if (type2FreeWorkers > 0 && !workshopQueue.isEmpty()) {
            type2FreeWorkers--;
            final Car car = workshopQueue.dequeue();
            Event event = RepairEvent.newInstance(this, car);
            addEvent(event);
        }
    }

    private void checkReturnRepairedCar() {
        if (type1FreeWorkers > 0 && !returnQueue.isEmpty()) {
            type1FreeWorkers--;
            final Car car = returnQueue.dequeue();
            Event event = ReturnCarEvent.newInstance(this, car);
            addEvent(event);
        }
    }

    @Override
    protected boolean simulationEndCondition() {
        return getSimulationTime() > TimeUnit.HOURS.toSeconds(simulationParameters.durationHours);
    }

    @Override
    public CarShopSimulation.IterationResults getResults() {
        CarShopSimulation.IterationResults results = new CarShopSimulation.IterationResults(
                timeInSystemCounter.getCount(),
                (long) refusedCustomersCounter.getSum(),
                refusedCustomersCounter.getMean(),
                cars.size(),
                entryQueue.getAverageQueueWait(),
                timeInSystemCounter.getMean(),
                timeFromAcquisitionToReturnCounter.getMean(),
                entryQueue.getAverageQueueWait(),
                entryQueue.getAverageQueueLength(),
                workshopQueue.getAverageQueueWait(),
                workshopQueue.getAverageQueueLength(),
                returnQueue.getAverageQueueWait(),
                returnQueue.getAverageQueueLength());
        return results;
    }

    @Override
    public CarShopSimulation.State getState() {
        CarShopSimulation.State state = new CarShopSimulation.State(
                getResults(),
                getSimulationTime(),
                cars.stream().sorted((o1, o2) -> (int) (o1.getId() - o2.getId())).map(Car::toString).collect(Collectors.toList()).toArray(new String[cars.size()]),
                cars.stream().sorted((o1, o2) -> (int) (o1.getId() - o2.getId())).map(Car::getState).collect(Collectors.toList()).toArray(new Car.State[cars.size()]),
                type1FreeWorkers,
                type2FreeWorkers);
        return state;
    }

    public static final class TimeUtils {
        public static final double workDayDuration = TimeUnit.HOURS.toSeconds(8);

        public static double getTimeOfDay(final double simulationTime) {
            return simulationTime % workDayDuration;
        }

        public static int getDay(final double simulationTime) {
            return (int)(simulationTime / workDayDuration) + 1;
        }

        public static double getNextDayStart(final double simulationTime) {
            return getDay(simulationTime) * workDayDuration;
        }

        public static double getTimeRemainingToday(final double simulationTime) {
            return workDayDuration - getTimeOfDay(simulationTime);
        }

        public static boolean willEventFinishToday(final double simulationTime, final double eventDuration) {
            final double remainingTimeToday = getTimeRemainingToday(simulationTime);
            return remainingTimeToday >= eventDuration;
        }

        public static double getEventFinishTimeAtomicEvent(final double simulationTime, final double eventDuration) {
            if (willEventFinishToday(simulationTime, eventDuration)) {
                return simulationTime + eventDuration;
            } else {
                return getNextDayStart(simulationTime);
            }
        }

        public static String formatDayTime(double simulationTime) {
            StringBuilder builder = new StringBuilder();
            builder.append("Day ")
                    .append(getDay(simulationTime))
                    .append(", ");
            final long hours = 7 + TimeUnit.SECONDS.toHours( (int) simulationTime) % 8;
            final long minutes = TimeUnit.SECONDS.toMinutes( (int) simulationTime) % 60;
            final long seconds = (int) simulationTime % 60;
            builder.append(String.format("%02d", hours))
                    .append(":")
                    .append(String.format("%02d", minutes))
                    .append(":")
                    .append(String.format("%02d", seconds));
            return builder.toString();
        }

        public static String formatTimePeriod(double time) {
            StringBuilder builder = new StringBuilder();

            final long hours = TimeUnit.SECONDS.toHours( (int) time);
            final long minutes = TimeUnit.SECONDS.toMinutes( (int) time) % 60;
            final long seconds = (int) time % 60;
            builder.append(String.format("%02d", hours))
                    .append(":")
                    .append(String.format("%02d", minutes))
                    .append(":")
                    .append(String.format("%02d", seconds));
            return builder.toString();
        }
    }
}
