package sk.epholl.dissim.util;

/**
 * Created by Tomáš on 14.05.2017.
 */
public interface SimTimeProvider {
    double getSimulationTime();

    public static final SimTimeProvider MOCK = () -> 0;
}
