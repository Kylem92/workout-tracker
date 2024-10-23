package com.kyle.exercise.testhelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kyle.exercise.model.Exercise;

public class TestHelper {

    public static List<Exercise> createExercises(int i) {
	List<Exercise> exercises = new ArrayList<>();
	for (int j = 1; j <= i; j++) {
	    Exercise exercise = createExercise(String.valueOf(j), "exerciseName");
	    exercises.add(exercise);
	}
	return exercises;
    }

    public static Exercise createExercise(String exerciseId, String exerciseName) {
	Exercise user = new Exercise();
	user.setExerciseId(exerciseId);
	user.setName(exerciseName);
	user.setDateCreated(LocalDate.now());
	return user;
    }

}
