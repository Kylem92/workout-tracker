package com.kyle.workoutmanager.testhelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kyle.workoutmanager.model.Workout;

public class TestHelper {

    public static List<Workout> createWorkouts(int i) {
	List<Workout> workouts = new ArrayList<>();
	for (int j = 1; j <= i; j++) {
	    Workout workout = createWorkout(String.valueOf(j));
	    workouts.add(workout);
	}
	return workouts;
    }

    public static Workout createWorkout(String workoutId) {
	Workout user = new Workout();
	user.setWorkoutId(workoutId);
	user.setDateCreated(LocalDate.now());
	return user;
    }

}
