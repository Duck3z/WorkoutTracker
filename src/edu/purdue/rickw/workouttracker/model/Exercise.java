package edu.purdue.rickw.workouttracker.model;

/**
 * Stores the data for one workout exercise.
 */
public class Exercise {

    private String name;
    private String description;
    private String reps;
    private String sets;
    private String weight;
    private String date;
    private String imagePath;

    public Exercise() {
    }

    // Exercise parameters are strings to allow free-form input and avoid restricting the user.
    public Exercise(String name, String description, String reps, String sets, String weight, String date) {
        this(name, description, reps, sets, weight, date, "");
    }

    public Exercise(String name, String description, String reps, String sets, String weight, String date,
            String imagePath) {
        this.name = name;
        this.description = description;
        this.reps = reps;
        this.sets = sets;
        this.weight = weight;
        this.date = date;
        this.imagePath = imagePath;
    }
    public Exercise(String name, int reps, int sets) {
        this.name = name;
        this.reps = String.valueOf(reps);
        this.sets = String.valueOf(sets);
        // Initialize optional fields to the CSV-compatible empty values used by this constructor.
        this.description = "";
        this.weight = "";
        this.date = "";
        this.imagePath = "";
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}