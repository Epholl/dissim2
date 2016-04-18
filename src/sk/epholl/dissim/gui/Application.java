package sk.epholl.dissim.gui;

import javax.swing.*;

/**
 * Created by Tomáš on 14.04.2016.
 */
public class Application {
    public static void main(String[] args)
    {
        new Application();
    }

    public Application()
    {
        startSwingGUI();
    }

    public void startSwingGUI()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            System.out.println("Cannot set native look and feel:");
        }

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new MainWindow();
            }
        });
    }
}