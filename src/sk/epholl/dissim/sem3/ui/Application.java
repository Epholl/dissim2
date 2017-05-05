package sk.epholl.dissim.sem3.ui;

import javax.swing.*;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class Application {

    public static final void main(String[] args) {

        /*JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        MainForm mainForm = new MainForm();
        frame.setContentPane(mainForm);
        frame.pack();
        frame.setVisible(true);*/

        SwingUtilities.invokeLater(() -> {
            new MainForm();
        });

    }
}
