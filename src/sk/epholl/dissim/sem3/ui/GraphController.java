package sk.epholl.dissim.sem3.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.util.subscribers.ResultManager;
import sk.epholl.dissim.util.subscribers.Subscriber;
import sk.epholl.dissim.util.subscribers.ValueType;

import javax.swing.*;
import java.util.HashMap;

/**
 * Created by Tomáš on 19.05.2017.
 */
public class GraphController {

    private JFreeChart chart;

    private JPanel containrPanel;
    private JComboBox comboBox;

    private XYSeriesCollection dataCollection;

    private HashMap<String, XYSeries> options;

    public GraphController(JPanel panel, JComboBox comboBox) {
        this.containrPanel = panel;
        this. comboBox = comboBox;
        initGraph();
        options = new HashMap<>();
    }

    public void init(ResultManager rm) {

        Rst.ResultType[] results = Rst.resultTypes();
        String[] resultNames = new String[results.length];
        for (int i = 0; i < results.length; i++) {
            String name = results[i].resultName;
            XYSeries series = new XYSeries(name);
            series.setMaximumItemCount(5000);
            resultNames[i] = name;
            options.put(resultNames[i], series);

            rm.addSubscriber(results[i].valueType, new Subscriber<Rst.Result>() {
                @Override
                public void onValueEmitted(Rst.Result value) {
                    series.add(value.replication, value.result.getMean());
                }
            });
        }

        comboBox.setModel(new DefaultComboBoxModel(resultNames));
        selectSeries(comboBox.getSelectedItem().toString());
        comboBox.addActionListener(e -> {
            selectSeries(comboBox.getSelectedItem().toString());
        });
    }

    public void clear() {
        for (XYSeries series: options.values()) {
            series.clear();
        }
    }

    private void selectSeries(String name) {
        dataCollection.removeAllSeries();
        dataCollection.addSeries(options.get(name));
        ((XYPlot)chart.getPlot()).getRangeAxis().setLabel(name);
    }

    private void initGraph() {
        dataCollection = new XYSeriesCollection();
        chart = ChartFactory.createXYLineChart(
                "",
                "Replication count",
                "",
                dataCollection,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );
        NumberAxis axis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        axis.setAutoRangeIncludesZero(false);

        ChartPanel chartPanel = new ChartPanel(chart);

        this.containrPanel.setLayout(new BoxLayout(this.containrPanel, BoxLayout.PAGE_AXIS));
        this.containrPanel.add(chartPanel);
    }
}
