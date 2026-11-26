package edu.purdue.rickw.workouttracker.ui;

import edu.purdue.rickw.workouttracker.WorkoutLogger;
import edu.purdue.rickw.workouttracker.model.Exercise;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Owns the Swing presentation and forwards workout changes to the file-backed logger.
 * Keeping persistence outside this class keeps window events separate from CSV handling.
 */
public class TrackerWindow extends JFrame {

    private final WorkoutLogger workoutLogger;
    private final JPanel exerciseListPanel;
    private final JTextField exerciseNameField = new JTextField();
    private final JTextField descriptionField = new JTextField();
    private final JTextField repsField = new JTextField();
    private final JTextField setsField = new JTextField();
    private final JTextField weightField = new JTextField();
    private final JTextField dateField = new JTextField();

    public TrackerWindow() throws IOException {
        super("Workout Tracker");
        workoutLogger = new WorkoutLogger();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton addButton = new JButton("Add");
        JButton saveButton = new JButton("Save to File");

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        addInputField(inputPanel, "Exercise:", exerciseNameField, 0, 0, 1);
        addInputField(inputPanel, "Description:", descriptionField, 0, 2, 3);
        addInputField(inputPanel, "Reps:", repsField, 1, 0, 1);
        addInputField(inputPanel, "Sets:", setsField, 1, 2, 3);
        addInputField(inputPanel, "Weight:", weightField, 2, 0, 1);
        addInputField(inputPanel, "Date:", dateField, 2, 2, 3);
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 3;
        buttonConstraints.gridy = 3;
        buttonConstraints.anchor = GridBagConstraints.EAST;
        buttonConstraints.insets = new Insets(4, 4, 4, 4);
        inputPanel.add(addButton, buttonConstraints);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel saveButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButtonPanel.add(saveButton);
        topPanel.add(saveButtonPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        exerciseListPanel = new JPanel(new GridBagLayout());
        JScrollPane exerciseScrollPane = new JScrollPane(exerciseListPanel);
        exerciseScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        JPanel listSection = new JPanel(new BorderLayout(0, 8));
        listSection.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        listSection.add(new JLabel("Exercises"), BorderLayout.NORTH);
        listSection.add(exerciseScrollPane, BorderLayout.CENTER);
        add(listSection, BorderLayout.CENTER);

        ActionListener addExerciseAction = event -> addExercise();
        addButton.addActionListener(addExerciseAction);
        saveButton.addActionListener(event -> saveExercises());
        // Enter submits through the same path as the Add button, preventing duplicate action logic.
        for (JTextField field : new JTextField[] {
                exerciseNameField, descriptionField, repsField, setsField, weightField, dateField}) {
            field.addActionListener(addExerciseAction);
        }
        refreshExerciseList();

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void addInputField(JPanel panel, String labelText, JTextField field, int row,
            int labelColumn, int fieldColumn) {
        JLabel label = new JLabel(labelText);
        label.setLabelFor(field);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = labelColumn;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(4, 4, 4, 4);
        panel.add(label, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = fieldColumn;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(4, 4, 4, 4);
        panel.add(field, fieldConstraints);
    }

    private void addExercise() {
        String name = exerciseNameField.getText().trim();
        // An exercise name is required so newly added and persisted rows remain identifiable.
        if (name.isEmpty()) {
            return;
        }

        try {
            workoutLogger.saveExercise(new Exercise(
                    name,
                    descriptionField.getText().trim(),
                    repsField.getText().trim(),
                    setsField.getText().trim(),
                    weightField.getText().trim(),
                    dateField.getText().trim()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not save the workout: " + e.getMessage(),
                    "Save failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Clear only after persistence succeeds, preserving user input when saving fails.
        exerciseNameField.setText("");
        descriptionField.setText("");
        repsField.setText("");
        setsField.setText("");
        weightField.setText("");
        dateField.setText("");
        refreshExerciseList();
        exerciseNameField.requestFocusInWindow();
    }

    private void saveExercises() {
        try {
            workoutLogger.saveToFile();
            JOptionPane.showMessageDialog(this, "Workouts saved to workouts.csv.",
                    "Save complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not save the workout: " + e.getMessage(),
                    "Save failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshExerciseList() {
        // Rebuilding avoids stale rows after an exercise is inserted at the top of the logger.
        exerciseListPanel.removeAll();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 6, 0);

        for (int i = 0; i < workoutLogger.getExercises().size(); i++) {
            Exercise exercise = workoutLogger.getExercises().get(i);
            JPanel row = new JPanel(new BorderLayout());
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            JPanel exerciseDetails = new JPanel();
            exerciseDetails.setLayout(new BoxLayout(exerciseDetails, BoxLayout.Y_AXIS));
            JLabel nameLabel = new JLabel(displayValue(exercise.getName()));
            nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
            exerciseDetails.add(nameLabel);
            exerciseDetails.add(new JLabel("Description: " + displayValue(exercise.getDescription())));
            exerciseDetails.add(new JLabel("Reps: " + displayValue(exercise.getReps())));
            exerciseDetails.add(new JLabel("Sets: " + displayValue(exercise.getSets())));
            exerciseDetails.add(new JLabel("Weight: " + displayValue(exercise.getWeight())));
            exerciseDetails.add(new JLabel("Date: " + displayValue(exercise.getDate())));
            row.add(exerciseDetails, BorderLayout.CENTER);
            constraints.gridy = i;
            exerciseListPanel.add(row, constraints);
        }

        constraints.gridy = workoutLogger.getExercises().size();
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        exerciseListPanel.add(new JPanel(), constraints);
        exerciseListPanel.revalidate();
        exerciseListPanel.repaint();
    }

    private String displayValue(String value) {
        return value == null ? "" : value;
    }

    public void showWindow() {
        setVisible(true);
    }
}