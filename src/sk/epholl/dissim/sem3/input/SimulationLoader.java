package sk.epholl.dissim.sem3.input;

import sun.plugin.dom.exception.InvalidStateException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationLoader {

    public static void main(String[] args) throws Exception {
        System.out.println("Testing load !!!");

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        BufferedReader reader = new BufferedReader(new FileReader("./conf.txt"));

        SimulationLoader loader = new SimulationLoader();
        loader.load(reader);

        ArrayList<ConfigurableSimulationParameters> paramsList = new ArrayList<>();
        do {
            ConfigurableSimulationParameters params = loader.createConfigurableSimulationParameters();
            paramsList.add(params);
        } while (loader.nextDataSet());

            System.out.println(paramsList);
    }

    private static final int
            TYPE_1_WORKER_COUNT_INDEX = 0,
            TYPE_2_WORKER_COUNT_INDEX = 1,
            CLIENT_INCOME_INCREASE_PERCENT_INDEX = 2;

    private static final int COUNT = 3;
    private int[] indexes = new int[COUNT];

    private int replicationCount = -1;
    private int[] type1WorkerCountArray;
    private int[] type2WorkerCountArray;
    private double[] clientIncomeIncreasePercentArray;

    private ArrayList<ConditionDef> conditionDefs = new ArrayList<>();

    public SimulationLoader() {
        reset();
    }

    private void reset() {
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = 0;
        }

        replicationCount = -1;

        type1WorkerCountArray = null;
        type2WorkerCountArray = null;
        clientIncomeIncreasePercentArray = null;

        conditionDefs.clear();
    }

    public void load(BufferedReader reader) throws IOException {
        reset();

        int lineIndex = -1;
        String line;
        while ((line = reader.readLine()) != null) {
            lineIndex++;
            if (line.isEmpty() || line.charAt(0) == '#') {
                continue;
            }

            int delimiterPos = line.indexOf(':');
            if (delimiterPos == -1 || delimiterPos == line.length() - 1) {
                String message = String.format("Line %d without delimiter or with delimiter at the end: %s", lineIndex, line);
                throw new IllegalArgumentException(message);
            }

            String type = line.substring(0, delimiterPos).trim();
            String content = line.substring(delimiterPos + 1).trim();

            if (type.isEmpty() || content.isEmpty()) {
                String message = String.format("Invalid line %d: type %s, content %s", lineIndex, type, content);
                throw new IllegalArgumentException(message);
            }

            processLine(type, content);
        }

        validateData();
    }

    private void validateData() {
        if (replicationCount == -1) {
            throw new IllegalArgumentException("Replications count not specified.");
        }

        ensureNotNull(type1WorkerCountArray, "Type 1 worker count");
        ensureNotNull(type2WorkerCountArray, "Type 2 worker count");
        ensureNotNull(clientIncomeIncreasePercentArray, "Client income increase percent");
    }

    private void ensureNotNull(Object obj, String name) {
        if (obj == null) {
            throw new InvalidStateException("Null value detected for: " + name);
        }
    }

    private void processLine(String type, String content) {
        switch (type) {
            case "replicationCount":
                replicationCount = parseReplicationsCount(content);
                break;
            case "type1WorkerCountRange":
                type1WorkerCountArray = createIntArray(content);
                break;
            case "type2WorkerCountRange":
                type2WorkerCountArray = createIntArray(content);
                break;
            case "clientIncomeIncreasePercentRange":
                clientIncomeIncreasePercentArray = createDoubleArray(content);
                break;
            case "?":
                parseCondition(content);
                break;
            default:
                    throw new IllegalArgumentException("Unknown line type: " + type);
        }
    }

    private int parseReplicationsCount(String content) {
        try {
            int replicationsCount = Integer.parseInt(content);
            if (replicationsCount <= 0) {
                throw new IllegalArgumentException("Replications count must be positive non-zero number.");
            }
            return replicationsCount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Replications count is not a number: " + content);
        }
    }

    private void parseCondition(String content) {
        String[] tokens = content.split(" ");
        ensureTokens(tokens);

        if (tokens.length != 6) {
            throw new IllegalArgumentException("Conditions must contain 5 tokens.");
        }

        String leftOperand = tokens[1];
        boolean leftOperandConstant = checkConstant(tokens[0]);
        if (leftOperandConstant) {
            ensureDouble(leftOperand);
        }

        String rightOperand = tokens[4];
        boolean rightOperandConstant = checkConstant(tokens[3]);
        if (rightOperandConstant) {
            ensureDouble(rightOperand);
        }

        String operator = tokens[2];

        String successReturnValue = tokens[5];

        ConditionDef conditionDef = new ConditionDef(
                leftOperand,
                leftOperandConstant,
                operator,
                rightOperand,
                rightOperandConstant,
                successReturnValue
        );

        conditionDefs.add(conditionDef);
    }

    private boolean checkConstant(String value) {
        switch (value) {
            case "v":
                return false;
            case "c":
                return true;
            default:
                    throw new IllegalArgumentException("Unsupported variable type: " + value);
        }
    }

    private void ensureDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(value + " is not a double.");
        }
    }

    public boolean nextDataSet() {
        for (int i = 0; i < COUNT; i++) {
            int value = indexes[i];
            value++;

            boolean overflow = checkValueIndexOverflow(i, value);
            if (!overflow) {
                indexes[i] = value;
                return true;
            } else {
                indexes[i] = 0;
            }
        }

        return false;
    }

    private boolean checkValueIndexOverflow(int variableIndex, int valueIndex) {
        int max = getDataArrayLength(variableIndex);
        return valueIndex >= max;
    }

    private int getDataArrayLength(int variableIndex) {
        switch (variableIndex) {
            case TYPE_1_WORKER_COUNT_INDEX:
                return type1WorkerCountArray.length;
            case TYPE_2_WORKER_COUNT_INDEX:
                return type2WorkerCountArray.length;
            case CLIENT_INCOME_INCREASE_PERCENT_INDEX:
                return clientIncomeIncreasePercentArray.length;
            default:
                throw new IllegalArgumentException("Unsupported index: " + variableIndex);
        }
    }

    public ConfigurableSimulationParameters createConfigurableSimulationParameters() {
        return new ConfigurableSimulationParameters(
                replicationCount,
                type1WorkerCountArray[getValueIndex(TYPE_1_WORKER_COUNT_INDEX)],
                type2WorkerCountArray[getValueIndex(TYPE_2_WORKER_COUNT_INDEX)],
                clientIncomeIncreasePercentArray[getValueIndex(CLIENT_INCOME_INCREASE_PERCENT_INDEX)],
                conditionDefs
        );
    }

    private int getValueIndex(int variableIndex) {
        return indexes[variableIndex];
    }

    private int[] createIntArray(String content) {
        String tokens[] = createRangeTokens(content);
        ensureTokens(tokens);

        int[] intArray = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            intArray[i] = Integer.parseInt(token);
        }

        return intArray;
    }

    private double[] createDoubleArray(String content) {
        String tokens[] = createRangeTokens(content);
        ensureTokens(tokens);

        double[] doubleArray = new double[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            doubleArray[i] = Integer.parseInt(token);
        }

        return doubleArray;
    }

    private String[] createRangeTokens(String content) {
        return content.split(",");
    }

    private void ensureTokens(String[] tokens) {
        for (int i = 0; i < tokens.length; i++)
        {
            String token = tokens[i];
            String trimmed = token.trim();
            if (trimmed.isEmpty()) {
                String message = String.format("Token %d is empty.", i);
                throw new IllegalArgumentException(message);
            }
        }
    }

    public static class ConfigurableSimulationParameters {

        public final int replicationCount;
        public final int type1WorkerCount;
        public final int type2WorkerCount;
        public final double clientIncomeIncreasePercent;

        public final ArrayList<ConditionDef> conditionDefs = new ArrayList<>();

        public ConfigurableSimulationParameters(
                int replicationCount,
                int type1WorkerCount,
                int type2WorkerCount,
                double clientIncomeIncreasePercent,
                List<ConditionDef> conditionDefs
        ) {
            this.replicationCount = replicationCount;
            this.type1WorkerCount = type1WorkerCount;
            this.type2WorkerCount = type2WorkerCount;
            this.clientIncomeIncreasePercent = clientIncomeIncreasePercent;

            this.conditionDefs.addAll(conditionDefs);
        }
    }

    public static class ConditionDef {

        public final String leftOperand;
        public final boolean leftOperandConstant;
        public final String operator;
        public final String rightOperand;
        public final boolean rightOperandConstant;
        public final String successReturnValue;

        public ConditionDef(
                String leftOperand,
                boolean leftOperandConstant,
                String operator,
                String rightOperand,
                boolean rightOperandConstant,
                String successReturnValue
        ) {
            this.leftOperand = leftOperand;
            this.leftOperandConstant = leftOperandConstant;
            this.operator = operator;
            this.rightOperand = rightOperand;
            this.rightOperandConstant = rightOperandConstant;
            this.successReturnValue = successReturnValue;
        }

        public double leftOperandAsConstant() {
            return Double.parseDouble(leftOperand);
        }

        public double rightOperandAsConstant() {
            return Double.parseDouble(rightOperand);
        }
    }
}
