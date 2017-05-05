package sk.epholl.dissim.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import sk.epholl.dissim.carshop.CarShopSimulation;
import sk.epholl.dissim.carshop.CarShopSimulationParameters;
import sk.epholl.dissim.core.Simulation;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tomáš on 10.03.2016.
 */
public class AverageTimeInSystemFrame extends JFrame {


    private XYSeries timeInSystem;
    private XYSeriesCollection dataCollection;

    private JFreeChart chart;

    public AverageTimeInSystemFrame() {
        super("Average time spent");
        setMinimumSize(new Dimension(640, 480));

        setContentPane(init());
        new Thread(this::loadResults).start();

        pack();
        setVisible(true);
    }

    private JPanel init() {
        timeInSystem = new XYSeries("Time spent");
        timeInSystem.setMaximumItemCount(10);

        dataCollection = new XYSeriesCollection();
        dataCollection.addSeries(timeInSystem);

        chart = ChartFactory.createHistogram(
                "",
                "Value 2 worker count",
                "Time in system (h)",
                dataCollection,
                PlotOrientation.VERTICAL,
                false,
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
        for (int type2 = 16; type2 < 27; type2++) {
            singleTest(5, type2);
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
                    timeInSystem.add(type2, results.averageTimeInSystem.getMean()/3600);
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
