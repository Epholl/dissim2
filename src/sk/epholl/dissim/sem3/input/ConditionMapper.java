package sk.epholl.dissim.sem3.input;

import sk.epholl.dissim.sem3.entity.deciders.OfficeAgentValueProvider;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Condition;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decision;
import sk.epholl.dissim.util.deciders.Comparator;
import sk.epholl.dissim.util.deciders.Condition;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConditionMapper {

    private HashMap<String, Comparator> comparatorsMaps = new HashMap<>();
    private HashMap<String, OfficeAgentValueProvider> providersMap = new HashMap<>();
    private HashMap<String, Worker1Decision> returnValuesMap = new HashMap<>();

    public void addComparator(String name, Comparator comparator) {
        comparatorsMaps.put(name, comparator);
    }

    public void addProvider(String name, OfficeAgentValueProvider provider) {
        providersMap.put(name, provider);
    }

    public void addSuccessReturnValue(String name, Worker1Decision successReturnValue) {
        returnValuesMap.put(name, successReturnValue);
    }

    public ArrayList<Worker1Condition> createConditions(List<SimulationLoader.ConditionDef> conditionDefs) {
        ArrayList<Worker1Condition> conditions = new ArrayList<>();

        for (SimulationLoader.ConditionDef def : conditionDefs) {
            Worker1Condition condition = createCondition(def);
            conditions.add(condition);
        }

        return conditions;
    }

    private Worker1Condition createCondition(SimulationLoader.ConditionDef def) {
        OfficeAgentValueProvider left;
        OfficeAgentValueProvider right;
        Comparator comparator;
        Worker1Decision successReturnValue;

        if (def.leftOperandConstant) {
            left = OfficeAgentValueProvider.newConstant(def.leftOperandAsConstant());
        } else {
            left = getValueProvider(def.leftOperand);
        }

        if (def.rightOperandConstant) {
            right = OfficeAgentValueProvider.newConstant(def.rightOperandAsConstant());
        } else {
            right = getValueProvider(def.rightOperand);
        }

        comparator = getComparator(def.operator);

        successReturnValue = getSuccessReturnValue(def.successReturnValue);

        return new Worker1Condition(left, comparator, right, successReturnValue);
    }

    private OfficeAgentValueProvider getValueProvider(String name) {
        OfficeAgentValueProvider provider = providersMap.get(name);
        if (provider == null) {
            throw new IllegalArgumentException("Provider not configured: " + name);
        }
        return provider;
    }

    private Comparator getComparator(String name) {
        Comparator comparator = comparatorsMaps.get(name);
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator not configured: " + name);
        }
        return comparator;
    }

    private Worker1Decision getSuccessReturnValue(String name) {
        Worker1Decision successReturnValue = returnValuesMap.get(name);
        if (successReturnValue == null) {
            throw new IllegalArgumentException("Success return value not configured: " + name);
        }
        return successReturnValue;
    }
}
