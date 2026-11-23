package edu.purdue.rickw.workouttracker;

import edu.purdue.rickw.workouttracker.ui.TrackerWindow;
import javax.swing.SwingUtilities;

public class WorkoutTrackerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TrackerWindow window = new TrackerWindow();
            window.showWindow();
        });
    }
}