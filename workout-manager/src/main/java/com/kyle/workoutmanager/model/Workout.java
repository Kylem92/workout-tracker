package com.kyle.workoutmanager.model;

import java.time.LocalDate;
import java.util.List;

import com.kyle.workoutmanager.enums.Status;

import lombok.Data;

@Data
public class Workout {

	private String workoutId;
	private String userId;
	private List<Exercise> exercises;
	private WorkoutTemplate workoutTemplate;
	private LocalDate dateCreated;
	private List<String> errorsAndWarnings;
	private Status status;
}
