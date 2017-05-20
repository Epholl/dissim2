package sk.epholl.dissim.sem3.ui;

import sk.epholl.dissim.sem3.entity.deciders.Worker1Condition;
import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.sem3.simulation.SimulationParameters;
import sk.epholl.dissim.util.subscribers.ResultManager;
import sk.epholl.dissim.util.subscribers.Subscriber;
import sk.epholl.dissim.util.subscribers.ValueType;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomáš on 20.05.2017.
 */
public class CsvOutputLogicModel {

    public static final String DELIMITER = ",";

    private SimulationParameters params;

    private JTextArea textArea;

    private DefaultListModel<Rst.ResultType> listModel;

    private Map<ValueType<Rst.Result>, Rst.Result> results = new HashMap<>();

    public CsvOutputLogicModel(
        JTextArea textArea,
        JList<Rst.ResultType> list,
        DefaultListModel<Rst.ResultType> listModel,
        JButton upButton,
        JButton downButton,
        JButton deleteButton,
        JComboBox<Rst.ResultType> comboBox,
        JButton clearButton
    ) {

        this.textArea = textArea;
        textArea.setFont(textArea.getFont().deriveFont(9f));
        this.listModel = listModel;

        comboBox.setModel(new DefaultComboBoxModel<>(getResultTypes()));
        comboBox.addActionListener(e -> {
            listModel.addElement((Rst.ResultType) comboBox.getSelectedItem());
        });
        list.setModel(listModel);

        upButton.addActionListener(e -> {
            final int selectedIndex = list.getSelectedIndex();
            if (selectedIndex > 0) {
                Rst.ResultType res1 = listModel.get(selectedIndex);
                Rst.ResultType res2 = listModel.get(selectedIndex-1);
                listModel.set(selectedIndex-1, res1);
                listModel.set(selectedIndex, res2);
                list.setSelectedIndex(selectedIndex-1);
            }
        });

        downButton.addActionListener(e -> {
            final int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1 && selectedIndex < (listModel.size()-1)) {
                Rst.ResultType res1 = listModel.get(selectedIndex);
                Rst.ResultType res2 = listModel.get(selectedIndex+1);
                listModel.set(selectedIndex+1, res1);
                listModel.set(selectedIndex, res2);
                list.setSelectedIndex(selectedIndex+1);
            }
        });

        deleteButton.addActionListener(e -> {
            if (listModel.isEmpty()) {
                return;
            }

            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.removeElementAt(selectedIndex);
                list.setSelectedIndex(selectedIndex);
            }
        });

        clearButton.addActionListener(e -> {
            results.clear();
            textArea.setText(null);
            printHeaderValues(listModel);
        });

        listModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                resetTextArea();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                resetTextArea();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                resetTextArea();
            }

            private void resetTextArea() {
                results.clear();
                textArea.setText(null);
                printHeaderValues(listModel);
            }
        });

        printHeaderValues(listModel);
    }

    public void setParams(SimulationParameters params) {
        this.params = params;
    }

    public void init(ResultManager rm) {
        for (Rst.ResultType resultType: Rst.resultTypes()){
            rm.addSubscriber(resultType.valueType, new Subscriber<Rst.Result>() {
                @Override
                public void onValueEmitted(Rst.Result value) {
                    results.put(resultType.valueType, value);
                }
            });
        }
    }

    public void onSimulationFinished() {
        StringBuilder builder = new StringBuilder();
        builder.append(params.getType1WorkerCount());
        builder.append(DELIMITER);
        builder.append(params.getType2WorkerCount());
        builder.append(DELIMITER);
        builder.append(params.getClientIncomeIncreasePercent());
        for (int i = 0; i < listModel.size(); i++) {
            builder.append(DELIMITER);
            Rst.ResultType resultType = listModel.getElementAt(i);
            builder.append(results.get(resultType.valueType).result.getMean());

        }
        builder.append("\n");
        textArea.append(builder.toString());
    }

    private Rst.ResultType[] getResultTypes() {
        return Rst.resultTypes();
    }

    private void printHeaderValues(DefaultListModel<Rst.ResultType> model) {
        StringBuilder builder = new StringBuilder();
        builder.append("Workers 1");
        builder.append(DELIMITER);
        builder.append("Workers 2");
        builder.append(DELIMITER);
        builder.append("Entry increase");
        for (int i = 0; i < model.size(); i++) {
            builder.append(DELIMITER);
            builder.append(model.getElementAt(i).resultName);

        }
        builder.append("\n");
        textArea.append(builder.toString());
    }
}
