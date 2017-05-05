package sk.epholl.dissim.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import sk.epholl.dissim.carshop.CarShopSimulation;
import sk.epholl.dissim.carshop.CarShopSimulationCore;
import sk.epholl.dissim.carshop.CarShopSimulationParameters;
import sk.epholl.dissim.core.Simulation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;

/**
 * Created by Tomáš on 10.03.2016.
 */
public class MainForm extends JFrame {
    private JTextField replicationCountTextField;
    private JPanel rootPanel;
    private JButton startButton;
    private JPanel chartPanel;
    private JTextField type1TextField;
    private JTextField warmupTextField;
    private JButton resetButton;
    private JTextField type2TextField;
    private JLabel entryWaitLabel;
    private JLabel repairWaitLabel;
    private JLabel iterationPercentageLabel;
    private JLabel carsInWorkshopLabel;
    private JTabbedPane mainTabbedPane;

    private JCheckBox entryWaitCheckbox;
    private JCheckBox repairWaitCheckbox;
    private JCheckBox returnWaitCheckbox;
    private JCheckBox entryQueueCheckbox;
    private JCheckBox RepairQueueCheckbox;
    private JCheckBox returnQueueCheckbox;
    private JCheckBox finishedCustomersCheckbox;
    private JCheckBox refusedCustomersCheckbox;
    private JCheckBox totalTimeCheckbox;
    private JCheckBox totalCustomerRepairWaitCheckbox;
    private JButton waitingQueuesButton;
    private JButton systemTimesButton;
    private JCheckBox continuousRunEnabledCheckbox;
    private JPanel continousControls;
    private JButton startButton2;
    private JButton resetButton2;
    private JLabel timeLabel;
    private JLabel type1FreeWorkersLabel;
    private JLabel type2FreeWorkersLabel;
    private JSlider speedSlider;
    private JList carsList;
    private JTextArea textArea1;

    private CarShopSimulation simulation;
    private CarShopSimulation.Result lastResult;

    private XYSeries finishedCustomersSeries;
    private XYSeries refusedCustomersSeries;

    private XYSeries timeInSystemSeries;
    private XYSeries timeFromAcquieitionToEndSeries;

    private XYSeries averageEntryWaitSeries;
    private XYSeries averageEntryQueueSeries;

    private XYSeries averageRepairWaitSeries;
    private XYSeries averageRepairQueueSeries;

    private XYSeries averageReturnWaitSeries;
    private XYSeries averageReturnQueueSeries;

    private XYSeriesCollection dataCollection;

    public MainForm() {
        super("Sem 1");
        setContentPane(mainTabbedPane);
        setMinimumSize(new Dimension(1024, 768));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initChart();
        setVisible(true);

        startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                startButtonClicked();
            }
        });

        startButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButtonClicked();
            }
        });

        resetButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetButtonClicked();
            }
        });

        resetButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetButtonClicked();
            }
        });


        configureCheckbox(entryWaitCheckbox, averageEntryWaitSeries);
        configureCheckbox(repairWaitCheckbox, averageRepairWaitSeries);
        configureCheckbox(returnWaitCheckbox, averageReturnWaitSeries);
        configureCheckbox(entryQueueCheckbox, averageEntryQueueSeries);
        configureCheckbox(RepairQueueCheckbox, averageRepairQueueSeries);
        configureCheckbox(returnQueueCheckbox, averageReturnQueueSeries);
        configureCheckbox(finishedCustomersCheckbox, finishedCustomersSeries);
        configureCheckbox(refusedCustomersCheckbox, refusedCustomersSeries);
        configureCheckbox(totalTimeCheckbox, timeInSystemSeries);
        configureCheckbox(totalCustomerRepairWaitCheckbox, timeFromAcquieitionToEndSeries);

        waitingQueuesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AverageQueueLengthsFrame();
            }
        });

        systemTimesButton.addActionListener(e -> {
            new AverageTimeInSystemFrame();
        });

        continuousRunEnabledCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (continuousRunEnabledCheckbox.isSelected()) {
                    continousControls.setEnabled(true);
                    if (simulation != null) {
                        simulation.setContinous(true);
                    }
                } else {
                    continousControls.setEnabled(false);
                    if (simulation != null) {
                        simulation.setContinous(false);
                    }
                }
            }
        });

        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (simulation != null) {
                    simulation.setSpeed(speedSlider.getValue());
                }
            }
        });
    }

    private void initChart() {

        finishedCustomersSeries = new XYSeries("Amount of finished customers");
        finishedCustomersSeries.setMaximumItemCount(300);
        refusedCustomersSeries = new XYSeries("Amount of refused customers");
        refusedCustomersSeries.setMaximumItemCount(300);

        timeInSystemSeries = new XYSeries("Time spent in system");
        timeInSystemSeries.setMaximumItemCount(300);

        timeFromAcquieitionToEndSeries = new XYSeries("Time spent waiting for repairs");
        timeFromAcquieitionToEndSeries.setMaximumItemCount(300);

        averageEntryWaitSeries= new XYSeries("Average wait to enter");
        averageEntryWaitSeries.setMaximumItemCount(300);

        averageEntryQueueSeries= new XYSeries("Average entry queue length");
        averageEntryQueueSeries.setMaximumItemCount(300);

        averageRepairWaitSeries = new XYSeries("Average wait for repair");
        averageRepairWaitSeries.setMaximumItemCount(300);

        averageRepairQueueSeries = new XYSeries("Average repair queue length");
        averageRepairQueueSeries.setMaximumItemCount(300);

        averageReturnWaitSeries = new XYSeries("Average wait to return");
        averageReturnWaitSeries.setMaximumItemCount(300);

        averageReturnQueueSeries = new XYSeries("Average return queue length");
        averageReturnQueueSeries.setMaximumItemCount(300);

        dataCollection = new XYSeriesCollection();

        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "Replication count",
                "Average timeLabel / count",
                dataCollection,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );
        NumberAxis axis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        axis.setAutoRangeIncludesZero(false);

        ChartPanel chartPanel = new ChartPanel(chart);

        this.chartPanel.setLayout(new BoxLayout(this.chartPanel, BoxLayout.PAGE_AXIS));
        this.chartPanel.add(chartPanel);
    }

    private void startSimulation() {
        finishedCustomersSeries.clear();
        refusedCustomersSeries.clear();

        try {
            int replicationCount = Integer.parseInt(replicationCountTextField.getText());
            int type1Workers = Integer.parseInt(type1TextField.getText());
            int type2Workers = Integer.parseInt(type2TextField.getText());
            CarShopSimulationParameters params = new CarShopSimulationParameters(replicationCount, type1Workers, type2Workers, 90*8);

            try {
                if (warmupTextField.getText().endsWith("%")) {
                    params.setWarmupReplicationPercentage(Double.parseDouble(warmupTextField.getText().replaceAll("%", "")));
                } else {
                    params.setWarmupReplicationCount(Integer.parseInt(warmupTextField.getText()));
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(rootPanel, "Unknown warmup duration: use absolute integer value (1000) or percentage (20%)");
                return;
            }
            params.setGuiUpdateInterval(Math.max(1, replicationCount / 300));

            repairWaitLabel.setText("Warmup...");
            entryWaitLabel.setText("Warmup...");
            carsInWorkshopLabel.setText("Warmup...");
            simulation = new CarShopSimulation(params, new Simulation.SimulationListener<CarShopSimulation.Result, CarShopSimulation.State>() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onFinished(CarShopSimulation.Result results) {
                    showResults(results);
                    iterationPercentageLabel.setText("100%");

                    resetButton.setEnabled(true);
                    startButton.setEnabled(false);
                    startButton.setText("Start");
                    resetButton2.setEnabled(true);
                    startButton2.setEnabled(false);
                    startButton2.setText("Start");
                }

                @Override
                public void onGuiUpdate(double progress, CarShopSimulation.Result results) {
                    if (results != null) {
                        showResults(results);
                    }
                    String percent = String.format("%.2f", progress*100);
                    iterationPercentageLabel.setText(percent + "%");
                }

                @Override
                public void onGuiSimulationStatus(CarShopSimulation.State state) {
                    timeLabel.setText(CarShopSimulationCore.TimeUtils.formatDayTime(state.simulationTime));
                    type1FreeWorkersLabel.setText("" + state.freeType1workers);
                    type2FreeWorkersLabel.setText("" + state.freeType2workers);

                    DefaultListModel<String> listModel = new DefaultListModel<String>();
                    for (String s: state.cars) {
                        listModel.addElement(s);
                    }
                    carsList.setModel(listModel);
                }
            });
            if (continuousRunEnabledCheckbox.isSelected()) {
                simulation.setContinous(true);
            }

            new Thread(simulation).start();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(rootPanel, "Replication count / n should be a number.");
        }
    }

    private void showResults(CarShopSimulation.Result results) {
        entryWaitLabel.setText(CarShopSimulationCore.TimeUtils.formatTimePeriod(results.averageEntryWait.getMean()) + " (" + results.averageEntryWait.formatMeanWithConfidenceInterval() + ")");
        repairWaitLabel.setText(CarShopSimulationCore.TimeUtils.formatTimePeriod(results.averageTimeFromAcquisitionToReturn.getMean()) + " (" + results.averageTimeFromAcquisitionToReturn.formatMeanWithConfidenceInterval() + ")");
        carsInWorkshopLabel.setText(String.format("%.3f", results.carsInWorshop.getMean()));

        final double rep = results.currentReplication;
        finishedCustomersSeries.add(rep, results.finishedCustomers.getMean());
        refusedCustomersSeries.add(rep, results.refusedCustomers.getMean());
        timeInSystemSeries.add(rep, results.averageTimeInSystem.getMean());
        timeFromAcquieitionToEndSeries.add(rep, results.averageTimeFromAcquisitionToReturn.getMean());
        averageEntryWaitSeries.add(rep, results.averageEntryWait.getMean());
        averageEntryQueueSeries.add(rep, results.averageEntryQueue.getMean());
        averageRepairWaitSeries.add(rep, results.averageRepairWait.getMean());
        averageRepairQueueSeries.add(rep, results.averageRepairQueue.getMean());
        averageReturnWaitSeries.add(rep, results.averageReturnWait.getMean());
        averageReturnQueueSeries.add(rep, results.averageReturnQueue.getMean());
    }

    private void configureCheckbox(JCheckBox checkbox, XYSeries series) {
        checkbox.addActionListener(e -> {
            if (!checkbox.isSelected()) {
                dataCollection.removeSeries(series);
            } else {
                dataCollection.addSeries(series);
            }
        });
    }

    private void startButtonClicked() {
        if (simulation == null) {
            startSimulation();
            startButton.setText("Stop");
            startButton2.setText("Stop");
            resetButton.setEnabled(false);
            resetButton2.setEnabled(false);
        } else if (simulation.isRunning()) {
            simulation.pause();
            resetButton.setEnabled(true);
            startButton.setText("Resume");
            resetButton2.setEnabled(true);
            startButton2.setText("Resume");
        } else {
            simulation.resume();
            resetButton.setEnabled(false);
            startButton.setText("Stop");
            resetButton2.setEnabled(false);
            startButton2.setText("Stop");
        }
    }

    private void resetButtonClicked() {
        finishedCustomersSeries.clear();
        refusedCustomersSeries.clear();
        timeInSystemSeries.clear();
        timeFromAcquieitionToEndSeries.clear();
        averageEntryWaitSeries.clear();
        averageEntryQueueSeries.clear();
        averageRepairWaitSeries.clear();
        averageRepairQueueSeries.clear();
        averageReturnWaitSeries.clear();
        averageReturnQueueSeries.clear();

        simulation.cancel();
        simulation = null;
        startButton.setText("Start");
        startButton.setEnabled(true);
        startButton2.setText("Start");
        startButton2.setEnabled(true);
        repairWaitLabel.setText("");
        entryWaitLabel.setText("");
        resetButton.setEnabled(false);
        resetButton2.setEnabled(false);
    }
}
