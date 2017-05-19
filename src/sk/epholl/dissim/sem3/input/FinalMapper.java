package sk.epholl.dissim.sem3.input;

import com.sun.istack.internal.Nullable;
import sk.epholl.dissim.sem3.entity.deciders.OfficeAgentValueProvider;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Condition;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decision;
import sk.epholl.dissim.sem3.simulation.SimulationParameters;
import sk.epholl.dissim.util.deciders.Comparator;

import java.io.*;
import java.util.ArrayList;

public abstract class FinalMapper {

    public static Reader getDefaultConfigFileReader() throws IOException {
        return new FileReader("sources/conf.txt");
    }

    private final ConditionMapper conditionMapper = new ConditionMapper();
    private final SimulationLoader simulationLoader = new SimulationLoader();

    public FinalMapper() {
        OfficeAgentValueProvider[] providers = OfficeAgentValueProvider.values();
        conditionMapper.addProvider("lot1FreeSpace", providers[0]);
        conditionMapper.addProvider("lot2FreeSpace", providers[1]);
        conditionMapper.addProvider("constant", providers[2]);

        conditionMapper.addComparator("<", Comparator.lessThan);
        conditionMapper.addComparator("<=", Comparator.lessThanOrEquals);
        conditionMapper.addComparator(">", Comparator.moreThan);
        conditionMapper.addComparator(">=", Comparator.moreThanOrEquals);
        conditionMapper.addComparator("==", Comparator.equals);
        conditionMapper.addComparator("!=", Comparator.not);

        conditionMapper.addSuccessReturnValue("ReturnCar", Worker1Decision.ReturnCar);
        conditionMapper.addSuccessReturnValue("TakeOrder", Worker1Decision.TakeOrder);
    }

    public void go(@Nullable Reader reader) throws IOException {
        if (reader == null) {
            reader = new InputStreamReader(System.in);
        }

        BufferedReader bufferedReader = new BufferedReader(reader);
        simulationLoader.load(bufferedReader);

        do {
            SimulationLoader.ConfigurableSimulationParameters configurableParams = simulationLoader.createConfigurableSimulationParameters();
            SimulationParameters params = createSimulationParameters(configurableParams);
            ArrayList<Worker1Condition> conditions = conditionMapper.createConditions(configurableParams.conditionDefs);
            onReplication(params, conditions);
        } while (simulationLoader.nextDataSet());

        reader.close();
    }

    protected abstract void onReplication(SimulationParameters params, ArrayList<Worker1Condition> conditions);

    private SimulationParameters createSimulationParameters(SimulationLoader.ConfigurableSimulationParameters configurableParams) {
        SimulationParameters params = new SimulationParameters();
        params.setRepliacationCount(configurableParams.replicationCount);
        params.setType1WorkerCount(configurableParams.type1WorkerCount);
        params.setType2WorkerCount(configurableParams.type2WorkerCount);
        params.setAdvertismentIncrease(configurableParams.clientIncomeIncreasePercent);
        return params;
    }
}
