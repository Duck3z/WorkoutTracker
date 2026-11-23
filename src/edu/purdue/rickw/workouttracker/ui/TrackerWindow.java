package edu.purdue.rickw.workouttracker.ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TrackerWindow extends JFrame {

    public TrackerWindow() {
        super("Workout Tracker");

        JLabel welcomeLabel = new JLabel("Workout Tracker", SwingConstants.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.CENTER);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    public void showWindow() {
        setVisible(true);
    }
}