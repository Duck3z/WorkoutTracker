package edu.purdue.rickw.workouttracker;

import edu.purdue.rickw.workouttracker.model.Exercise;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Loads and persists exercises in the local CSV file.
 */
public class WorkoutLogger {

    private static final Path WORKOUT_FILE = Paths.get("workouts.csv");
    private ArrayList<Exercise> exercises;

    public WorkoutLogger() throws IOException {
        exercises = loadExercises();
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public ArrayList<Exercise> loadExercises() throws IOException {
        ArrayList<Exercise> exercises = new ArrayList<>();
        // A missing file represents a first run rather than a read failure.
        if (Files.notExists(WORKOUT_FILE)) {
            return exercises;
        }

        try (BufferedReader reader = Files.newBufferedReader(WORKOUT_FILE, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    exercises.add(parseExercise(line));
                } catch (IOException e) {
                    throw new IOException("Invalid workouts.csv at line " + lineNumber + ": "
                            + e.getMessage(), e);
                }
            }
        }
        return exercises;
    }

    private Exercise parseExercise(String line) throws IOException {
        ArrayList<String> fields = new ArrayList<>();
        int index = 0;
        // This parser follows CSV quoting rules without adding an external library.
        while (true) {
            StringBuilder field = new StringBuilder();
            if (index < line.length() && line.charAt(index) == '"') {
                index++;
                boolean closed = false;
                while (index < line.length()) {
                    char current = line.charAt(index++);
                    if (current == '"') {
                        // Two quotes inside a quoted field represent one literal quote.
                        if (index < line.length() && line.charAt(index) == '"') {
                            field.append('"');
                            index++;
                        } else {
                            closed = true;
                            break;
                        }
                    } else {
                        field.append(current);
                    }
                }
                if (!closed) {
                    throw new IOException("Unterminated quoted field.");
                }
                if (index < line.length() && line.charAt(index) != ',') {
                    throw new IOException("Expected a comma after a quoted field.");
                }
            } else {
                while (index < line.length() && line.charAt(index) != ',') {
                    if (line.charAt(index) == '"') {
                        throw new IOException("Unexpected quote in an unquoted field.");
                    }
                    field.append(line.charAt(index++));
                }
            }
            fields.add(field.toString());

            if (index >= line.length()) {
                break;
            }
            index++;
            if (index == line.length()) {
                fields.add("");
                break;
            }
        }

        if (fields.size() != 6 && fields.size() != 7) {
            throw new IOException("Expected 6 or 7 fields but found " + fields.size() + ".");
        }
        return new Exercise(fields.get(0), fields.get(1), fields.get(2),
                fields.get(3), fields.get(4), fields.get(5),
                fields.size() == 7 ? fields.get(6) : "");
    }

    public void saveExercise(Exercise exercise) throws IOException {
        ArrayList<Exercise> updatedExercises = new ArrayList<>(exercises);
        updatedExercises.add(0, exercise);
        // Persist successfully before changing memory so a write failure does not appear as a saved exercise.
        saveToFile(updatedExercises);
        exercises.add(0, exercise);
    }

    public void saveToFile() throws IOException {
        saveToFile(exercises);
    }

    private void saveToFile(ArrayList<Exercise> exercises) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(WORKOUT_FILE, StandardCharsets.UTF_8)) {
            for (Exercise exercise : exercises) {
                writeCsvField(writer, exercise.getName());
                writer.write(',');
                writeCsvField(writer, exercise.getDescription());
                writer.write(',');
                writeCsvField(writer, exercise.getReps());
                writer.write(',');
                writeCsvField(writer, exercise.getSets());
                writer.write(',');
                writeCsvField(writer, exercise.getWeight());
                writer.write(',');
                writeCsvField(writer, exercise.getDate());
                writer.write(',');
                writeCsvField(writer, exercise.getImagePath());
                writer.newLine();
            }
        }
    }

    private void writeCsvField(BufferedWriter writer, String value) throws IOException {
        writer.write('"');
        if (value != null) {
            // Quoting is doubled inside a CSV field so commas and quotes remain valid in user input.
            writer.write(value.replace("\"", "\"\""));
        }
        writer.write('"');
    }
}
