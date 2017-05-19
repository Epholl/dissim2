package sk.epholl.dissim.sem3.output;

import sk.epholl.dissim.sem3.entity.deciders.Worker1Condition;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decider;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decision;
import sk.epholl.dissim.sem3.input.FinalMapper;
import sk.epholl.dissim.sem3.simulation.MySimulation;
import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.sem3.simulation.SimulationController;
import sk.epholl.dissim.sem3.simulation.SimulationParameters;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Tomáš on 19.05.2017.
 */
public class ConsoleRunner {

    public static void main(String[] args) throws IOException {
        SimulationController controller = new SimulationController();

        FinalMapper mapper = new FinalMapper() {
            @Override
            protected void onReplication(SimulationParameters params, ArrayList<Worker1Condition> conditions) {
                MySimulation simulation = controller.getSimulation();
                controller.setParameters(params);
                Worker1Decider decider = new Worker1Decider(Worker1Decision.TakeOrder);
                for (Worker1Condition condition: conditions) {
                    decider.addCondition(condition);
                }
                controller.getParameters().setWorker1Decider(decider);
                ConsoleOutput output = new ConsoleOutput(new BufferedWriter(new OutputStreamWriter(System.out)), controller.getParameters(), controller.getResultManager(),
                        Rst.R_CUSTOMER_ORDER_WAIT_TIME);

                controller.addSimulationEndedCallback(() -> {
                    try {
                        output.DoOutput();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                controller.startSimulation();
            }
        };

        mapper.go(FinalMapper.getDefaultConfigFileReader());
    }
}
