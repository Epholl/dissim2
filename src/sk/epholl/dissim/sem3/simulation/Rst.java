package sk.epholl.dissim.sem3.simulation;

import sk.epholl.dissim.util.StatisticCounter;
import sk.epholl.dissim.util.subscribers.ValueType;

import java.util.List;

/**
 * Created by Tomáš on 08.05.2017.
 */
public class Rst {

    public static final ValueType<Double> SIMULATION_TIME = new ValueType<>(Double.class);
    public static final ValueType<Integer> REPLICATION_COUNT = new ValueType<>(Integer.class);

    public static final ValueType<String> CONSOLE_LOG = new ValueType<>(String.class);

    public static final ValueType<Integer> ENTERED_CUSTOMERS = new ValueType<>(Integer.class);
    public static final ValueType<Integer> FINISHED_CUSTOMERS = new ValueType<>(Integer.class);
    public static final ValueType<Integer> REFUSED_CUSTOMERS = new ValueType<>(Integer.class);
    public static final ValueType<Integer> SHOP_CLOSED_CUSTOMERS = new ValueType<>(Integer.class);

    public static final ValueType<VehicleUpdate> VEHICLE_STATE = new ValueType<>(VehicleUpdate.class);
    public static final ValueType<ParkingUpdate> PARKING_STATE = new ValueType<>(ParkingUpdate.class);
    public static final ValueType<WorkerUpdate> WORKER1_STATE = new ValueType<>(WorkerUpdate.class);
    public static final ValueType<WorkerUpdate> WORKER2_STATE = new ValueType<>(WorkerUpdate.class);

    public static final ValueType<Double> PROFIT = new ValueType<>(Double.class);
    public static final ValueType<Double> BALANCE = new ValueType<>(Double.class);

    public static final ResultType[] resultTypes() {
            return new ResultType[] {
                    new ResultType("All customers", R_ALL_CUSTOMERS),
                    new ResultType("Finished customers", R_FINISHED_CUSTOMERS),
                    new ResultType("Refused customers", R_REFUSED_CUSTOMERS),
                    new ResultType("Finished customers ratio", R_FINISHED_RATIO),
                    new ResultType("Refused customers ratio", R_REFUSED_RATIO),

            };
    }

    public static final ValueType<Result> R_ALL_CUSTOMERS = new ValueType<>(Result.class);
    public static final ValueType<Result> R_FINISHED_CUSTOMERS = new ValueType<>(Result.class);
    public static final ValueType<Result> R_REFUSED_CUSTOMERS = new ValueType<>(Result.class);
    public static final ValueType<Result> R_FINISHED_RATIO = new ValueType<>(Result.class);
    public static final ValueType<Result> R_REFUSED_RATIO = new ValueType<>(Result.class);


    public static class VehicleUpdate {
        public double simTime;
        public List<VehicleState> states;
    }

    public static class VehicleState {
        public String name;
        public String position;
        public String state;
        public String worker;
        public double timeStateStarted;
        public double timeStateEnds;
    }

    public static class ParkingUpdate {
        public List<ParkingSpotState> spots;
    }

    public static class ParkingSpotState {
        public String name;
        public String state;
        public String vehicle;
    }

    public static class WorkerUpdate {
        public List<WorkerState> states;
    }

    public static class WorkerState {
        public String name;
        public String state;
        public String vehicle;
    }

    public static class ResultType {
        public String resultName;
        public ValueType<Result> valueType;

        public ResultType(String name, ValueType<Result> valueType) {
            this.resultName = name;
            this.valueType = valueType;
        }
    }

    public static class Result {
        public long replication;
        public StatisticCounter result;

        public Result(long replication, StatisticCounter result) {
            this.replication = replication;
            this.result = result.copy();
        }
    }
}
