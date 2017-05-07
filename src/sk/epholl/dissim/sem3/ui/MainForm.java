package sk.epholl.dissim.sem3.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class MainForm extends JFrame {
    private JPanel contentPanel;
    private JPanel mainControlPanel;
    private JButton runButton;
    private JPanel mainContentPanel;
    private JPanel statusBarPanel;
    private JProgressBar simulationProgressBar;
    private JTabbedPane mainTabbedPane;
    private JCheckBox continousRunCheckBox;

    public MainForm() {
        super("Parking lot simulation");
        setContentPane(contentPanel);
        setMinimumSize(new Dimension(1024, 768));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);

        testComponente();
    }

    private void testComponente() {
        simulationProgressBar.setString("Test string");
        simulationProgressBar.setValue(20);

        //RecursiveComponentEnabler.setEnabled(mainContentPanel, false);
        //mainTabbedPane.setEnabledAt(1, false);
    }
}
