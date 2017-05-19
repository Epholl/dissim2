package sk.epholl.dissim.sem2.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import sk.epholl.dissim.sem2.Sem2Results;
import sk.epholl.dissim.sem2.Sem2SimulationParameters;
import sk.epholl.dissim.sem2.Sem2SimulationRunner;
import sk.epholl.dissim.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

/**
 * Created by Tomáš on 14.04.2016.
 */
public class MainWindow extends JFrame {

    Sem2SimulationRunner simulationRunner;

    private Sem2SimulationParameters params;

    private XYSeries data;
    private XYSeriesCollection dataCollection;

    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextField iterationCountTextField;
    private JButton runButton;
    private JComboBox variantsComboBox;
    private JLabel avgTimeLabel;
    private JLabel avgLoaderWaitLabel;
    private JLabel avgLoaderQueueLabel;
    private JLabel avgUnloaderWaitLabel;
    private JLabel avgUnloaderQueueLabel;
    private JLabel totalLoaderWaitLabel;
    private JLabel totalUnloaderWaitLabel;
    private JProgressBar iterationProgressBar;
    private JPanel chartPanel;
    private JButton runButtonSingle;
    private JComboBox variantsComboBoxSingle;
    private JSlider simulationSpeedSlider;
    private JLabel loaderCargoLabel;
    private JLabel hauledCargoLabel;
    private JLabel finishedCargoLabel;
    private JLabel v1State;
    private JLabel v2State;
    private JLabel v3State;
    private JLabel v4State;
    private JLabel v5State;
    private JLabel v1Cargo;
    private JLabel v2Cargo;
    private JLabel v3Cargo;
    private JLabel v4Cargo;
    private JLabel v5Cargo;
    private JLabel v1Runs;
    private JLabel v2Runs;
    private JLabel v3Runs;
    private JLabel v4Runs;
    private JLabel v5Runs;
    private JLabel simTimeLabel;

    private JLabel[] stateLabels;
    private JLabel[] cargoLabels;
    private JLabel[] runLabels;

    private int selectedTabIndex = 0;

    public MainWindow() {
        super("Sem 2");
        params = new Sem2SimulationParameters();
        setContentPane(tabbedPane1);
        setMinimumSize(new Dimension(1024, 768));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initChart();

        setVisible(true);

        tabbedPane1.addChangeListener(e1 -> {
            if (tabbedPane1.getSelectedIndex() != selectedTabIndex) {
                if (simulationRunner != null) {
                    simulationRunner.stop();
                }
                selectedTabIndex = tabbedPane1.getSelectedIndex();
            }
        });

        runButton.addActionListener(e -> {
            if (runButton.getText().equals("Run")) {
                resetLabelValues();
                runButton.setText("Stop");
                prepareMultipleSimulations();
                new Thread(simulationRunner).start();
            } else {
                runButton.setText("Run");
                simulationRunner.stop();
            }
        });

        runButtonSingle.addActionListener(e -> {
            if (runButtonSingle.getText().equals("Run")) {
                resetSingleRunLabelValues();
                runButtonSingle.setText("Reset");
                prepareSingleSimulation();
                new Thread((simulationRunner)).start();
            } else {
                runButtonSingle.setText("Run");
                simulationRunner.stop();
            }
        });

        simulationSpeedSlider.addChangeListener(event -> {
            if (simulationRunner != null) {
                simulationRunner.setContinousRunSpeed(simulationSpeedSlider.getValue());
            }
        });

        initLabels();
    }

    public void prepareMultipleSimulations() {

        simulationRunner = new Sem2SimulationRunner();
        autofigureIterationCount();
        simulationRunner.setSimulation(params, variantsComboBox.getSelectedIndex());

        simulationRunner.setSimListener(new Sem2SimulationRunner.Listener() {
            @Override
            public void onFinalResults(final Sem2SimulationRunner.StatisticCounter averageTime,
                                       final Sem2SimulationRunner.StatisticCounter totalLoaderWait,
                                       final Sem2SimulationRunner.StatisticCounter avgLoaderWait,
                                       final Sem2SimulationRunner.StatisticCounter avgLoaderQueue,
                                       final Sem2SimulationRunner.StatisticCounter totalUnloaderWait,
                                       final Sem2SimulationRunner.StatisticCounter avgUnloaderWait,
                                       final Sem2SimulationRunner.StatisticCounter avgUnloaderQueue) {
                SwingUtilities.invokeLater(() -> {
                    setIntervals(avgTimeLabel, averageTime, Utils::secondsToHours);
                    setIntervals(totalLoaderWaitLabel, totalLoaderWait, Utils::secondsToHours);
                    setIntervals(avgLoaderWaitLabel, avgLoaderWait, Utils::secondsToMinutes);
                    setIntervals(avgLoaderQueueLabel, avgLoaderQueue, Function.identity());
                    setIntervals(totalUnloaderWaitLabel, totalUnloaderWait, Utils::secondsToHours);
                    setIntervals(avgUnloaderWaitLabel, avgUnloaderWait, Utils::secondsToMinutes);
                    setIntervals(avgUnloaderQueueLabel, avgUnloaderQueue, Function.identity());
                    runButton.setText("Run");
                });
            }

            @Override
            public void onPartialResults(int iterationCount, double averageTime) {
                SwingUtilities.invokeLater(() -> {
                    avgTimeLabel.setText("" + Utils.secondsToHours(averageTime));
                    iterationProgressBar.setValue(iterationCount);
                    iterationProgressBar.setString("" + iterationCount);

                    if (iterationCount > Math.min((iterationProgressBar.getMaximum() / 10), 10000)) {
                        addNewGraphValue(iterationCount, Utils.secondsToHours(averageTime));
                    }
                });
            }

            @Override
            public void onContinousUpdate(Sem2Results results) {
                // not in multiple runs
            }
        });
    }

    public void prepareSingleSimulation() {
        simulationRunner = new Sem2SimulationRunner();
        simulationRunner.setSimulation(params, variantsComboBoxSingle.getSelectedIndex());
        simulationRunner.setContinousRunSpeed(simulationSpeedSlider.getValue());
        simulationRunner.setContinuousRun(true);
        simulationRunner.setSimListener(new Sem2SimulationRunner.Listener() {
            @Override
            public void onFinalResults(Sem2SimulationRunner.StatisticCounter averageTime, Sem2SimulationRunner.StatisticCounter totalLoaderWait, Sem2SimulationRunner.StatisticCounter avgLoaderWait, Sem2SimulationRunner.StatisticCounter avgLoaderQueue, Sem2SimulationRunner.StatisticCounter totalUnloaderWait, Sem2SimulationRunner.StatisticCounter AvgUnloaderWait, Sem2SimulationRunner.StatisticCounter avgUnloaderQueue) {
                runButtonSingle.setText("Run");
            }

            @Override
            public void onPartialResults(int iterationCount, double averageTime) {

            }

            @Override
            public void onContinousUpdate(Sem2Results results) {
                /*finishedCargoLabel.setText("" + resultTypes.unloader.cargoAmount);
                loaderCargoLabel.setText("" + resultTypes.loader.cargoAmount);
                double hauledCargo = 0;

                for (int i = 0; i < resultTypes.vehicles.size(); i++) {
                    Vehicle v = resultTypes.vehicles.get(i);
                    hauledCargo += v.getCurrentLoad();
                    stateLabels[i].setText(v.getCurrentState());
                    cargoLabels[i].setText(v.getCurrentLoad() + "/" + v.getCapacity());
                    runLabels[i].setText("" + v.getFinishedLoadsCount());
                }

                hauledCargoLabel.setText("" + hauledCargo);
                simTimeLabel.setText(String.format("%2f", Utils.secondsToHours(resultTypes.simTime)));
            */}
        });
    }

    private void setIntervals(JLabel label, Sem2SimulationRunner.StatisticCounter statistic, Function<Double, Double> conversion) {
        label.setText(conversion.apply(statistic.getMean()) + ", <"
                + conversion.apply(statistic.getLeftConfidenceInterval()) + ", "
                + conversion.apply(statistic.getRightConfidenceInterval()) + ">");
    }

    private void autofigureIterationCount() {
        int iterationCount = 10000;
        try {
            iterationCount = Integer.parseInt(iterationCountTextField.getText());
        } catch (NumberFormatException e) {
            iterationCountTextField.setText("10000");
        }
        simulationRunner.setIterationCount(iterationCount);
        iterationProgressBar.setValue(0);
        iterationProgressBar.setMaximum(iterationCount);
        data.clear();
    }

    private void initChart() {

        data = new XYSeries("Average project duration");
        data.setMaximumItemCount(1000);

        dataCollection = new XYSeriesCollection(data);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "Replication count",
                "Average project duratrion",
                dataCollection,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
        NumberAxis axis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        axis.setAutoRangeIncludesZero(false);

        ChartPanel chartPanel = new ChartPanel(chart);

        this.chartPanel.setLayout(new BoxLayout(this.chartPanel, BoxLayout.PAGE_AXIS));
        this.chartPanel.add(chartPanel);
    }

    private void resetLabelValues() {
        avgTimeLabel.setText("");
        totalLoaderWaitLabel.setText("");
        avgLoaderWaitLabel.setText("");
        avgLoaderQueueLabel.setText("");
        totalUnloaderWaitLabel.setText("");
        avgUnloaderWaitLabel.setText("");
        avgUnloaderQueueLabel.setText("");
    }

    private void resetSingleRunLabelValues() {
        finishedCargoLabel.setText("0");
        hauledCargoLabel.setText("0");
        loaderCargoLabel.setText("" /*+ params.cargoAmount*/);
        simTimeLabel.setText("0");

        for (int i = 0; i < stateLabels.length; i++) {
            stateLabels[i].setText("N/A");
            cargoLabels[i].setText("N/A");
            runLabels[i].setText("0");
        }
    }

    private void addNewGraphValue(int replication, double value) {
        data.add(replication, value);
    }

    private void initLabels() {
        stateLabels = new JLabel[5];
        cargoLabels = new JLabel[5];
        runLabels = new JLabel[5];

        stateLabels[0] = v1State;
        stateLabels[1] = v2State;
        stateLabels[2] = v3State;
        stateLabels[3] = v4State;
        stateLabels[4] = v5State;

        cargoLabels[0] = v1Cargo;
        cargoLabels[1] = v2Cargo;
        cargoLabels[2] = v3Cargo;
        cargoLabels[3] = v4Cargo;
        cargoLabels[4] = v5Cargo;

        runLabels[0] = v1Runs;
        runLabels[1] = v2Runs;
        runLabels[2] = v3Runs;
        runLabels[3] = v4Runs;
        runLabels[4] = v5Runs;
    }
}
