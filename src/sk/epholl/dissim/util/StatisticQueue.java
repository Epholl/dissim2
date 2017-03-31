package sk.epholl.dissim.util;

import sk.epholl.dissim.core.Simulation;
import sk.epholl.dissim.core.SimulationCore;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by kocurik on 18.03.2017.
 */
public class StatisticQueue<T> extends LinkedList<T> {

    private final HashMap<Integer, Double> queueLengths = new HashMap<>();
    private final SimulationCore simulation;

    private double lastQueueUpdate;

    public StatisticQueue(SimulationCore simulation) {
        this.simulation = simulation;
        lastQueueUpdate = simulation.getSimulationTime();
    }

    public void enqueue(T element) {
        if (contains(element)) {
            throw new IllegalStateException("Tried enqueuing an element already present");
        }

        updateStatistics();
        addLast(element);
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Tried dequeuing from an empty queue");
        }

        updateStatistics();
        return pollFirst();
    }

    public double getAverageQueueLength() {
        updateStatistics();
        double totalTime = simulation.getSimulationTime();
        double sum = 0d;
        for (Integer index: queueLengths.keySet()) {
            sum += queueLengths.get(index) * index;
        }

        return sum / totalTime;
    }

    public void updateStatistics() {
        final double duration = simulation.getSimulationTime() - lastQueueUpdate;
        final int index = size();

        if (duration > 0.000001) {
            queueLengths.compute(index, (integer, aDouble) -> aDouble == null? duration : aDouble + duration);
            lastQueueUpdate = simulation.getSimulationTime();
        }
    }

    @Override
    public void clear() {
        super.clear();
        lastQueueUpdate = 0d;
        queueLengths.clear();
    }
}
