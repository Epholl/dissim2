package sk.epholl.dissim.sem2;

import sk.epholl.dissim.core.SimulationCore;

/**
 * Created by Tomáš on 14.04.2016.
 */
public class Sem2SimulationRunner implements Runnable {

    public interface Listener {
        void onFinalResults(StatisticCounter averageTime, StatisticCounter totalLoaderWait, StatisticCounter avgLoaderWait, StatisticCounter avgLoaderQueue, StatisticCounter totalUnloaderWait, StatisticCounter AvgUnloaderWait, StatisticCounter avgUnloaderQueue);
        void onPartialResults(int iterationCount, double averageTime);
        void onContinousUpdate(Sem2Results results);
    }

    private Sem2SimulationParameters parameters;
    private int vehicleVariant;

    private Sem2SimulationCore currentSimulation;

    private long iterationCount;

    private volatile boolean stopped;
    private boolean continuousRun = false;
    private double continousSpeed = 1d;

    private StatisticCounter timeCounter;
    private StatisticCounter loaderWaitedTime;
    private StatisticCounter loaderWait;
    private StatisticCounter loaderQueue;
    private StatisticCounter unloaderWaitedTime;
    private StatisticCounter unloaderWait;
    private StatisticCounter unloaderQueue;

    private Listener simListener;

    public Sem2SimulationRunner() {
    }

    public void setSimulation(Sem2SimulationParameters params, int variant) {
        this.parameters = params;
        this.vehicleVariant = variant;

        timeCounter = new StatisticCounter();
        loaderWaitedTime = new StatisticCounter();
        loaderWait = new StatisticCounter();
        loaderQueue = new StatisticCounter();
        unloaderWaitedTime = new StatisticCounter();
        unloaderWait = new StatisticCounter();
        unloaderQueue = new StatisticCounter();
    }

    public void setSimListener(Listener listener) {
        simListener = listener;
    }

    public void runAll() {
        int i = 0;
        for (; i < iterationCount; i++) {
            if (stopped) {
                break;
            }
            singleRun();
            if(i % 1000 == 0 && simListener != null) {
                simListener.onPartialResults(i, timeCounter.getMean());
            }
        }
        simListener.onPartialResults(i, timeCounter.getMean());
        if(simListener != null) {
            simListener.onFinalResults(timeCounter, loaderWaitedTime, loaderWait, loaderQueue, unloaderWaitedTime, unloaderWait, unloaderQueue);
        }
    }

    public void continousRun(double speed) {
        if (speed == 0) {
            speed = 1d;
        }
        currentSimulation = new Sem2SimulationCore(parameters);
        currentSimulation.setVehicleVariant(vehicleVariant);

        currentSimulation.setContinuousRun(true);
        currentSimulation.setContinuousSpeed(speed);

        currentSimulation.addListener(new SimulationCore.ResultListener<Sem2Results>() {
            @Override
            public void onReplicationFinished(Sem2Results result) {
                if (simListener != null) {
                    simListener.onContinousUpdate(result);
                }
            }

            @Override
            public void onContinuousUpdate(Sem2Results result) {
                if (simListener != null) {
                    simListener.onContinousUpdate(result);
                }
            }
        });
        currentSimulation.start();
    }

    public void setContinousRunSpeed(double speed) {
        continousSpeed = speed;
        if (currentSimulation != null) {
            currentSimulation.setContinuousSpeed(speed);
        }
    }

    public void singleRun() {
        currentSimulation = new Sem2SimulationCore(parameters);
        currentSimulation.setVehicleVariant(vehicleVariant);

        currentSimulation.addListener(new SimulationCore.ResultListener<Sem2Results>() {
            @Override
            public void onReplicationFinished(Sem2Results result) {
                timeCounter.addValue(currentSimulation.getSimulationTime());
                loaderWaitedTime.addValue(result.loader.waitingTimeSum);
                loaderWait.addValue(result.loader.averageWaitingTime);
                loaderQueue.addValue(result.loader.averageQueueLength);
                unloaderWaitedTime.addValue(result.unloader.waitingTimeSum);
                unloaderWait.addValue(result.unloader.averageWaitingTime);
                unloaderQueue.addValue(result.unloader.averageQueueLength);
            }

            @Override
            public void onContinuousUpdate(Sem2Results result) {

            }
        });
        currentSimulation.start();
    }

    public void setContinuousRun(boolean continous) {
        this.continuousRun = continous;
    }

    public void setIterationCount(long iterationCount) {
        this.iterationCount = iterationCount;
    }

    public void stop() {
        stopped = true;
        if (currentSimulation != null && continuousRun) {
            currentSimulation.stop();
        }
    }

    @Override
    public void run() {
        if (continuousRun) {
            continousRun(continousSpeed);
        } else {
            runAll();
        }
    }

    public static class StatisticCounter {
        public static final double CONFIDENCE_INTERVAL_90_PERCENT = 1.645;

        private double sum;
        private long count;

        private double sumSquared;

        public void addValue(double value) {
            sum += value;
            count++;
            sumSquared += value*value;
        }

        public double getMean() {
            return sum / count;
        }

        public double getVariance() {
            return Math.abs((sumSquared / count) - (getMean() * getMean()));
        }

        public double getDeviation() {
            return Math.sqrt(getVariance());
        }

        public double getLeftConfidenceInterval() {
            return getMean() - (CONFIDENCE_INTERVAL_90_PERCENT * getDeviation()) / Math.sqrt(count-1);
        }

        public double getRightConfidenceInterval() {
            return getMean() + (CONFIDENCE_INTERVAL_90_PERCENT * getDeviation()) / Math.sqrt(count-1);
        }
    }
}
