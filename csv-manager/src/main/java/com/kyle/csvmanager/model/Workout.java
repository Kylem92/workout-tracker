package com.kyle.csvmanager.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Workout {

    private String workoutId;
    private String userId;
    private int dayNumber;
    private int weekNumber;
    private List<Exercise> exercises;
    private LocalDate dateCreated;

    public Workout(int weekNumber, int dayNumber) {
	this.weekNumber = weekNumber;
	this.dayNumber = dayNumber;
	this.exercises = new ArrayList<>();
    }

    public void addExercise(Exercise exercise) {
	exercises.add(exercise);
    }
}
