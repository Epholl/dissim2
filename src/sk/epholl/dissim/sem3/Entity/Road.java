package sk.epholl.dissim.sem3.Entity;

import OSPABA.Entity;
import OSPABA.Simulation;
import sk.epholl.dissim.util.Pair;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class Road extends Entity {

    private final double travelDuration;
    private final double additionalDelay;

    private LinkedList<Pair<Integer, Integer>> graphicalPoints;
    private double totalGraphicalDistance = 0d;

    public Road(Simulation mySim, final double travelDuration, final double additionalDelay) {
        super(mySim);
        this.travelDuration = travelDuration;
        this.additionalDelay = additionalDelay;
        graphicalPoints = new LinkedList<>();
    }

    public double getTotalDuration() {
        return travelDuration + additionalDelay;
    }

    public void addRoadPoint(int x, int y) {
        final Pair<Integer, Integer> last = graphicalPoints.peekLast();
        final Pair<Integer, Integer> added = new Pair<>(x, y);
        graphicalPoints.add(added);

        if (last != null) {
            totalGraphicalDistance += distance(last, added);
        }
    }

    private double distance(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
        final double xSquared = Math.pow(p1.first - p2.first, 2);
        final double ySquared = Math.pow(p1.second - p2.second, 2);
        return Math.sqrt(xSquared + ySquared);
    }
}