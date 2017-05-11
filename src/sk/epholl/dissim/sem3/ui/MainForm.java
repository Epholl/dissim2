package sk.epholl.dissim.sem3.ui;

import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.sem3.simulation.SimulationController;
import sk.epholl.dissim.util.subscribers.Subscriber;

import javax.swing.*;
import java.awt.*;
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
    private JComboBox type1WorkerStrategyComboBox;
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
    private JPanel entriesPanel;
    private JPanel consolePanel;

    private State state;

    private SimulationController simulationController;
    private BottomProgressBarController progressBarController;

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
        initConsolePanel();

        setState(State.Initial);

        mainTabbedPane.setSelectedIndex(2);
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
            simulationController.getParameters().setNumber1Workers((Integer)type1WorkersSpinner.getModel().getValue());
            type1WorkersPriceLabel.setText("= " + simulationController.getParameters().getWorker1TotalPrice() + "€ monthly");
            updateTotalPrice();
        });

        type2WorkersSpinner.setModel(new SpinnerNumberModel(1, 1, 50, 1));
        type2WorkersSpinner.addChangeListener(changeEvent -> {
            simulationController.getParameters().setNumber2Workers((Integer)type2WorkersSpinner.getModel().getValue());
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
        simulationController.getParameters().setNumber1Workers((Integer)type1WorkersSpinner.getModel().getValue());
        type1WorkersPriceLabel.setText("= " + simulationController.getParameters().getWorker1TotalPrice() + "€ monthly");
        simulationController.getParameters().setNumber2Workers((Integer)type2WorkersSpinner.getModel().getValue());
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
}
