package sk.epholl.dissim.sem3.ui;

import sk.epholl.dissim.sem3.entity.deciders.OfficeAgentValueProvider;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Condition;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decider;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decision;
import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.sem3.simulation.SimulationController;
import sk.epholl.dissim.util.deciders.Comparator;
import sk.epholl.dissim.util.subscribers.ResultManager;
import sk.epholl.dissim.util.subscribers.Subscriber;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class MainForm extends JFrame {

    enum State {
        Initial,
        Running,
        Paused,
        Finished
    }

    private JPanel contentPanel;
    private JPanel topControlPanel;
    private JButton runButton;
    private JPanel mainContentPanel;
    private JPanel statusBarPanel;
    private JProgressBar simulationProgressBar;
    private JTabbedPane mainTabbedPane;
    private JCheckBox continousRunCheckBox;
    private JSlider simSpeedSlider;
    private JTextArea consoleTextArea;
    private JButton resetButton;
    private JPanel continousSimControlPanel;
    private JLabel simSpeedExplanationLabel;
    private JSpinner type1WorkersSpinner;
    private JSpinner type2WorkersSpinner;
    private JSpinner advertisementCostSpinner;
    private JSpinner replicationCountSpinner;
    private JSpinner warmupPercentSpinner;
    private JLabel type1WorkersPriceLabel;
    private JLabel type2WorkersPricelabel;
    private JLabel advertismentsPriceLabel;
    private JLabel totalMonthlyCostsLabel;
    private JPanel configurationPanel;
    private JPanel resultsPanel;
    private JPanel watchPanel;
    private JPanel consolePanel;
    private JList worker1ConditionsList;
    private JPanel worker1StrategyPanel;
    private JButton worker1MoveUpButton;
    private JButton worker1MoveDownButton;
    private JButton worker1DeleteButton;
    private JComboBox worker1ConditionArg1Combo;
    private JComboBox worker1ConditionComparatorCombo;
    private JComboBox worker1ConditionArg2Combo;
    private JComboBox worker1ConditionReturnValueCombo;
    private JButton worker1AddButton;
    private JComboBox worker1DefaultReturnValueCombo;
    private JTextField worker1Arg1ConstantTextField;
    private JTextField worker1Arg2ConstantTextField;
    private JTable vehiclesTable;
    private JLabel watchRefusedCustomersLabel;
    private JLabel watchFinishedCustomersLabel;
    private JLabel watchShopClosedCustomersLabel;
    private JLabel watchProfitLabel;
    private JLabel watchBalanceLabel;
    private JLabel watchEnteredLabel;
    private JTable parkingTable;
    private JTable workerTable;
    private JPanel graphsPanel;
    private JPanel graph1Panel;
    private JPanel graph2Panel;
    private JComboBox graph1ComboBox;
    private JComboBox graph2ComboBox;
    private JTable resultsTable;
    private JPanel csvOutputPanel;
    private JTextArea csvOutputTextArea;
    private JList csvOutputList;
    private JButton csvOutputUpButton;
    private JComboBox csvOutputComboBox;
    private JButton csvOutputDownButton;
    private JButton csvOutputDeleteButton;
    private JButton csvOutputClearButton;
    private JButton csvOutputAddAllButton;

    private State state;

    private SimulationController simulationController;
    private BottomProgressBarController progressBarController;

    private ConditionLogicModel worker1Model;
    private VehicleTableModel vehicleTableModel;
    private ParkingTableModel parkingTableModel;
    private WorkerTableModel workerTableModel;
    private CsvOutputLogicModel csvOutputModel;

    private GraphController graph1Controller;
    private GraphController graph2Controller;

    private ResultTableController resultTableController;

    public MainForm() {
        super("Parking lot simulation");
        setContentPane(contentPanel);
        setMinimumSize(new Dimension(1024, 768));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);

        initSimulationController();
        initProgressBarController();

        initTopPanel();
        initConfigurationPanel();
        initWorker1Model();
        initConsolePanel();
        initWatchPanel();
        initGraphPanel();
        initResultPanel();
        initCsvOutputPanel();

        setState(State.Initial);
    }


    public void setState(State newState) {
        if (state != newState) {
            state = newState;
            switch (state) {
                case Initial:
                    RecursiveComponentEnabler.setEnabled(configurationPanel, true);
                    progressBarController.reset();
                    resetButton.setEnabled(false);
                    runButton.setEnabled(true);
                    runButton.setText("Run");
                    break;

                case Running:
                    RecursiveComponentEnabler.setEnabled(configurationPanel, false);
                    runButton.setText("Pause");
                    resetButton.setEnabled(false);
                    break;

                case Paused:
                    RecursiveComponentEnabler.setEnabled(configurationPanel, false);
                    resetButton.setEnabled(true);
                    runButton.setText("Resume");
                    break;

                case Finished:
                    RecursiveComponentEnabler.setEnabled(configurationPanel, false);
                    resetButton.setEnabled(true);
                    runButton.setEnabled(false);
                    break;
            }
        }
    }

    private void initTopPanel() {

        runButton.addActionListener(e -> {
            switch (state) {
                case Initial:
                    figureSimSpeed();
                    simulationController.startSimulation();
                    setState(State.Running);
                    break;

                case Running:
                    simulationController.pauseSimulation();
                    setState(State.Paused);
                    break;

                case Paused:
                    simulationController.resumeSimulation();
                    setState(State.Running);
            }
        });

        simSpeedSlider.setMinimum(0);
        simSpeedSlider.setMaximum(SimulationSpeed.SPEEDS.length-1);
        simSpeedSlider.setValue(0);
        simSpeedSlider.addChangeListener(e -> {
            figureSimSpeed();
        });

        resetButton.addActionListener(e -> {
            simulationController.resetSimulation();
            graph1Controller.clear();
            graph2Controller.clear();
            progressBarController.reset();
            resultTableController.reset();
            setState(State.Initial);
        });

        continousRunCheckBox.addActionListener(e -> {
            figureSimSpeed();
        });
        figureSimSpeed();
    }

    private void initSimulationController() {
        simulationController = new SimulationController();
        simulationController.getParameters().setReplicationDurationDays(21); //TODO parametrize
        simulationController.addSimulationEndedCallback(() -> {
            setState(State.Finished);
        });
    }

    private void initConsolePanel() {

        final BufferedSubscriber<String> subscriber = new BufferedSubscriber<String>() {
            @Override
            public void onValuesEmitted(List<String> emittedValues) {
                final StringBuilder stringBuilder = new StringBuilder();
                for (String line: emittedValues) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
                consoleTextArea.append(stringBuilder.toString());
            }
        };
        simulationController.getResultManager().addSubscriber(Rst.CONSOLE_LOG, subscriber);
    }

    private void initConfigurationPanel() {

        replicationCountSpinner.setModel(new SpinnerNumberModel(1000, 100, 100000, 1000));
        replicationCountSpinner.addChangeListener(changeEvent -> {
            simulationController.getParameters().setRepliacationCount((Integer)replicationCountSpinner.getModel().getValue());
        });

        warmupPercentSpinner.setModel(new SpinnerNumberModel(10, 0, 30, 1));
        warmupPercentSpinner.addChangeListener(changeEvent -> {
            simulationController.getParameters().setWarmupPercentage((Integer)warmupPercentSpinner.getModel().getValue());
        });

        type1WorkersSpinner.setModel(new SpinnerNumberModel(1, 1, 50, 1));
        type1WorkersSpinner.addChangeListener(changeEvent -> {
            simulationController.getParameters().setType1WorkerCount((Integer)type1WorkersSpinner.getModel().getValue());
            type1WorkersPriceLabel.setText("= " + simulationController.getParameters().getWorker1TotalPrice() + "€ monthly");
            updateTotalPrice();
        });

        type2WorkersSpinner.setModel(new SpinnerNumberModel(1, 1, 50, 1));
        type2WorkersSpinner.addChangeListener(changeEvent -> {
            simulationController.getParameters().setType2WorkerCount((Integer)type2WorkersSpinner.getModel().getValue());
            type2WorkersPricelabel.setText("= " + simulationController.getParameters().getWorker2TotalPrice() + "€ monthly");
            updateTotalPrice();
        });
        advertisementCostSpinner.setModel(new SpinnerNumberModel(0, 0, 78, 1));
        advertisementCostSpinner.addChangeListener(changeEvent -> {
            simulationController.getParameters().setAdvertismentIncrease((Integer)advertisementCostSpinner.getModel().getValue());
            advertismentsPriceLabel.setText("% = " + simulationController.getParameters().getAdvertisementTotalPrice() + "€ monthly");
            updateTotalPrice();
        });

        simulationController.getParameters().setRepliacationCount((Integer)replicationCountSpinner.getModel().getValue());
        simulationController.getParameters().setWarmupPercentage((Integer)warmupPercentSpinner.getModel().getValue());
        simulationController.getParameters().setType1WorkerCount((Integer)type1WorkersSpinner.getModel().getValue());
        type1WorkersPriceLabel.setText("= " + simulationController.getParameters().getWorker1TotalPrice() + "€ monthly");
        simulationController.getParameters().setType2WorkerCount((Integer)type2WorkersSpinner.getModel().getValue());
        type2WorkersPricelabel.setText("= " + simulationController.getParameters().getWorker2TotalPrice() + "€ monthly");
        advertismentsPriceLabel.setText("% = " + simulationController.getParameters().getAdvertisementTotalPrice() + "€ monthly");
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        totalMonthlyCostsLabel.setText("= " + simulationController.getParameters().getTotalPrice() + "€ monthly");
    }

    private void initProgressBarController() {

        progressBarController = new BottomProgressBarController(simulationProgressBar, simulationController);
        progressBarController.setContinous(continousRunCheckBox.isSelected());
    }

    private void initWorker1Model() {
        DefaultListModel<Worker1Condition> listModel = new DefaultListModel<>();
        worker1ConditionsList.setModel(listModel);

        worker1DefaultReturnValueCombo.setModel(new DefaultComboBoxModel(Worker1Decision.values()));
        worker1ConditionReturnValueCombo.setModel(new DefaultComboBoxModel(Worker1Decision.values()));
        worker1ConditionArg1Combo.setModel(new DefaultComboBoxModel(OfficeAgentValueProvider.values()));
        worker1ConditionComparatorCombo.setModel(new DefaultComboBoxModel(Comparator.values()));
        worker1ConditionArg2Combo.setModel(new DefaultComboBoxModel(OfficeAgentValueProvider.values()));

        worker1Arg1ConstantTextField.setEditable(false);
        worker1Arg2ConstantTextField.setEditable(false);

        worker1Model = new ConditionLogicModel(
                worker1ConditionsList,
                listModel,
                worker1MoveUpButton,
                worker1MoveDownButton,
                worker1DeleteButton,
                worker1ConditionArg1Combo,
                worker1ConditionComparatorCombo,
                worker1ConditionArg2Combo,
                worker1Arg1ConstantTextField,
                worker1Arg2ConstantTextField,
                worker1ConditionReturnValueCombo,
                worker1DefaultReturnValueCombo,
                worker1AddButton);

        listModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                resetDecider();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                resetDecider();

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                resetDecider();
            }

            private void resetDecider() {
                Worker1Decider decider = simulationController.getParameters().getWorker1Decider();
                decider.clear();
                int size = listModel.size();
                for (int i = 0; i < size; i++) {
                    decider.addCondition(listModel.elementAt(i));
                }
            }
        });
    }

    private void figureSimSpeed() {
        final int value = simSpeedSlider.getValue();
        final double speedSeconds = SimulationSpeed.SPEEDS[value];
        if (continousRunCheckBox.isSelected()) {
            simulationController.setSimulationSpeed(speedSeconds);
        } else {
            simulationController.setMaxSpeed();
        }

        progressBarController.setContinous(continousRunCheckBox.isSelected());
        simSpeedExplanationLabel.setText(SimulationSpeed.getLabel(value));
    }

    private void initGraphPanel() {
        graph1Controller = new GraphController(graph1Panel, graph1ComboBox);
        graph2Controller = new GraphController(graph2Panel, graph2ComboBox);
        graph1Controller.init(rm());
        graph2Controller.init(rm());
    }

    private void initWatchPanel() {

        initVehicleTable();
        new JLabelResultController("Entered: ", watchEnteredLabel, Rst.ENTERED_CUSTOMERS, rm());
        new JLabelResultController("Finished: ", watchFinishedCustomersLabel, Rst.FINISHED_CUSTOMERS, rm());
        new JLabelResultController("Refused: ", watchRefusedCustomersLabel, Rst.REFUSED_CUSTOMERS, rm());
        new JLabelResultController("Day end: ", watchShopClosedCustomersLabel, Rst.SHOP_CLOSED_CUSTOMERS, rm());
        new JLabelResultController("Profit: ", watchProfitLabel, Rst.PROFIT, rm());
        new JLabelResultController("Balance: ", watchBalanceLabel, Rst.BALANCE, rm());
        initParkingTable();
        initWorkerTable();
    }


    private void initResultPanel() {
        resultTableController = new ResultTableController(resultsTable);
        resultTableController.init(simulationController.getResultManager());
    }

    private void initCsvOutputPanel() {

        DefaultListModel<Rst.ResultType> listModel = new DefaultListModel<>();
        csvOutputModel = new CsvOutputLogicModel(
                csvOutputTextArea,
                csvOutputList,
                listModel,
                csvOutputUpButton,
                csvOutputDownButton,
                csvOutputDeleteButton,
                csvOutputComboBox,
                csvOutputClearButton,
                csvOutputAddAllButton
                );
        csvOutputModel.setParams(simulationController.getParameters());
        csvOutputModel.init(simulationController.getResultManager());
        simulationController.addSimulationEndedCallback( () -> {
            csvOutputModel.onSimulationFinished();
        });
    }

    private void initVehicleTable() {
        vehicleTableModel = new VehicleTableModel();
        vehiclesTable.setModel(vehicleTableModel);
        simulationController.getResultManager().addSubscriber(Rst.VEHICLE_STATE, new Subscriber<Rst.VehicleUpdate>() {
            @Override
            public void onValueEmitted(Rst.VehicleUpdate value) {
                vehicleTableModel.setNewData(value);
            }
        });
        vehiclesTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        vehiclesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        vehiclesTable.getColumnModel().getColumn(3).setPreferredWidth(24);
        vehiclesTable.getColumnModel().getColumn(4).setPreferredWidth(12);
    }

    private void initParkingTable() {
        parkingTableModel = new ParkingTableModel();
        parkingTable.setModel(parkingTableModel);
        simulationController.getResultManager().addSubscriber(Rst.PARKING_STATE, new Subscriber<Rst.ParkingUpdate>() {
            @Override
            public void onValueEmitted(Rst.ParkingUpdate value) {
                parkingTableModel.setNewData(value);
            }
        });
    }

    private void initWorkerTable() {
        workerTableModel = new WorkerTableModel();
        workerTable.setModel(workerTableModel);
        simulationController.getResultManager().addSubscriber(Rst.WORKER1_STATE, new Subscriber<Rst.WorkerUpdate>() {
            @Override
            public void onValueEmitted(Rst.WorkerUpdate value) {
                workerTableModel.setNewWorker1Data(value);
            }
        });
        simulationController.getResultManager().addSubscriber(Rst.WORKER2_STATE, new Subscriber<Rst.WorkerUpdate>() {
            @Override
            public void onValueEmitted(Rst.WorkerUpdate value) {
                workerTableModel.setNewWorker2Data(value);
            }
        });
    }

    private static class VehicleTableModel extends AbstractTableModel {

        private String[] columnNames = new String[] {"Vehicle", "Place", "Activity", "Worker", "State"};

        private Rst.VehicleUpdate vehicles;

        {
            vehicles = new Rst.VehicleUpdate();
            vehicles.states = Collections.EMPTY_LIST;
        }

        @Override
        public int getRowCount() {
            return vehicles.states.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Rst.VehicleState state = vehicles.states.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return state.name;
                case 1:
                    return state.position;
                case 2:
                    return state.state;
                case 3:
                    return state.worker;
                case 4:
                    if (state.timeStateStarted >= state.timeStateEnds) {
                        return " - ";
                    } else {
                        return getStatePercentage(state);
                    }
            }
            return " - ";
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        public void setNewData(Rst.VehicleUpdate vehicles) {
            this.vehicles = vehicles;
            fireTableDataChanged();
        }

        private String getStatePercentage(Rst.VehicleState state) {
            double difference = state.timeStateEnds - state.timeStateStarted;
            double progress = vehicles.simTime - state.timeStateStarted;
            double percent = (progress / difference)*100;
            return String.format("%.1f%%", percent);
        }
    }

    private static class ParkingTableModel extends AbstractTableModel {

        private String[] columnNames = new String[] {"Spot", "State", "Vehicle", "Load rate"};

        private Rst.ParkingUpdate spots;

        {
            spots = new Rst.ParkingUpdate();
            spots.spots = Collections.EMPTY_LIST;
        }

        @Override
        public int getRowCount() {
            return spots.spots.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Rst.ParkingSpotState state = spots.spots.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return state.name;
                case 1:
                    return state.state;
                case 2:
                    return state.vehicle;
                case 3:
                    return String.format("%.2f%%", state.loadCoeficient*100);
            }
            return " - ";
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        public void setNewData(Rst.ParkingUpdate spots) {
            this.spots = spots;
            fireTableDataChanged();
        }
    }

    private static class WorkerTableModel extends AbstractTableModel {

        private String[] columnNames = new String[] {"Worker", "State", "Vehicle", "Load rate"};

        private List<Rst.WorkerState> workers1;
        private List<Rst.WorkerState> workers2;
        private List<Rst.WorkerState> allWorkers;

        {
            allWorkers = new ArrayList<>();
            workers1 = Collections.EMPTY_LIST;
            workers2 = Collections.EMPTY_LIST;
        }

        @Override
        public int getRowCount() {
            return allWorkers.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Rst.WorkerState state =allWorkers.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return state.name;
                case 1:
                    return state.state;
                case 2:
                    return state.vehicle;
                case 3:
                    return String.format("%.2f%%", state.loadCoeficient*100);
            }
            return " - ";
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        public void setNewWorker1Data(Rst.WorkerUpdate spots) {
            allWorkers.clear();
            this.workers1 = spots.states;
            allWorkers.addAll(this.workers1);
            allWorkers.addAll(this.workers2);
            fireTableDataChanged();
        }

        public void setNewWorker2Data(Rst.WorkerUpdate spots) {
            allWorkers.clear();
            this.workers2 = spots.states;
            allWorkers.addAll(this.workers1);
            allWorkers.addAll(this.workers2);
            fireTableDataChanged();
        }
    }

    private ResultManager rm() {
        return simulationController.getResultManager();
    }
}
