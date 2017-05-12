package sk.epholl.dissim.sem3.entity;

import OSPABA.Entity;
import OSPABA.Simulation;
import sk.epholl.dissim.util.Pair;

import java.util.LinkedList;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class Road {

    private final Place start;
    private final Place finish;

    private final double travelDuration;
    private final double additionalDelay;

    private LinkedList<Pair<Integer, Integer>> graphicalPoints;
    private double totalGraphicalDistance = 0d;

    public Road(final Place start, final Place finish, final double travelDuration, final double additionalDelay) {
        this.start = start;
        this.finish = finish;
        this.travelDuration = travelDuration;
        this.additionalDelay = additionalDelay;
        graphicalPoints = new LinkedList<>();
    }

    public double getTotalDuration() {
        return travelDuration + additionalDelay;
    }

    @Override
    public String toString() {
        return "Road " + start + " to " + finish;
    }

    /*public void addRoadPoint(int x, int y) {
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
    }*/
}
