package sk.epholl.dissim.sem3.output;

import sk.epholl.dissim.sem3.simulation.SimulationParameters;
import sk.epholl.dissim.util.subscribers.ResultManager;
import sk.epholl.dissim.util.subscribers.ValueType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ConsoleOutput {

    public static final String DELIMITER = "|";

    private BufferedWriter writer;

    private SimulationParameters simulationParameters;

    private LinkedHashMap<ValueType, Object> lastValuesMap = new LinkedHashMap<>();

    public ConsoleOutput(
            BufferedWriter writer,
            SimulationParameters simulationParameters,
            ResultManager resultManager,
            ValueType... valuesObserver
    ) {
        this.writer = writer;
        this.simulationParameters = simulationParameters;

        for (final ValueType valueType : valuesObserver) {
            resultManager.addSubscriber(valueType, value -> lastValuesMap.put(valueType, value));
        }
    }

    public void DoOutput() throws IOException {
        ArrayList<Object> values = new ArrayList<>();

        values.add(simulationParameters.getType1WorkerCount());
        values.add(simulationParameters.getType2WorkerCount());
        values.add(simulationParameters.getClientIncomeIncreasePercent());

        values.addAll(lastValuesMap.values());

        String[] tokens = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            Object obj = values.get(i);
            String text = (obj == null) ? "null" : obj.toString();
            tokens[i] = text;
        }
        outputLine(tokens);
    }

    private void outputLine(String[] tokens) throws IOException {
        StringBuilder b = new StringBuilder();
        for (String token : tokens) {
            if (b.length() > 0) {
                b.append(DELIMITER);
            }
            String escaped = escape(token);
            b.append(token);
        }

        writer.write(b.toString());
    }

    private String escape(String original) {
        return original.replace("|", "\\|");
    }

}
