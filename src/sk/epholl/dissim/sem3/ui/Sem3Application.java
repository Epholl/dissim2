package sk.epholl.dissim.sem3.ui;

import javax.swing.*;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class Sem3Application {

    public static void main(String[] args) {

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            System.out.println("Cannot set native look and feel:");
        }

        SwingUtilities.invokeLater(() -> {
            new MainForm();
        });

    }
}
