package sk.epholl.dissim.sem2.core;

/**
 * Created by Tomáš on 25.02.2017.
 */
public class SimulationParameters {

    public static final long DEFAULT_WARMUP_REPLICATION_COUNT = 1000;
    public static final long DEFAULT_GUI_UPDATE_INTERVAL = 1000;

    private final long replicationCount;
    private long warmupReplicationCount; // the amount of replications that will not yield intermediate resultTypes

    private long guiUpdateInterval;

    private SimulationParameters(final long replicationCount) {
        this.replicationCount = replicationCount;
        this.guiUpdateInterval = DEFAULT_GUI_UPDATE_INTERVAL;
    }

    public SimulationParameters(final long replicationCount, final long warmupReplicationCount) {
        this(replicationCount);
        setWarmupReplicationCount(warmupReplicationCount);
    }

    public SimulationParameters(final long replicationCount, final double warmupReplicationPercentage) {
        this(replicationCount);
        setWarmupReplicationPercentage(warmupReplicationPercentage);
    }

    public final long getReplicationCount() {
        return replicationCount;
    }

    public final long getWarmupReplicationCount() {
        return warmupReplicationCount;
    }

    public long getGuiUpdateInterval() {
        return guiUpdateInterval;
    }

    public final void setWarmupReplicationCount(final long warmupReplicationCount) {
        this.warmupReplicationCount = Math.min(warmupReplicationCount, replicationCount);
    }

    public final void setWarmupReplicationPercentage(final double warmupReplicationPercentage) {
        if (0d < warmupReplicationPercentage || warmupReplicationPercentage < 100) {
            this.warmupReplicationCount = (long) (replicationCount * (warmupReplicationPercentage / 100));
        }
    }

    public void setGuiUpdateInterval(long guiUpdateInterval) {
        this.guiUpdateInterval = guiUpdateInterval;
    }
}
