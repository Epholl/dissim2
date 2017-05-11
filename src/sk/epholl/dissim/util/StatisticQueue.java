package sk.epholl.dissim.util;

import sk.epholl.dissim.sem2.core.SimulationCore;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by kocurik on 18.03.2017.
 */
public class StatisticQueue<T> extends LinkedList<Pair<Double, T>> {

    public final HashMap<Integer, Double> queueLengths = new HashMap<>();
    public final SimulationCore simulation;

    public long processedElements;
    public double waitingSum;

    public double lastQueueUpdate;

    public StatisticQueue(SimulationCore simulation) {
        this.simulation = simulation;
        lastQueueUpdate = simulation.getSimulationTime();
    }

    public void enqueue(T element) {
        if (contains(element)) {
            throw new IllegalStateException("Tried enqueuing an element already present");
        }

        final double simTime = simulation.getSimulationTime();
        updateStatistics();
        addLast(new Pair(simTime, element));
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Tried dequeuing from an empty queue");
        }

        updateStatistics();
        final Pair<Double, T> polled = pollFirst();
        final double duration = simulation.getSimulationTime() - polled.first;
        processedElements++;
        waitingSum += duration;
        return polled.second;
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

    public double getAverageQueueWait() {
        if (processedElements == 0) {
            return 0d;
        }
        return waitingSum / processedElements;
    }

    public void updateStatistics() {
        final double duration = simulation.getSimulationTime() - lastQueueUpdate;
        final int index = size();

        if (duration > 0.000001) {
            queueLengths.compute(index, (integer, aDouble) -> aDouble == null? duration : aDouble + duration);
            lastQueueUpdate = simulation.getSimulationTime();
        }
    }

    public void softClear() {
        super.clear();
    }

    @Override
    public void clear() {
        super.clear();
        lastQueueUpdate = 0d;
        queueLengths.clear();
        processedElements = 0;
        waitingSum = 0d;
    }
}
