package edu.purdue.rickw.workouttracker;

import edu.purdue.rickw.workouttracker.model.Exercise;
import java.util.ArrayList;

public class WorkoutLogger {

    private ArrayList<Exercise> exercises;

    public WorkoutLogger() {
        exercises = loadExercises();
    }

    //returns the list of exercises
    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    //loads exercises from the file, if it exists, otherwise returns an empty list
    public ArrayList<Exercise> loadExercises() {
        ArrayList<Exercise> exercises = new ArrayList<>();
        //fake exercises for testing purposes
        exercises.add(new Exercise("Push-ups", 20, 3));
        exercises.add(new Exercise("Squats", 15, 3));
        exercises.add(new Exercise("Pull-ups", 10, 3));
        
        return exercises;
    }
    public void saveExercise(Exercise exercise) {
        exercises.add(0, exercise);
        saveToFile(exercises);
    }

    private void saveToFile(ArrayList<Exercise> exercises) {
        //TODO
        // Persistence is not yet implemented.
    }
}
