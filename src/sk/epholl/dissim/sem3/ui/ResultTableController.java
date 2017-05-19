package sk.epholl.dissim.sem3.ui;

import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.util.TimeUtils;
import sk.epholl.dissim.util.subscribers.ResultManager;
import sk.epholl.dissim.util.subscribers.Subscriber;
import sk.epholl.dissim.util.subscribers.ValueType;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;

/**
 * Created by Tomáš on 19.05.2017.
 */
public class ResultTableController {

    private JTable table;

    private ResultTableModel model;

    public ResultTableController(final JTable table) {
        this.table = table;
        model = new ResultTableModel();
        table.setModel(model);
    }

    public void init(ResultManager rm) {
        model.init(rm);
    }

    public void reset() {
        model.clear();
    }

    private static class ResultTableModel extends AbstractTableModel {

        private static final String[] columnNames = new String[] {
                "Name", "Value", "90% confidence left", "90% confidence right"
        };

        private Rst.ResultType[] resultTypes = new Rst.ResultType[0];
        private Rst.Result[] results = new Rst.Result[0];

        @Override
        public int getRowCount() {
            return resultTypes.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Rst.Result result = results[rowIndex];
            switch (columnIndex) {
                case 0:
                    return resultTypes[rowIndex].resultName;
                case 1:
                    return result == null? " - ": getValue(rowIndex, result.result.getMean());
                case 2:
                    return result == null? " - ": getValue(rowIndex, result.result.getLeftConfidenceInterval());
                case 3:
                    return result == null? " - ": getValue(rowIndex, result.result.getRightConfidenceInterval());
            }
            return " - ";
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        public void clear() {
            for (int i = 0; i < results.length; i++) {
                results[i] = null;
            }
        }

        public void init(ResultManager rm) {
            resultTypes = Rst.resultTypes();
            results = new Rst.Result[resultTypes.length];
            for (int i = 0; i < resultTypes.length; i++) {

                final int index = i;
                rm.addSubscriber(resultTypes[i].valueType, new Subscriber<Rst.Result>() {
                    @Override
                    public void onValueEmitted(Rst.Result value) {
                        results[index] = value;
                        fireTableDataChanged();
                    }
                });
            }
        }

        private String getValue(int rowIndex, double value) {
            if (resultTypes[rowIndex].isTime()) {
                return TimeUtils.formatTimePeriod(value);
            }
            return String.format("%.4f", value);
        }
    }
}
