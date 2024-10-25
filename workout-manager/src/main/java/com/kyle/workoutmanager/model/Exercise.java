package com.kyle.workoutmanager.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.kyle.workoutmanager.enums.Status;

import lombok.Data;

@Data
public class Exercise {

	@Id
	private String exerciseId;
	private String userId;
	private String name;
	private String type;
	private Integer sets;
	private Integer reps;
	private Integer intensity;
	private Integer rpe;
	private Integer load;
	private Integer tempo;
	private Integer rest;
	private List<String> errorsAndWarnings;
	private Status status;
	
}
