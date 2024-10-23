package com.kyle.exercise.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.kyle.exercise.enums.Status;

import lombok.Data;

@Data
public class Exercise {

    @Id
    private String exerciseId;
    private String userId;
    private String name;
    private String type;
    private String muscleGroup; // maybe enum
//	private int sets;
//	private int reps;
//	private int intensity;
//	private int rpe;
//	private int load;
//	private int tempo;
//	private int rest;
    private LocalDate dateCreated;
    private List<String> errorsAndWarnings;
    private Status status;

}
