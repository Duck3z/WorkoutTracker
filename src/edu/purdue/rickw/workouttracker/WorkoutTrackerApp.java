package edu.purdue.rickw.workouttracker;

import edu.purdue.rickw.workouttracker.ui.TrackerWindow;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Application entry point for the workout tracker.
 */
public class WorkoutTrackerApp {
    public static void main(String[] args) {
        // Swing components must be created on the event-dispatch thread.
        SwingUtilities.invokeLater(() -> {
            try {
                TrackerWindow window = new TrackerWindow();
                window.showWindow();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not load workouts.csv: " + e.getMessage(),
                        "Load failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}