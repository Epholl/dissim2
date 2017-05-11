package sk.epholl.dissim.sem2.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import sk.epholl.dissim.sem2.carshop.CarShopSimulation;
import sk.epholl.dissim.sem2.carshop.CarShopSimulationParameters;
import sk.epholl.dissim.sem2.core.Simulation;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tomáš on 10.03.2016.
 */
public class AverageQueueLengthsFrame extends JFrame {


    private XYSeries entryQueue;
    private XYSeries repairQueue;
    private XYSeries returnQueue;
    private XYSeriesCollection dataCollection;

    private JFreeChart chart;

    public AverageQueueLengthsFrame() {
        super("Average queue lengths");
        setMinimumSize(new Dimension(640, 480));

        setContentPane(init());
        new Thread(this::loadResults).start();

        pack();
        setVisible(true);
    }

    private JPanel init() {
        entryQueue = new XYSeries("Entry queue");
        entryQueue.setMaximumItemCount(10);
        repairQueue = new XYSeries("Repair queue");
        repairQueue.setMaximumItemCount(10);
        returnQueue = new XYSeries("Return queue");
        returnQueue.setMaximumItemCount(10);

        dataCollection = new XYSeriesCollection();
        dataCollection.addSeries(entryQueue);
        dataCollection.addSeries(repairQueue);
        dataCollection.addSeries(returnQueue);

        chart = ChartFactory.createHistogram(
                "",
                "Value 1 worker count",
                "Queue length",
                dataCollection,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );

        chart.setNotify(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);

        return chartPanel;
    }

    private void loadResults() {
        for (int type1 = 1; type1 < 11; type1++) {
            singleTest(type1, 21);
        }
    }

    public void singleTest(int type1, int type2) {
        CarShopSimulationParameters params = new CarShopSimulationParameters(
                100,
                10d,
                type1,
                type2,
                90*8);

        CarShopSimulation simulation = new CarShopSimulation(params, new Simulation.SimulationListener<CarShopSimulation.Result, CarShopSimulation.State>() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onFinished(CarShopSimulation.Result results) {
                SwingUtilities.invokeLater(() -> {
                    entryQueue.add(type1, results.averageEntryQueue.getMean());
                    repairQueue.add(type1, results.averageRepairQueue.getMean());
                    returnQueue.add(type1, results.averageReturnQueue.getMean());
                });
            }

            @Override
            public void onGuiUpdate(double progress, CarShopSimulation.Result results) {

            }

            @Override
            public void onGuiSimulationStatus(CarShopSimulation.State state) {

            }
        });
        simulation.run();
    }
}
